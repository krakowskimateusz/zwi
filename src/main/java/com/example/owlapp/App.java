package com.example.owlapp;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            // jeśli podano argument ścieżki do pliku, spróbuj wczytać automatycznie
            if (args != null && args.length > 0) {
                java.io.File f = new java.io.File(args[0]);
                if (f.exists() && f.isFile()) {
                    mf.loadOntologyFile(f);
                } else {
                    System.err.println("Plik nie istnieje: " + args[0]);
                }
            }
            mf.setVisible(true);
        });
    }
}
