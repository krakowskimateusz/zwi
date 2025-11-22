package com.example.owlapp;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OntologyManager {
    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private OWLDataFactory dataFactory;
    private OWLReasoner reasoner;
    private String namespace = "http://example.com/ontology#";

    public OntologyManager() {
        manager = OWLManager.createOWLOntologyManager();
        dataFactory = manager.getOWLDataFactory();
    }

    public void loadOntology(File file) throws OWLOntologyCreationException {
        ontology = manager.loadOntologyFromOntologyDocument(file);
        dataFactory = manager.getOWLDataFactory();
        // try to set namespace based on ontology IRI
        if (ontology.getOntologyID().getOntologyIRI().isPresent()) {
            String iri = ontology.getOntologyID().getOntologyIRI().get().toString();
            if (!iri.endsWith("#") && !iri.endsWith("/")) iri += "#";
            namespace = iri;
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void addIndividual(String localName, String classLocal) {
        OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(IRI.create(namespace + localName));
        OWLClass cls = dataFactory.getOWLClass(IRI.create(namespace + classLocal));
        OWLClassAssertionAxiom ax = dataFactory.getOWLClassAssertionAxiom(cls, ind);
        manager.addAxiom(ontology, ax);
    }

    public void addDataProperty(String subjectLocal, String propLocal, String literal) {
        OWLNamedIndividual subj = dataFactory.getOWLNamedIndividual(IRI.create(namespace + subjectLocal));
        OWLDataProperty prop = dataFactory.getOWLDataProperty(IRI.create(namespace + propLocal));
        OWLLiteral lit = dataFactory.getOWLLiteral(literal);
        OWLDataPropertyAssertionAxiom ax = dataFactory.getOWLDataPropertyAssertionAxiom(prop, subj, lit);
        manager.addAxiom(ontology, ax);
    }

    public void addObjectProperty(String subjectLocal, String propLocal, String objectLocal) {
        OWLNamedIndividual subj = dataFactory.getOWLNamedIndividual(IRI.create(namespace + subjectLocal));
        OWLObjectProperty prop = dataFactory.getOWLObjectProperty(IRI.create(namespace + propLocal));
        OWLNamedIndividual obj = dataFactory.getOWLNamedIndividual(IRI.create(namespace + objectLocal));
        OWLObjectPropertyAssertionAxiom ax = dataFactory.getOWLObjectPropertyAssertionAxiom(prop, subj, obj);
        manager.addAxiom(ontology, ax);
    }

    public void runReasoner() {
        if (ontology == null) return;
        if (reasoner != null) {
            reasoner.dispose();
        }
        try {
            Class<?> hermitClass = Class.forName("org.semanticweb.HermiT.ReasonerFactory");
            Object factory = hermitClass.getDeclaredConstructor().newInstance();
            org.semanticweb.owlapi.reasoner.OWLReasonerFactory rf = (org.semanticweb.owlapi.reasoner.OWLReasonerFactory) factory;
            reasoner = rf.createReasoner(ontology);
        } catch (ClassNotFoundException cnf) {
            try {
                Class<?> elkClass = Class.forName("org.semanticweb.elk.owlapi.ElkReasonerFactory");
                Object elkFactory = elkClass.getDeclaredConstructor().newInstance();
                org.semanticweb.owlapi.reasoner.OWLReasonerFactory rf = (org.semanticweb.owlapi.reasoner.OWLReasonerFactory) elkFactory;
                reasoner = rf.createReasoner(ontology);
            } catch (ClassNotFoundException cnf2) {
                org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory srf = new org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory();
                reasoner = srf.createReasoner(ontology);
            } catch (Exception ex2) {
                throw new RuntimeException("Nie można utworzyć ELK reasonera: " + ex2.getMessage(), ex2);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Nie można utworzyć HermiT reasonera: " + ex.getMessage(), ex);
        }
        
        reasoner.precomputeInferences();
    }

    public String getReasonerName() {
        if (reasoner == null) return "(none)";
        try {
            return reasoner.getReasonerName();
        } catch (Throwable t) {
            return reasoner.getClass().getName();
        }
    }

    public void saveOntology(File file) {
        if (ontology == null) throw new IllegalStateException("Brak załadowanej ontologii");
        try {
            manager.saveOntology(ontology, IRI.create(file.toURI()));
        } catch (OWLOntologyStorageException e) {
            throw new RuntimeException("Błąd zapisu ontologii: " + e.getMessage(), e);
        }
    }

    public List<String> getInstancesOfClass(String classLocal) {
        List<String> result = new ArrayList<>();
        if (ontology == null || reasoner == null) return result;
        OWLClass cls = dataFactory.getOWLClass(IRI.create(namespace + classLocal));
        NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, false);
        for (org.semanticweb.owlapi.reasoner.Node<OWLNamedIndividual> n : instances) {
            for (OWLNamedIndividual ind : n) {
                result.add(ind.getIRI().toString());
            }
        }
        return result;
    }

    public List<String> getAllClasses() {
        List<String> result = new ArrayList<>();
        if (ontology == null) return result;
        for (OWLClass cls : ontology.getClassesInSignature()) {
            result.add(cls.getIRI().toString());
        }
        return result;
    }

    /**
     * Zwraca wnioski: dla każdej instancji listę klas (w formie "instancja -> klasa").
     */
    public List<String> getInferredClassAssertions() {
        List<String> result = new ArrayList<>();
        if (ontology == null || reasoner == null) return result;
        for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
            try {
                NodeSet<OWLClass> types = reasoner.getTypes(ind, false);
                for (org.semanticweb.owlapi.reasoner.Node<OWLClass> n : types) {
                    for (OWLClass cls : n) {
                        result.add(ind.getIRI().toString() + " -> " + cls.getIRI().toString());
                    }
                }
            } catch (Exception ex) {
                // ignoruj pojedyncze błędy dla danej instancji
            }
        }
        return result;
    }

    public Set<String> getAllIndividuals() {
        Set<String> result = new HashSet<>();
        if (ontology == null) return result;
        for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
            result.add(ind.getIRI().toString());
        }
        return result;
    }
}
