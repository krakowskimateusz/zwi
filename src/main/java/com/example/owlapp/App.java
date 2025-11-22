package com.example.owlapp;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            if (args != null && args.length > 0) {
                java.io.File f = new java.io.File(args[0]);
                if (f.exists() && f.isFile()) {
                    mf.loadOntologyFile(f);
                } else {
                    System.err.println("Plik nie istnieje: " + args[0]);
                }
            } else {
                // Spróbuj automatycznie wczytać pierwszy plik .rdf/.owl z folderu data
                java.io.File dataDir = new java.io.File("data");
                if (dataDir.exists() && dataDir.isDirectory()) {
                    java.io.File[] candidates = dataDir.listFiles(p -> p.isFile() && (p.getName().endsWith(".rdf") || p.getName().endsWith(".owl") || p.getName().endsWith(".xml")) );
                    if (candidates != null && candidates.length > 0) {
                        java.io.File f = candidates[0];
                        mf.loadOntologyFile(f);
                    } else {
                        System.err.println("Brak plików .rdf/.owl w katalogu data");
                    }
                } else {
                    System.err.println("Katalog data nie istnieje");
                }
            }
            mf.setVisible(true);
        });
    }
}
