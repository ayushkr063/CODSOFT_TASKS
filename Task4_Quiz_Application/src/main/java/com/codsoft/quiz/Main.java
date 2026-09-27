package com.codsoft.quiz;

import com.codsoft.quiz.ui.MainFrame;

import javax.swing.*;

/**
 * Main
 * 
 * Application entry point for the CodSoft Quiz Application with Timer.
 * Safely initializes the Swing GUI on the Event Dispatch Thread (EDT)
 * and sets up modern UI Look and Feel.
 */
public class Main {

    public static void main(String[] args) {
        // Set modern Look and Feel (Nimbus or System L&F)
        setupLookAndFeel();

        // Launch GUI safely on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }

    /**
     * Attempts to set the Nimbus Look and Feel for a polished modern UI,
     * falling back to System Look and Feel if unavailable.
     */
    private static void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equalsIgnoreCase(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Fallback: System Look and Feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // If Look and Feel fails, continue with default Swing appearance
            System.err.println("Notice: Could not load custom Look and Feel. Using default. Error: " + e.getMessage());
        }
    }
}
