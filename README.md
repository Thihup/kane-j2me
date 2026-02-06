# 🕹️ KANE — Kane Ain't No Emulator

**KANE** is a high-performance J2ME compatibility layer designed to run legacy mobile games (`.jar` MIDlets) natively on modern Java environments. 

Unlike traditional emulators that simulate a mobile CPU, **KANE** performs dynamic bytecode remapping and uses custom Module Layers to execute J2ME code directly on your stock JVM. This results in superior performance, modern windowing support, and low overhead.

---

## 🚀 Key Features

*   **Native Execution:** No CPU emulation. Code runs at the speed of your host JVM.
*   **JDK 25 ClassFile API:** Built on the bleeding edge using the finalized `java.lang.classfile` API for bytecode transformation.
*   **Dynamic Remapping:** Automatically moves default-package J2ME classes into a safe, modular namespace (`com.emulator.game`).
*   **Modular Architecture:** Uses JPMS (Java Module System) and `ModuleLayer` to isolate game code from the runtime.
*   **Standalone Distribution:** Includes `jlink` support to create a minimal, self-contained runtime image.
*   **CI/CD Ready:** Full Gradle integration and GitHub Actions workflow out of the box.

---

## 🛠️ Prerequisites

*   **Java 25 (Early Access or later):** KANE leverages the latest finalized features of the `ClassFile` API.
*   **Gradle:** (Included wrapper is recommended).

---

## 📦 Building

To compile all modules and prepare the runtime:

```bash
./gradlew build
```

### Create a Standalone Runtime Image (JLink)
To generate a minimal, self-contained distribution:

```bash
./gradlew :kane-boot:jlink
```
The resulting image will be located in `kane-boot/build/image`.

---

## 🎮 Running a Game

You can run a MIDlet JAR directly through Gradle or via the standalone binary.

### Using Gradle:
```bash
./gradlew :kane-boot:run --args="path/to/your/game.jar"
```

### Using Standalone Image:
```bash
./kane-boot/build/image/bin/kane "path/to/your/game.jar"
```

---

## 🧠 How it Works

1.  **Ingestion:** KANE reads the provided Game JAR and parses its `MANIFEST.MF` to find the main MIDlet class.
2.  **Transformation:** Using the **JDK 25 ClassFile API**, it iterates through every class in the JAR, remapping references from the default package to `com.emulator.game`.
3.  **Isolation:** A new `ModuleLayer` is created in-memory. The remapped game code is loaded into this layer as a dynamic module.
4.  **Execution:** The runtime provides its own implementation of `javax.microedition.*` APIs (LCDUI, RMS, Connection Framework) backed by modern Java SE counterparts (Swing, File NIO).

---

## 🤝 Contributing

Kane is an open-source project. If you'd like to help implement missing J2ME APIs (Bluetooth, Media, etc.), feel free to open a Pull Request!

---

## ⚖️ License

MIT License - Copyright (c) 2026
