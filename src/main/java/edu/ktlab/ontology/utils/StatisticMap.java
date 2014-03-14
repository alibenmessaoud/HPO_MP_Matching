package edu.ktlab.ontology.utils;

import java.util.HashMap;
import java.util.Map;

public class StatisticMap {
	private Map<String, Integer> statistic = new HashMap<String, Integer>();

	public void add(String actualLabel, String predictLabel){
		if(actualLabel.equals("1") && predictLabel.equals("1"))
			increase("TP");
		else if(actualLabel.equals("1") && predictLabel.equals("0"))
			increase("FP");
		else if(actualLabel.equals("0") && predictLabel.equals("1"))
			increase("FN");
		else if(actualLabel.equals("0") && predictLabel.equals("0"))
			increase("TN");
	}

	public int getValue(String type){
		Integer value = statistic.get(type);
		if(value == null) return 0;
		return value;
	}

	private void increase(String type){
		Integer count = statistic.get(type);
		if(count == null) statistic.put(type, 1);
		else statistic.put(type, ++count);
	}

	public double getPrecision(){
		double score = (double) getValue("TP")/(getValue("TP")+getValue("FN"));
		return score;
	}

	public double getRecall(){
		double score = (double) getValue("TP")/(getValue("TP") + getValue("FP"));
		return score;
	}

	public double getF1(){
		double precision = getPrecision();
		double recall = getRecall();
		double f1 = (2*precision*recall)/(precision+recall);
		return f1;
	}
	
	public void report(){
		double precision = getPrecision();
		double recall = getRecall();
		double f1 = getF1();
		
		System.out.println("Precision = " + precision);
		System.out.println("Recall = " + recall);
		System.out.println("F = " + f1);
	}

}
