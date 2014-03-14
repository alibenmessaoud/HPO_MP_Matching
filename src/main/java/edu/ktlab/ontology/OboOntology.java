package edu.ktlab.ontology;

import org.obolibrary.oboformat.model.Frame;
import org.obolibrary.oboformat.model.OBODoc;


public class OboOntology {
	private OBODoc ontology;

	public OboOntology(){}
	
	public OboOntology(OBODoc ontology) {
		super();
		this.ontology = ontology;
	}

	public OBODoc getOntology() { return ontology; }
	public void setOntology(OBODoc ontology) { this.ontology = ontology; }
	
	public String getTermName(String id){
		Frame term = ontology.getTermFrame(id);
		return term.getTagValue("name", String.class);
	}
}