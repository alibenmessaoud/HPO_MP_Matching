package edu.ktlab.ontology.matching;

import java.util.List;

import edu.ktlab.ontology.OboOntology;
import edu.ktlab.ontology.classification.Classifier;
import edu.ktlab.ontology.io.OntologyLoader;
import edu.ktlab.ontology.paring.Pair;
import edu.ktlab.ontology.paring.TermParing;

public class TermMatching {
	
	private double threshold = 0.8;
	
	public void matching(String ontologyRes1, String ontologyRes2){
		OboOntology o1 = OntologyLoader.loadOboOntology(ontologyRes1);
		OboOntology o2 = OntologyLoader.loadOboOntology(ontologyRes2);
		
		List<Pair> pairs = TermParing.pairing(o1, o2);
		
		Classifier c = new Classifier();
		
		for(Pair p : pairs){
			if(c.classify(p) > 0.8) System.out.println("Printing the pair");
		}
	}

}
