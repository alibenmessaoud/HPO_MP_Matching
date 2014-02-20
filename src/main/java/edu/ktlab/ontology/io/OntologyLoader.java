package edu.ktlab.ontology.io;

import java.io.File;

import org.obolibrary.oboformat.model.OBODoc;
import org.obolibrary.oboformat.parser.OBOFormatParser;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import edu.ktlab.ontology.OboOntology;
import edu.ktlab.ontology.OwlOntology;


public class OntologyLoader {

	public static OboOntology loadOboOntology(String filename){
		try {
			OBOFormatParser parser = new OBOFormatParser();
			OBODoc doc = parser.parse(new File(filename));
			return new OboOntology(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static OwlOntology loadOwlOntology(String filename){
		try {
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(filename));
			return new OwlOntology(ontology);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
