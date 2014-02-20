package edu.ktlab.ontology;

import org.semanticweb.owlapi.model.OWLOntology;

public class OwlOntology {
	private OWLOntology ontology;
	
	
	public OwlOntology(){}
	
	public OwlOntology(OWLOntology ontology) {
		super();
		this.ontology = ontology;
	}


	public OWLOntology getOntology() {
		return ontology;
	}

	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}
}
