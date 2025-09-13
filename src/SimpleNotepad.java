import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class SimpleNotepad extends JFrame {
    private final JTextArea textArea;
    private final JFileChooser fileChooser;
    private File currentFile = null;
    private boolean isModified = false;

    public SimpleNotepad() {
        super("Untitled - Simple Notepad");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // we'll handle exit to prompt for save
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Text area with monospaced font
        textArea = new JTextArea();
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        fileChooser = new JFileChooser();

        createMenuBar();

        // Track modifications to the document
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { setModified(true); }
            @Override public void removeUpdate(DocumentEvent e) { setModified(true); }
            @Override public void changedUpdate(DocumentEvent e) { setModified(true); }
        });

        // Confirm on window close
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { exitApplication(); }
        });

        updateTitle();
    }

    private void setModified(boolean modified) {
        if (this.isModified != modified) {
            this.isModified = modified;
            updateTitle();
        }
    }

    private void updateTitle() {
        String name = (currentFile == null) ? "Untitled" : currentFile.getName();
        setTitle((isModified ? "*" : "") + name + " - Simple Notepad");
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        //  File menu
        JMenu fileMenu = new JMenu("File");

        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
        openItem.addActionListener(e -> openFile());

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        saveItem.addActionListener(e -> saveFile());

        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.addActionListener(e -> saveFileAs());

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> exitApplication());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        //  Edit menu (Cut/Copy/Paste)
        JMenu editMenu = new JMenu("Edit");

        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        cutItem.addActionListener(e -> textArea.cut());

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        copyItem.addActionListener(e -> textArea.copy());

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        pasteItem.addActionListener(e -> textArea.paste());

        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this,
                "Simple Notepad\nAuthor: Your Name\nVersion: 1.0",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        ));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    // Open a file (with unsaved changes check)
    private void openFile() {
        if (!confirmSaveIfNeeded()) return;

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File f = fileChooser.getSelectedFile();
            try {
                byte[] all = Files.readAllBytes(f.toPath());
                String content = new String(all, StandardCharsets.UTF_8);
                textArea.setText(content);
                currentFile = f;
                setModified(false);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error opening file:\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Save current file (if no file chosen, delegate to Save As)
    private boolean saveFile() {
        if (currentFile == null) {
            return saveFileAs();
        }
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(currentFile), StandardCharsets.UTF_8))) {
            writer.write(textArea.getText());
            setModified(false);
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Save As dialog
    private boolean saveFileAs() {
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File chosen = fileChooser.getSelectedFile();
            // Add .txt if user didn't specify an extension
            if (!chosen.getName().contains(".")) {
                chosen = new File(chosen.getAbsolutePath() + ".txt");
            }
            currentFile = chosen;
            return saveFile();
        }
        return false;
    }

    // Confirm saving unsaved changes
    private boolean confirmSaveIfNeeded() {
        if (!isModified) return true;
        int opt = JOptionPane.showConfirmDialog(this,
                "There are unsaved changes. Do you want to save them?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opt == JOptionPane.CANCEL_OPTION || opt == JOptionPane.CLOSED_OPTION) {
            return false; // cancel operation
        }
        if (opt == JOptionPane.YES_OPTION) {
            return saveFile();
        }
        return true; // user selected NO
    }

    // Exit action (confirm save)
    private void exitApplication() {
        if (!confirmSaveIfNeeded()) return;
        dispose();
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleNotepad notepad = new SimpleNotepad();
            notepad.setVisible(true);
        });
    }
}


