package edu.ktlab.ontology.classification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import de.bwaldvogel.liblinear.Train;
import edu.ktlab.ontology.classification.analyze.AcronymAnalyzer;
import edu.ktlab.ontology.classification.analyze.CharacterBigramsAnalyzer;
import edu.ktlab.ontology.classification.analyze.CommonTokenAnalyzer;
import edu.ktlab.ontology.classification.analyze.DifferentTokenAnalyzer;
import edu.ktlab.ontology.classification.analyze.SoftTFIDAnalyzer;
import edu.ktlab.ontology.io.PairLoader;
import edu.ktlab.ontology.paring.Pair;
import edu.ktlab.ontology.utils.RandomIntGroupSelection;
import edu.ktlab.ontology.utils.StatisticMap;

public class PhenoOMCrossValidator2 {
	private FeatureVectorGenerator featureVectorGenerator;
	private FeatureSet featureSet;
	private Pair[] trainingPairs;
	private Pair[] testPairs;

	private StatisticMap statistic = new StatisticMap();

	private String directoryPair = "data";
	private String fileTraining = "model/OntologyMatching.training";
	private String fileModel = "model/OntologyMatching.model";
	private String fileWordlist = "model/OntologyMatching.worldlist";

	// Using liblinear
	public void validate(int s, double c, int folds, boolean isBaselineTest) throws Exception{
		featureVectorGenerator = createFeatureVectorGenerator();
		Pair[] loadedPairs = PairLoader.loadDirectory(directoryPair);

		RandomIntGroupSelection randomSelection = 
				new RandomIntGroupSelection(loadedPairs.length, folds);
		randomSelection.populate();

		for(int i = 1 ; i <= folds; i++){
			System.out.println("\n***************** Fold #" + i + " ******************\n");
			int[] testIdxs = randomSelection.getPartAt(i);
			int[] trainIdxs = randomSelection.combinePartsExceptPartAt(i);

			trainingPairs = getPairsByIndex(trainIdxs, loadedPairs);
			testPairs = getPairsByIndex(testIdxs, loadedPairs);

			train(c, s);
			test(isBaselineTest);
		}
		statistic.detailedReport();
	}

	private Pair[] getPairsByIndex(int[] resIdxs, Pair[] loadedPairs){
		Pair[] rPairs = new Pair[resIdxs.length];
		for(int i = 0; i < resIdxs.length; i++)
			rPairs[i] = loadedPairs[resIdxs[i]];
		return rPairs;
	}

	public void train(double c, int s) throws Exception {
		featureSet = FeatureSet.createFeatureSet();
		SoftTFIDFBuilder.getIntance().build(trainingPairs);
		Train.main(new String[] { "-c", Double.toString(c), "-s", Integer.toString(s), 
				fileTraining, fileModel});
	}

	public void test(boolean isBaselineTest) throws Exception{
		Classifier classifier = null;
		if(isBaselineTest){
			classifier = new BaselineClassifier();
			classifier.init(trainingPairs);
		} else classifier = new PhenoOMClassifier();
		
		for(Pair p: testPairs){
			String predictLabel = classifier.classify(p);
			statistic.add(p.getLabel(), predictLabel);
		}
	}

	public FeatureVectorGenerator createFeatureVectorGenerator(){
		return new FeatureVectorGenerator(
				new AcronymAnalyzer(), new CharacterBigramsAnalyzer(),
				new CommonTokenAnalyzer(), new DifferentTokenAnalyzer(),
				new SoftTFIDAnalyzer(SoftTFIDFBuilder.getIntance())
				);
	}

	public void createVectorTrainingFile(Pair[] trainingPairs) throws IOException{
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileTraining)));

		for(Pair p: trainingPairs){
			featureVectorGenerator.analyze(p);
			String vector = featureSet.
					addprintSVMVector(p.getFeatures(), p.getLabel(), false);
			if(vector == null) continue;
			writer.append(vector).append("\n");
		}

		writer.close();

		FileOutputStream fileOut = new FileOutputStream(fileWordlist);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(featureSet);
		out.close();
		fileOut.close();
	}

	public static void main(String[] args) throws Exception{
		new PhenoOMCrossValidator2().validate(2, 1, 10, false);
	}
}
