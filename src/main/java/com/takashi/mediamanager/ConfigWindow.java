package com.takashi.mediamanager;

import javax.swing.*;
import java.awt.*;
import java.util.prefs.Preferences;

/**
 * Startup configuration dialog.
 *
 * Lets the user set RootDir, OutputDir, DuplicateDir, VideoDir, ImageDir
 * and DateUnknownDir (normally hard-coded in FileInfoTypes) and remembers
 * whatever was entered last time using the Java Preferences API, so the
 * fields are pre-filled on the next run.
 *
 * Usage (typically at the top of Main.main):
 *
 *     ConfigWindow configWindow = new ConfigWindow();
 *     if (!configWindow.showDialog()) {
 *         System.exit(0); // user hit Cancel / closed the window
 *     }
 *     // FileInfoTypes.RootDir / OutputDir / etc. are now populated.
 */
public class ConfigWindow {

    private static final Preferences prefs = Preferences.userNodeForPackage(ConfigWindow.class);

    private static final String KEY_ROOT_DIR = "RootDir";
    private static final String KEY_OUTPUT_DIR = "OutputDir";
    private static final String KEY_DUPLICATE_DIR = "DuplicateDir";
    private static final String KEY_VIDEO_DIR = "VideoDir";
    private static final String KEY_IMAGE_DIR = "ImageDir";
    private static final String KEY_DATE_UNKNOWN_DIR = "DateUnknownDir";

    private final JDialog dialog;
    private final JTextField rootDirField;
    private final JTextField outputDirField;
    private final JTextField duplicateDirField;
    private final JTextField videoDirField;
    private final JTextField imageDirField;
    private final JTextField dateUnknownDirField;

    private boolean confirmed = false;

    public ConfigWindow() {
        dialog = new JDialog((Frame) null, "Media Manager Configuration", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Pre-fill with the last saved value, falling back to whatever
        // is currently in FileInfoTypes (the hard-coded default) the
        // very first time the dialog is ever shown.
        rootDirField = new JTextField(prefs.get(KEY_ROOT_DIR, FileInfoTypes.RootDir), 30);
        outputDirField = new JTextField(prefs.get(KEY_OUTPUT_DIR, FileInfoTypes.OutputDir), 30);
        duplicateDirField = new JTextField(prefs.get(KEY_DUPLICATE_DIR, FileInfoTypes.DuplicateDir), 30);
        videoDirField = new JTextField(prefs.get(KEY_VIDEO_DIR, FileInfoTypes.VideoDir), 30);
        imageDirField = new JTextField(prefs.get(KEY_IMAGE_DIR, FileInfoTypes.ImageDir), 30);
        dateUnknownDirField = new JTextField(prefs.get(KEY_DATE_UNKNOWN_DIR, FileInfoTypes.DateUnknownDir), 30);

        int row = 0;
        row = addRow(gbc, row, "Root Dir (source folder to scan):", rootDirField, true);
        row = addRow(gbc, row, "Output Dir:", outputDirField, true);
        row = addRow(gbc, row, "Duplicate Dir name:", duplicateDirField, false);
        row = addRow(gbc, row, "Video Dir name:", videoDirField, false);
        row = addRow(gbc, row, "Image Dir name:", imageDirField, false);
        row = addRow(gbc, row, "Date Unknown Dir name:", dateUnknownDirField, false);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> {
            if (!validateFields()) {
                return;
            }
            saveValues();
            confirmed = true;
            dialog.dispose();
        });
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dialog.dispose();
        });
        dialog.getRootPane().setDefaultButton(okButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null); // center on screen
    }

    private int addRow(GridBagConstraints gbc, int row, String labelText, JTextField field, boolean withBrowse) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        dialog.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        dialog.add(field, gbc);

        if (withBrowse) {
            JButton browseButton = new JButton("Browse...");
            browseButton.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser(
                        field.getText().isEmpty() ? "." : field.getText());
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = chooser.showOpenDialog(dialog);
                if (result == JFileChooser.APPROVE_OPTION) {
                    field.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            });
            gbc.gridx = 2;
            dialog.add(browseButton, gbc);
        }

        return row + 1;
    }

    /** Basic sanity check: none of the fields may be left blank. */
    private boolean validateFields() {
        JTextField[] fields = {
                rootDirField, outputDirField, duplicateDirField,
                videoDirField, imageDirField, dateUnknownDirField
        };
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog,
                        "All fields are required.",
                        "Invalid input",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return true;
    }

    /** Pushes the entered values into FileInfoTypes and persists them for next time. */
    private void saveValues() {
        FileInfoTypes.RootDir = rootDirField.getText().trim();
        FileInfoTypes.OutputDir = outputDirField.getText().trim();
        FileInfoTypes.DuplicateDir = duplicateDirField.getText().trim();
        FileInfoTypes.VideoDir = videoDirField.getText().trim();
        FileInfoTypes.ImageDir = imageDirField.getText().trim();
        FileInfoTypes.DateUnknownDir = dateUnknownDirField.getText().trim();

        prefs.put(KEY_ROOT_DIR, FileInfoTypes.RootDir);
        prefs.put(KEY_OUTPUT_DIR, FileInfoTypes.OutputDir);
        prefs.put(KEY_DUPLICATE_DIR, FileInfoTypes.DuplicateDir);
        prefs.put(KEY_VIDEO_DIR, FileInfoTypes.VideoDir);
        prefs.put(KEY_IMAGE_DIR, FileInfoTypes.ImageDir);
        prefs.put(KEY_DATE_UNKNOWN_DIR, FileInfoTypes.DateUnknownDir);
    }

    /**
     * Shows the dialog (it's modal, so this call blocks until the user
     * clicks OK/Cancel or closes the window).
     *
     * @return true if the user clicked OK (values were saved and applied
     *         to FileInfoTypes), false if they cancelled/closed the dialog.
     */
    public boolean showDialog() {
        dialog.setVisible(true);
        return confirmed;
    }
}
