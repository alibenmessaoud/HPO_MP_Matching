package edu.ktlab.ontology.classification;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import de.bwaldvogel.liblinear.FeatureNode;
import de.bwaldvogel.liblinear.Linear;
import de.bwaldvogel.liblinear.Model;
import edu.ktlab.ontology.classification.analyze.AcronymAnalyzer;
import edu.ktlab.ontology.classification.analyze.CharacterBigramsAnalyzer;
import edu.ktlab.ontology.classification.analyze.CommonTokenAnalyzer;
import edu.ktlab.ontology.classification.analyze.DifferentTokenAnalyzer;
import edu.ktlab.ontology.classification.analyze.SoftTFIDAnalyzer;
import edu.ktlab.ontology.io.PairLoader;
import edu.ktlab.ontology.paring.Pair;
import edu.ktlab.ontology.utils.IOUtil;
import edu.ktlab.ontology.utils.StatisticMap;

public class PhenoOMClassifier implements Classifier {
	private FeatureVectorGenerator featureVectorGenerator;
	private FeatureSet featureSet;
	private Model model;

	private String directoryPair = "data";
	private String fileModel = "model/OntologyMatching.model";
	private String fileWordlist = "model/OntologyMatching.worldlist";

	public FeatureVectorGenerator createFeatureVectorGenerator(){
		return new FeatureVectorGenerator(
				new AcronymAnalyzer(), new CharacterBigramsAnalyzer(),
				new CommonTokenAnalyzer(), new DifferentTokenAnalyzer(),
				new SoftTFIDAnalyzer(SoftTFIDFBuilder.getIntance())
				);
	}

	public PhenoOMClassifier() throws Exception {
		loadModel();
		loadDictionary();
	}

	public PhenoOMClassifier(String model, String wordlist) throws Exception {
		this.fileModel = model;
		this.fileWordlist = wordlist;
		loadModel();
		loadDictionary();
	}

	public void init(Pair[] pairs){
		SoftTFIDFBuilder.getIntance().build(pairs);
	}

	void loadDictionary() throws Exception {
		featureSet = IOUtil.readObject(fileWordlist);

		Pair[] trainingPairs = PairLoader.loadDirectory(directoryPair);
		init(trainingPairs);
	}

	void loadModel() throws Exception {
		model = Linear.loadModel(new File(fileModel));
		featureVectorGenerator = createFeatureVectorGenerator();
	}

	public double predict(Pair p){
		featureVectorGenerator.analyze(p);
		Map<Integer, Integer> vector = 
				featureSet.addStringFeatureVector(p.getFeatures(), p.getLabel(), true);
		ArrayList<FeatureNode> vfeatures = new ArrayList<FeatureNode>();
		for (int key : vector.keySet()) {
			if (key == featureSet.getLabelKey())
				continue;
			FeatureNode featurenode = new FeatureNode(key, vector.get(key));
			vfeatures.add(featurenode);
		}
		return Linear.predict(model, vfeatures.toArray(new FeatureNode[vfeatures.size()]));
	}

	public String classify(Pair p){
		double output = predict(p);
		return featureSet.getLabels().get((int) output);
	}

	public static void main(String[] args) throws Exception{
		PhenoOMClassifier classifier = new PhenoOMClassifier();
		Pair[] pairs = PairLoader.loadDirectory("data");

		StatisticMap statistic = new StatisticMap();

		for(Pair p: pairs){
			String predictLabel = classifier.classify(p);
			statistic.add(p.getLabel(), predictLabel);
		}
		statistic.shortReport();
	}
	/*
	 *  SVM
		Precision = 0.9842602308499475
		Recall = 0.7522052927024859
		F = 0.8527272727272728

		LR
		Precision = 0.9238095238095239
		Recall = 0.6222935044105854
		F = 0.7436511739338764

		SoftTFIDF
		Precision = 0.8163481953290871
		Recall = 0.6166800320769847
		F = 0.702603928734582
	 */

}
