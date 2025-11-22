package com.example.owlapp;

import java.io.File;
import java.util.List;

public class CliLoad {
    public static void main(String[] args) {
        String path = "data/ml_ontology_part1.rdf";
        if (args != null && args.length > 0) path = args[0];
        File f = new File(path);
        if (!f.exists()) {
            System.err.println("Plik nie znaleziony: " + f.getAbsolutePath());
            System.exit(2);
        }
        OntologyManager om = new OntologyManager();
        try {
            om.loadOntology(f);
            System.out.println("Namespace: " + om.getNamespace());
            List<String> classes = om.getAllClasses();
            System.out.println("Classes in ontology: " + classes.size());
            int show = Math.min(10, classes.size());
            for (int i = 0; i < show; i++) System.out.println(" - " + classes.get(i));
            System.out.println("Individuals in ontology: " + om.getAllIndividuals().size());
            System.out.println("Uruchamiam reasoner (może być structural fallback)...");
            om.runReasoner();
            if (!classes.isEmpty()) {
                String first = classes.get(0);
                System.out.println("Przykładowe instancje klasy pierwszej: " + first);
                String local = first;
                int idx = Math.max(local.lastIndexOf('#'), local.lastIndexOf('/'));
                if (idx >= 0 && idx < local.length()-1) local = local.substring(idx+1);
                List<String> inst = om.getInstancesOfClass(local);
                for (String s : inst) System.out.println("  * " + s);
            }
        } catch (Exception ex) {
            System.err.println("Błąd: " + ex.getMessage());
            ex.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
