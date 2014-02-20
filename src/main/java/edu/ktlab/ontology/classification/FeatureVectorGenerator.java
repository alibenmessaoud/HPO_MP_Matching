package edu.ktlab.ontology.classification;

import edu.ktlab.ontology.paring.Pair;

public class FeatureVectorGenerator {
	
	private FeatureAnalyzer[] analyzers;
	
	public FeatureVectorGenerator(FeatureAnalyzer[] analyzers) {
		super();
		this.analyzers = analyzers;
	}

	public String generate(Pair p){
		
		for(FeatureAnalyzer analyzer: analyzers){
			// Analyse features of p
		}
		
		return null;
	}
	
	public FeatureAnalyzer[] getAnalyzers() { return analyzers; }
	public void setAnalyzers(FeatureAnalyzer[] analyzers) { this.analyzers = analyzers; }

}
