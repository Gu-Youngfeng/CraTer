package sklse.yongfeng.experiments;


import java.util.Random;

import weka.attributeSelection.ChiSquaredAttributeEval;
//import weka.attributeSelection.CorrelationAttributeEval;
//import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>This class <b>FoldResults</b> is used to get results of each fold in 10-fold cross validation.</p>
 * <p>We get one results( including <b>P1, R1, F1, P2, R2, F2, Acc</b>) in each fold, and for <b>7</b> projects,
 *  we can get <b>70</b> results. Further, we use the 70 results to conduct the <b>Wilcoxon Singed-rank Test</b> to explore the difference between using FL methods or not.</p>
 *  
 *  <p>Just comparing the output of methods {@link#showFolds(String)} and {@link#showFolds(String, int)} to </p>
 */
class FoldResults {
	
	public static void main(String[] args) throws Exception {
		
		String[] ARFFs = {"files\\codec500.arff",
				"files\\ormlite500.arff",
				"files\\jsqlparser500.arff",
				"files\\collections500.arff",
				"files\\io500.arff",
				"files\\jsoup500.arff",
				"files\\mango500.arff"};
//		
		/**10-fold cross validation*/
		System.out.println("----------  70 results without Feature Selection  ----------");
		for(int i=0; i<ARFFs.length; i++){
			
			showFolds(ARFFs[i]); // without feature selection		
			
		}
		
		System.out.println("\n----------  70 results with Feature Selection  ----------");
		/**10-fold cross validation*/
		for(int i=0; i<ARFFs.length; i++){
			
			showFolds(ARFFs[i], 0); // with feature selection
			
		}

	}

	/***
	 * <p>Do not use Feature Selection method to get 10 folds results in the given project</p>
	 * @param path project path
	 * @throws Exception 
	 */
	public static void showFolds(String path) throws Exception{
		
		Instances data1 = DataSource.read(path);
		data1.setClassIndex(data1.numAttributes()-1);
		if(!data1.classAttribute().isNominal()) // in case of noisy data, return
			return;

		/** Randomize and stratify the dataset*/
		data1.randomize(new Random(1)); 	
		data1.stratify(10);	// 10 folds
		
		double[][] matrix = new double[10][7];	
		
		for(int i=0; i<10; i++){ // To calculate the results in each fold
			
			Instances test = data1.testCV(10, i);
			Instances train = data1.trainCV(10, i);
			
			/** SMOTE */
			SMOTE smote = new SMOTE();
			smote.setInputFormat(train);
			train = Filter.useFilter(train, smote);

			/** C4.5 */
			J48 rf = new J48();
//			RandomForest rf = new RandomForest();
//			rf.setNumIterations(300);
			rf.buildClassifier(train);
			
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(rf, test); 
					
			matrix[i][6] = 1-eval.errorRate();
			
			matrix[i][0] = eval.precision(0);
			
			matrix[i][1] = eval.recall(0);
			
			matrix[i][2] = eval.fMeasure(0);
			
			matrix[i][3] = eval.precision(1);
			
			matrix[i][4] = eval.recall(1);
			
			matrix[i][5] = eval.fMeasure(1);
			
		}
		
		for(int i=0;i<10;i++){
			for(int j=0;j<7;j++){
				System.out.printf("%15.8f", matrix[i][j]);
			}
			System.out.println("");
		}
	}
	
	/***
	 * <p>Using Feature Selection method to get 10 folds results in the given project</p>
	 * @param path project path
	 * @param sel label means we use the Feature Selection method
	 * @throws Exception 
	 */
	public static void showFolds(String path, int sel) throws Exception{
			
		Instances data1 = DataSource.read(path);
		data1.setClassIndex(data1.numAttributes()-1);
		if(!data1.classAttribute().isNominal()) // in case of noisy data, return
			return;
		
		/** Feature Selection: Chi-square */
		ChiSquaredAttributeEval evall = new ChiSquaredAttributeEval();	// here we can replace the Chi-square with Correlation, Information Gain, or other validation methods
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(data1);
		data1 = Filter.useFilter(data1, selector);

		/** Randomize and stratify the dataset*/
		data1.randomize(new Random(1)); 	
		data1.stratify(10);	// 10 folds
		
		double[][] matrix = new double[10][7];	
		
		for(int i=0; i<10; i++){ // To calculate the results in each fold
			
			Instances test = data1.testCV(10, i);
			Instances train = data1.trainCV(10, i);
			
			/** SMOTE */
			SMOTE smote = new SMOTE();
			smote.setInputFormat(train);
			train = Filter.useFilter(train, smote);

			/** C4.5 */
			J48 rf = new J48();
//			RandomForest rf = new RandomForest();
//			rf.setNumIterations(300);
			rf.buildClassifier(train);
			
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(rf, test); 
					
			matrix[i][6] = 1-eval.errorRate();
			
			matrix[i][0] = eval.precision(0);
			
			matrix[i][1] = eval.recall(0);
			
			matrix[i][2] = eval.fMeasure(0);
			
			matrix[i][3] = eval.precision(1);
			
			matrix[i][4] = eval.recall(1);
			
			matrix[i][5] = eval.fMeasure(1);
			
		}
		
		for(int i=0;i<10;i++){
			for(int j=0;j<7;j++){
				System.out.printf("%15.8f", matrix[i][j]);
			}
			System.out.println("");
		}
	}
}
