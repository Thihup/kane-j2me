package com.emulator.boot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReader;
import java.lang.module.ModuleReference;
import java.lang.module.ModuleDescriptor;
import java.lang.ModuleLayer;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.SwingUtilities;

// Imports for the ClassFile API
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.ClassFile;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.attribute.ModuleAttribute;

import jdk.internal.classfile.components.ClassRemapper;

import java.lang.constant.ClassDesc;
import java.lang.constant.ModuleDesc;
import java.lang.constant.PackageDesc;

public class Main {

    private static final String GAME_MODULE_NAME = "com.emulator.game";
    private static final String GAME_PACKAGE_NAME = "com.emulator.game";

    // --- In-Memory Module Implementation ---

    public static class InMemoryModuleFinder implements ModuleFinder {
        private final RemappedModuleData moduleData;
        private ModuleReference moduleReference;

        public InMemoryModuleFinder(RemappedModuleData moduleData) {
            this.moduleData = moduleData;
        }

        @Override
        public Optional<ModuleReference> find(String name) {
            if (name.equals(moduleData.descriptor.name())) {
                if (moduleReference == null) {
                    moduleReference = new InMemoryModuleReference(moduleData);
                }
                return Optional.of(moduleReference);
            }
            return Optional.empty();
        }

        @Override
        public Set<ModuleReference> findAll() {
            return Set.of(find(moduleData.descriptor.name()).orElseThrow());
        }
    }

    static {
        // Register the custom URLStreamHandlerFactory for j2me-game protocol
        try {
            java.net.URL.setURLStreamHandlerFactory(protocol -> {
                if ("j2me-game".equals(protocol)) {
                    return new java.net.URLStreamHandler() {
                        @Override
                        protected java.net.URLConnection openConnection(URL url) throws IOException {
                            return new java.net.URLConnection(url) {
                                @Override
                                public void connect() throws IOException {}

                                @Override
                                public InputStream getInputStream() throws IOException {
                                    String path = url.getPath().startsWith("/") ? url.getPath().substring(1) : url.getPath();
                                    for (InMemoryModuleReference moduleRef : InMemoryModuleReferenceRegistry.getAllModuleReferences()) {
                                        Optional<InputStream> resourceStream = moduleRef.open().open(path);
                                        if (resourceStream.isPresent()) {
                                            return resourceStream.get();
                                        }
                                    }
                                    throw new IOException("Resource not found in any dynamic module: " + path);
                                }
                            };
                        }
                    };
                }
                return null;
            });
        } catch (Error e) {
            // Already set or other initialization error, ignore if it's just a duplicate factory setting
        }
    }
    
    static class InMemoryModuleReferenceRegistry {
        private static final Map<String, InMemoryModuleReference> registry = new HashMap<>();

        public static void registerModuleReference(InMemoryModuleReference moduleRef) {
            registry.put(moduleRef.descriptor().name(), moduleRef);
        }

        public static Collection<InMemoryModuleReference> getAllModuleReferences() {
            return registry.values();
        }
    }

    public static class InMemoryModuleReference extends ModuleReference {
        private final RemappedModuleData moduleData;

        public InMemoryModuleReference(RemappedModuleData moduleData) {
            super(moduleData.descriptor, URI.create("j2me-game://" + moduleData.descriptor.name()));
            this.moduleData = moduleData;
            InMemoryModuleReferenceRegistry.registerModuleReference(this);
        }

        @Override
        public ModuleReader open() {
            return new InMemoryModuleReader(moduleData);
        }
    }

    public static class InMemoryModuleReader implements ModuleReader {
        private final RemappedModuleData moduleData;
        private boolean closed = false;

        public InMemoryModuleReader(RemappedModuleData moduleData) {
            this.moduleData = moduleData;
        }

        @Override
        public Optional<InputStream> open(String name) throws IOException {
            if (closed) throw new IOException("Reader is closed");
            return Optional.ofNullable(moduleData.resources.get(name)).map(ByteArrayInputStream::new);
        }
        
        @Override
        public Optional<ByteBuffer> read(String name) throws IOException {
            if (closed) throw new IOException("Reader is closed");
             return Optional.ofNullable(moduleData.resources.get(name)).map(ByteBuffer::wrap);
        }

        @Override
        public Optional<URI> find(String name) throws IOException {
            if (closed) throw new IOException("Reader is closed");
            if (moduleData.resources.containsKey(name)) {
                return Optional.of(URI.create("j2me-game:/" + name));
            }
            return Optional.empty();
        }

