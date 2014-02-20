package edu.ktlab.ontology.classification;

public class Feature {
	private int id;
	private String feature;
	
	public Feature(){}
	
	public Feature(int id, String feature){
		this.id = id;
		this.feature = feature;
	}
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }
	
	public String getFeature() { return feature; }
	public void setFeature(String feature) { this.feature = feature; }
}
