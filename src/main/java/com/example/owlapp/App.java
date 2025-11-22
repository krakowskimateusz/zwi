package com.example.owlapp;

import javax.swing.*;
import java.awt.*;

public class App {
    public static void main(String[] args) {
        boolean headlessMode = GraphicsEnvironment.isHeadless();
        // allow override via arg
        for (String a : args == null ? new String[0] : args) if ("--headless".equals(a)) headlessMode = true;

        if (headlessMode) {
            System.out.println("Uruchomiono w trybie headless (konsolowym)");
            OntologyManager om = new OntologyManager();
            java.io.File f = null;
            if (args != null && args.length > 0) {
                f = new java.io.File(args[0]);
                if (!f.exists() || !f.isFile()) {
                    System.err.println("Plik nie istnieje: " + args[0]);
                    f = null;
                }
            }
            if (f == null) {
                java.io.File dataDir = new java.io.File("data");
                if (dataDir.exists() && dataDir.isDirectory()) {
                    java.io.File[] candidates = dataDir.listFiles(p -> p.isFile() && (p.getName().endsWith(".rdf") || p.getName().endsWith(".owl") || p.getName().endsWith(".xml")) );
                    if (candidates != null && candidates.length > 0) {
                        f = candidates[0];
                    } else {
                        System.err.println("Brak plików .rdf/.owl w katalogu data");
                    }
                } else {
                    System.err.println("Katalog data nie istnieje");
                }
            }

            if (f != null) {
                try {
                    om.loadOntology(f);
                    System.out.println("Wczytano ontologię: " + f.getAbsolutePath());
                    System.out.println("Namespace: " + om.getNamespace());
                    boolean ok = om.ensureReasoner();
                    System.out.println("Reasoner: " + (ok ? om.getReasonerName() : "(none)"));
                    if (ok) {
                        System.out.println("--- Wnioski (instancja -> klasa) ---");
                        for (String s : om.getInferredClassAssertions()) System.out.println(s);
                    } else {
                        System.out.println("Nie udało się uruchomić reasonera.");
                    }
                } catch (Exception ex) {
                    System.err.println("Błąd: " + ex.getMessage());
                    ex.printStackTrace(System.err);
                }
            }
            return;
        }

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
