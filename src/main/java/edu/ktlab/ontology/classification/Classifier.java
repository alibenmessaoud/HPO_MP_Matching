package edu.ktlab.ontology.classification;

import edu.ktlab.ontology.paring.Pair;

public class Classifier {
	
	public Classifier(){
		this("DEFAULT_MODEL.model");
	}
	
	public Classifier(String modelRes){
		loadModel(modelRes);
	}
	
	private void loadModel(String modelRes){
		// Load model
	}
	
	public double classify(Pair p){
		return 0;
	}
}
