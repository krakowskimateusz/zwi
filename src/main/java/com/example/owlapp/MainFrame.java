package com.example.owlapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

public class MainFrame extends JFrame {
    private OntologyManager om = new OntologyManager();
    private JTextArea outputArea = new JTextArea(15, 60);
    private JLabel statusLabel = new JLabel("Brak załadowanej ontologii");
    private JTextField nsField = new JTextField(40);
    private JComboBox<String> classCombo = new JComboBox<>();
    private JLabel reasonerLabel = new JLabel("Reasoner: (none)");

    public MainFrame() {
        super("OWL Swing App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Programowe wczytanie ontologii z pliku (używane przy starcie, gdy podano argument).
     */
    public void loadOntologyFile(File f) {
        try {
            om.loadOntology(f);
            statusLabel.setText("Załadowano: " + f.getName());
            nsField.setText(om.getNamespace());
            appendOut("Wczytano ontologię: " + f.getAbsolutePath());
            // uzupełnij listę klas
            classCombo.removeAllItems();
            for (String c : om.getAllClasses()) {
                classCombo.addItem(c);
            }
            try {
                reasonerLabel.setText("Reasoner: " + om.getReasonerName());
            } catch (Exception ignore) {}
        } catch (Exception ex) {
            appendOut("Błąd wczytywania: " + ex.getMessage());
            statusLabel.setText("Błąd ładowania");
        }
    }

    private void initUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadBtn = new JButton("Wczytaj .rdf/.owl");
        loadBtn.addActionListener(this::onLoad);
        top.add(loadBtn);
        top.add(statusLabel);
        JButton saveBtn = new JButton("Zapisz ontologię");
        saveBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int res = chooser.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                try {
                    om.saveOntology(chooser.getSelectedFile());
                    appendOut("Zapisano ontologię do: " + chooser.getSelectedFile().getAbsolutePath());
                } catch (Exception ex) {
                    appendOut("Błąd zapisu: " + ex.getMessage());
                }
            }
        });
        top.add(saveBtn);
        top.add(reasonerLabel);
        panel.add(top);

        JPanel nsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nsPanel.add(new JLabel("Namespace:"));
        nsField.setEditable(false);
        nsPanel.add(nsField);
        panel.add(nsPanel);

        panel.add(Box.createVerticalStrut(6));

        // Add individual
        JPanel addInd = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addInd.setBorder(BorderFactory.createTitledBorder("Dodaj instancję"));
        JTextField indName = new JTextField(12);
        JTextField indClass = new JTextField(12);
        JButton indBtn = new JButton("Dodaj instancję");
        indBtn.addActionListener(e -> {
            try {
                om.addIndividual(indName.getText().trim(), indClass.getText().trim());
                appendOut("Dodano instancję: " + indName.getText());
            } catch (Exception ex) {
                appendOut("Błąd: " + ex.getMessage());
            }
        });
        addInd.add(new JLabel("Nazwa:")); addInd.add(indName);
        addInd.add(new JLabel("Klasa:")); addInd.add(indClass);
        addInd.add(indBtn);
        panel.add(addInd);

        // Data property
        JPanel dataProp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dataProp.setBorder(BorderFactory.createTitledBorder("Dodaj data property"));
        JTextField dpSubj = new JTextField(10);
        JTextField dpProp = new JTextField(10);
        JTextField dpLit = new JTextField(10);
        JButton dpBtn = new JButton("Dodaj literal");
        dpBtn.addActionListener(e -> {
            try {
                om.addDataProperty(dpSubj.getText().trim(), dpProp.getText().trim(), dpLit.getText().trim());
                appendOut("Dodano data property: " + dpProp.getText());
            } catch (Exception ex) {
                appendOut("Błąd: " + ex.getMessage());
            }
        });
        dataProp.add(new JLabel("Podmiot:")); dataProp.add(dpSubj);
        dataProp.add(new JLabel("Właściwość:")); dataProp.add(dpProp);
        dataProp.add(new JLabel("Wartość:")); dataProp.add(dpLit);
        dataProp.add(dpBtn);
        panel.add(dataProp);

        // Object property
        JPanel objProp = new JPanel(new FlowLayout(FlowLayout.LEFT));
        objProp.setBorder(BorderFactory.createTitledBorder("Dodaj object property"));
        JTextField opSubj = new JTextField(10);
        JTextField opProp = new JTextField(10);
        JTextField opObj = new JTextField(10);
        JButton opBtn = new JButton("Dodaj relację");
        opBtn.addActionListener(e -> {
            try {
                om.addObjectProperty(opSubj.getText().trim(), opProp.getText().trim(), opObj.getText().trim());
                appendOut("Dodano object property: " + opProp.getText());
            } catch (Exception ex) {
                appendOut("Błąd: " + ex.getMessage());
            }
        });
        objProp.add(new JLabel("Podmiot:")); objProp.add(opSubj);
        objProp.add(new JLabel("Właściwość:")); objProp.add(opProp);
        objProp.add(new JLabel("Obiekt:")); objProp.add(opObj);
        objProp.add(opBtn);
        panel.add(objProp);

        // Reasoner + inquiry
        JPanel reasonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton reasonBtn = new JButton("Uruchom reasoner");
        reasonBtn.addActionListener(e -> {
            om.runReasoner();
            appendOut("Reasoner uruchomiony.");
            try {
                reasonerLabel.setText("Reasoner: " + om.getReasonerName());
            } catch (Exception ignore) {}
            // pokaż wyniki wnioskowania
            try {
                List<String> inf = om.getInferredClassAssertions();
                appendOut("--- Wnioski (instancja -> klasa) ---");
                for (String s : inf) appendOut(s);
            } catch (Exception ex) {
                appendOut("Błąd podczas pobierania wniosków: " + ex.getMessage());
            }
        });
        JButton showInst = new JButton("Pokaż instancje klasy");
        showInst.addActionListener(e -> {
            try {
                String selected = (String) classCombo.getSelectedItem();
                if (selected == null || selected.isEmpty()) {
                    appendOut("Wybierz klasę z listy.");
                    return;
                }
                // compute local name
                String local = selected;
                int idx = Math.max(local.lastIndexOf('#'), local.lastIndexOf('/'));
                if (idx >= 0 && idx < local.length()-1) local = local.substring(idx+1);
                if (!om.ensureReasoner()) {
                    appendOut("Reasoner nie jest uruchomiony i nie można uzyskać instancji.");
                    try { reasonerLabel.setText("Reasoner: (none)"); } catch (Exception ignore) {}
                    return;
                }
                try { reasonerLabel.setText("Reasoner: " + om.getReasonerName()); } catch (Exception ignore) {}
                List<String> instances = om.getInstancesOfClass(local);
                appendOut("Instancje klasy " + selected + ":");
                for (String s : instances) appendOut(" - " + s);
            } catch (Exception ex) {
                appendOut("Błąd: " + ex.getMessage());
            }
        });
        reasonPanel.add(reasonBtn);
        reasonPanel.add(new JLabel("Klasa:")); reasonPanel.add(classCombo); reasonPanel.add(showInst);
        panel.add(reasonPanel);

        // Dodaj listener do classCombo, żeby przy zmianie wyboru pokazywać instancje
        classCombo.addActionListener(e -> {
            try {
                String selected = (String) classCombo.getSelectedItem();
                if (selected == null || selected.isEmpty()) return;
                String local = selected;
                int idx = Math.max(local.lastIndexOf('#'), local.lastIndexOf('/'));
                if (idx >= 0 && idx < local.length()-1) local = local.substring(idx+1);

                // najpierw pokaż podklasy (jeśli są)
                List<String> subs = om.getSubClassesOf(local);
                if (subs != null && !subs.isEmpty()) {
                    appendOut("Podklasy " + selected + ":");
                    for (String s : subs) appendOut(" - " + s);
                }

                // zapewnij reasonera jeśli chcemy instancje (inferred)
                if (!om.ensureReasoner()) {
                    appendOut("Reasoner nie jest uruchomiony — kliknij 'Uruchom reasoner' aby uzyskać instancje.");
                    try { reasonerLabel.setText("Reasoner: (none)"); } catch (Exception ignore) {}
                    return;
                }
                try { reasonerLabel.setText("Reasoner: " + om.getReasonerName()); } catch (Exception ignore) {}

                List<String> instances = om.getInstancesOfClass(local);
                if (instances == null || instances.isEmpty()) {
                    appendOut("Brak instancji dla klasy " + selected + ".");
                } else {
                    appendOut("Instancje klasy " + selected + ":");
                    for (String s : instances) appendOut(" - " + s);
                }
            } catch (Exception ex) {
                appendOut("Błąd: " + ex.getMessage());
            }
        });

        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);
        panel.add(scroll);

        getContentPane().add(panel);
    }

    private void appendOut(String t) {
        outputArea.append(t + "\n");
    }

    private void onLoad(ActionEvent ev) {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                om.loadOntology(f);
                statusLabel.setText("Załadowano: " + f.getName());
                nsField.setText(om.getNamespace());
                appendOut("Wczytano ontologię: " + f.getAbsolutePath());
                classCombo.removeAllItems();
                for (String c : om.getAllClasses()) {
                    classCombo.addItem(c);
                }
                try {
                    reasonerLabel.setText("Reasoner: " + om.getReasonerName());
                } catch (Exception ignore) {}
            } catch (Exception ex) {
                appendOut("Błąd wczytywania: " + ex.getMessage());
                statusLabel.setText("Błąd ładowania");
            }
        }
    }
}