        @Override
        public Stream<String> list() {
            if (closed) throw new IllegalStateException("Reader is closed");
            return moduleData.resources.keySet().stream();
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    public static class RemappedModuleData {
        final ModuleDescriptor descriptor;
        final Map<String, byte[]> resources = new HashMap<>();

        RemappedModuleData(ModuleDescriptor descriptor) {
            this.descriptor = descriptor;
        }
    }

    public static void trapHalt(int status) {
        System.err.println("!!! TRAPPED Runtime.halt(" + status + ") !!!");
        System.exit(status);
    }

    public static void trapExit(int status) {
        System.err.println("!!! TRAPPED System.exit(" + status + ") !!!");
        System.exit(status);
    }

    public static RemappedModuleData remapJarInMemory(Path jarPath, String moduleName, String packageName) throws IOException {
        System.out.println(">>> REMAPPER: Processing JAR: " + jarPath);
        String packagePath = packageName.replace('.', '/');

        java.util.function.Function<ClassDesc, ClassDesc> remapperFunction = (className) -> {
            if (className.packageName().isEmpty()) {
                return ClassDesc.ofInternalName(packagePath + "/" + className.displayName());
            }
            return className;
        };

        ClassTransform classRemapper = ClassRemapper.of(remapperFunction);
        
        java.lang.classfile.CodeTransform exitTrapper = (codeBuilder, element) -> {
            if (element instanceof java.lang.classfile.instruction.InvokeInstruction inv) {
                String owner = inv.owner().name().stringValue();
                String name = inv.name().stringValue();
                if (owner.equals("java/lang/System") && name.equals("exit")) {
                    codeBuilder.invokestatic(ClassDesc.of("com.emulator.boot.Main"), "trapExit", java.lang.constant.MethodTypeDesc.ofDescriptor("(I)V"));
                    return;
                }
                if (owner.equals("java/lang/Runtime") && name.equals("halt")) {
                    codeBuilder.invokestatic(ClassDesc.of("com.emulator.boot.Main"), "trapHalt", java.lang.constant.MethodTypeDesc.ofDescriptor("(I)V"));
                    return;
                }
            }
            codeBuilder.with(element);
        };

        Set<String> j2meApiModules = Set.of("j2me.bluetooth", "j2me.io", "j2me.lcdui", "j2me.media", "j2me.midlet", "j2me.rms");
        
        ModuleDescriptor.Builder descriptorBuilder = ModuleDescriptor.newModule(moduleName)
                .packages(Set.of(packageName))
                .requires("java.base");

        for (String j2meModule : j2meApiModules) {
            descriptorBuilder.requires(j2meModule);
        }
        descriptorBuilder.exports(packageName);
        
        ModuleDescriptor descriptor = descriptorBuilder.build();
        RemappedModuleData moduleData = new RemappedModuleData(descriptor);

        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            for (JarEntry entry : jarFile.stream().collect(Collectors.toList())) {
                if (entry.isDirectory()) continue;

                String entryName = entry.getName();
                byte[] bytes = jarFile.getInputStream(entry).readAllBytes();

                if (entryName.endsWith(".class") && !entryName.contains("/")) {
                    String originalClassName = entryName.substring(0, entryName.length() - 6);
                    String newClassName = packagePath + "/" + originalClassName;
                    ClassDesc newClassDesc = ClassDesc.ofInternalName(newClassName);
                    
                    ClassModel sourceModel = ClassFile.of().parse(bytes);
                    
                    ClassTransform combinedTransform = classRemapper.andThen(
                        (classBuilder, element) -> {
                            if (element instanceof java.lang.classfile.MethodModel method) {
                                classBuilder.withMethod(method.methodName(), method.methodType(), method.flags().flagsMask(), methodBuilder -> {
                                    methodBuilder.withCode(codeBuilder -> {
                                        method.code().ifPresent(code -> {
                                            code.forEach(ce -> exitTrapper.accept(codeBuilder, ce));
                                        });
                                    });
                                    method.forEach(mElement -> {
                                        if (!(mElement instanceof java.lang.classfile.attribute.CodeAttribute)) {
                                            methodBuilder.with(mElement);
                                        }
                                    });
                                });
                            } else {
                                classBuilder.with(element);
                            }
                        }
                    );

                    byte[] newBytes = ClassFile.of().transformClass(sourceModel, newClassDesc, combinedTransform);
                    String newEntryName = newClassName + ".class";
                    moduleData.resources.put(newEntryName, newBytes);
                } else if (!entryName.startsWith("META-INF/")) {
                    moduleData.resources.put(entryName, bytes);
                }
            }
        }

        byte[] moduleInfoBytes = ClassFile.of().buildModule(ModuleAttribute.of(
                ModuleDesc.of(moduleName),
                classAttrBuilder -> {
                    classAttrBuilder.requires(ModuleDesc.of("java.base"), ClassFile.ACC_MANDATED, null);
                    for(String api : j2meApiModules) {
                        classAttrBuilder.requires(ModuleDesc.of(api), 0, null);
                    }
                    classAttrBuilder.exports(PackageDesc.of(packageName), 0);
                }
            ));
        
        moduleData.resources.put("module-info.class", moduleInfoBytes);
        System.out.println(">>> REMAPPER: Remapping complete.");
        return moduleData;
    }

