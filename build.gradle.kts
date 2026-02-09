plugins {
    java
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("--source", "25", "--target", "25"))
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project(":kane-boot") {
    dependencies {
        implementation(project(":j2me-bluetooth"))
        implementation(project(":j2me-io"))
        implementation(project(":j2me-lcdui"))
        implementation(project(":j2me-media"))
        implementation(project(":j2me-midlet"))
        implementation(project(":j2me-rms"))
        implementation(project(":j2me-m3g"))
        implementation(project(":j2me-wma"))
        implementation(project(":j2me-nokia-ui"))
    }

tasks.register<Copy>("copyToLib") {
    from(tasks.jar)
    from(configurations.runtimeClasspath)
    into(layout.buildDirectory.dir("output/libs"))
}

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf(
            "--add-exports", "java.base/jdk.internal.classfile.components=com.emulator.boot"
        ))
    }

    tasks.withType<JavaExec> {
        jvmArgs(listOf(
            "--enable-preview",
            "--add-exports", "java.base/jdk.internal.classfile.components=com.emulator.boot"
        ))
    }

    // Custom JLink task configuration
    val buildDir = layout.buildDirectory.get().asFile
    val imageDir = buildDir.resolve("image")

    tasks.register<Exec>("jlink") {
        group = "distribution"
        description = "Builds a custom runtime image using jlink"
        dependsOn("jar") 
        // Ensure dependencies are built
        dependsOn(configurations.runtimeClasspath)

        outputs.dir(imageDir)

        doFirst {
            imageDir.deleteRecursively()
        }

        // Resolve all dependencies (JARs)
        val modulePathFiles = configurations.runtimeClasspath.get().files + tasks.jar.get().archiveFile.get().asFile
        val modulePath = modulePathFiles.joinToString(File.pathSeparator)

        val jmodsDir = javaToolchains.launcherFor(java.toolchain).get()
            .metadata.installationPath.asFile.resolve("jmods")
        val jmods = jmodsDir.listFiles { file -> file.extension == "jmod" }?.joinToString(File.pathSeparator) { it.absolutePath } ?: ""
        val fullModulePath = if (jmods.isNotEmpty()) "$modulePath${File.pathSeparator}$jmods" else modulePath

        // Find jlink executable
        val jlinkTool = javaToolchains.launcherFor(java.toolchain).get()
            .metadata.installationPath.asFile.resolve("bin/jlink").absolutePath

        commandLine(
            jlinkTool,
            "--module-path", modulePath,
            "--add-modules", "ALL-MODULE-PATH",
            "--output", imageDir.absolutePath,
            "--launcher", "kane=com.emulator.boot/com.emulator.boot.Main",
            "--no-header-files",
            "--no-man-pages",
            "--add-options=--add-modules=ALL-DEFAULT --add-exports=java.base/jdk.internal.classfile.components=com.emulator.boot"
        )
        
        doLast {
            println("JLink image created at: ${imageDir.absolutePath}")
        }
    }
}

project(":j2me-lcdui") {
    dependencies {
        implementation(project(":j2me-midlet"))
    }
}


project(":j2me-media") {
    dependencies {
        implementation(project(":j2me-lcdui"))
    }
}

project(":j2me-wma") {
    dependencies {
        implementation(project(":j2me-io"))
    }
}

project(":j2me-nokia-ui") {
    dependencies {
        implementation(project(":j2me-lcdui"))
    }
}

