# MiniProject2
# 📝 Simple Notepad (Java Swing)

A lightweight **Notepad-like text editor** built using **Java Swing**.  
This mini project demonstrates the use of **JFrame, JTextArea, JScrollPane, JMenuBar, JFileChooser, and JOptionPane** to create a desktop GUI application.

---

## 📌 Features

- **Text Editing**
  - Multi-line text editing using `JTextArea`
  - Scrollable editor (`JScrollPane`)
  - Monospaced font for clarity  

- **File Menu**
  - `Open` — Load an existing text file  
  - `Save` — Save current file  
  - `Save As` — Save file with a new name  
  - `Exit` — Close application (with unsaved changes prompt)  

- **Edit Menu**
  - `Cut` (Ctrl+X)  
  - `Copy` (Ctrl+C)  
  - `Paste` (Ctrl+V)  

- **Help Menu**
  - `About` — Shows app information  

- **Other**
  - Tracks unsaved changes (`*` in window title)  
  - Prompts to save before closing or opening another file  
  - UTF-8 encoding support  

---

## 🛠️ Technologies Used

- **Java 8+**  
- **Swing (javax.swing)** for GUI  
- **AWT (java.awt)** for event handling and layout  
- **I/O (java.io, java.nio.file)** for file operations  

---

## ⚙️ Requirements

- Java Development Kit (**JDK 8 or later**)  
- Any IDE (IntelliJ IDEA, Eclipse, NetBeans) **or** terminal with `javac`  

---

## 🚀 How to Run

### 1. Clone this repository
```bash
git clone https://github.com/your-username/simple-notepad.git
cd simple-notepad