    private static String findMainMIDlet(Path jarPath) throws IOException {
        try (JarFile jarFile = new JarFile(jarPath.toFile())) {
            Manifest manifest = jarFile.getManifest();
            if (manifest == null) return null;
            Attributes mainAttrs = manifest.getMainAttributes();
            // MIDlet-1 entry usually looks like: MIDletName, /icon.png, com.example.MyMIDlet
            String midlet1 = mainAttrs.getValue("MIDlet-1");
            if (midlet1 != null) {
                String[] parts = midlet1.split(",");
                if (parts.length >= 3) {
                    return parts[2].trim();
                }
            }
            // Fallback: check for MIDlet-Name or other entries if needed
        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java ... com.emulator.boot.Main <path-to-game.jar>");
            System.exit(1);
        }

        String jarPathStr = args[0];
        Path jarPath = Paths.get(jarPathStr);

        // Set standard J2ME system properties
        System.setProperty("microedition.profiles", "MIDP-2.1");
        System.setProperty("microedition.configuration", "CLDC-1.1");
        System.setProperty("microedition.platform", "j2me-emulator-v2");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            System.err.println("UNCAUGHT EXCEPTION on thread " + t.getName() + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        });

        try {
            System.out.println(">>> EMULATOR: Loading Game: " + jarPath.getFileName());

            // 1. Auto-detect main MIDlet class
            String originalMainClass = findMainMIDlet(jarPath);
            if (originalMainClass == null) {
                System.err.println("ERROR: Could not find MIDlet-1 entry in JAR manifest.");
                System.exit(2);
            }
            System.out.println(">>> EMULATOR: Detected Main MIDlet: " + originalMainClass);

            // 2. Remap the game JAR in-memory
            RemappedModuleData gameModuleData = remapJarInMemory(jarPath, GAME_MODULE_NAME, GAME_PACKAGE_NAME);

            // 3. Setup Module Layer
            ModuleFinder gameFinder = new InMemoryModuleFinder(gameModuleData);

            Set<String> gameModule = Set.of(GAME_MODULE_NAME);
            ModuleLayer bootLayer = ModuleLayer.boot();
            Configuration config = bootLayer.configuration().resolve(gameFinder, ModuleFinder.of(), gameModule);

            var gameController = ModuleLayer.defineModulesWithOneLoader(config, List.of(bootLayer), ClassLoader.getSystemClassLoader());
            ModuleLayer layer = gameController.layer();
            
            Thread.currentThread().setContextClassLoader(layer.findLoader(GAME_MODULE_NAME));

            // 4. Instantiate and Launch
            // The class was remapped to GAME_PACKAGE_NAME
            String remappedMainClass = GAME_PACKAGE_NAME + "." + originalMainClass;
            gameController.addOpens(layer.findModule(GAME_MODULE_NAME).orElseThrow(), GAME_PACKAGE_NAME, Main.class.getModule());
            Class<?> midletClass = Class.forName(remappedMainClass, true, layer.findLoader(GAME_MODULE_NAME));
            
            Object midletInstance = midletClass.getDeclaredConstructor().newInstance();
            System.out.println(">>> EMULATOR: MIDlet instantiated.");

            Method startAppMethod = midletClass.getDeclaredMethod("startApp");
            startAppMethod.setAccessible(true);
            
            System.out.println(">>> EMULATOR: Launching startApp()...");
            startAppMethod.invoke(midletInstance);

            try { Thread.sleep(Long.MAX_VALUE); } catch (InterruptedException e) {}

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(3);
        }

        
    }
}