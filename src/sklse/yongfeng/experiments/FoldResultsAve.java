package sklse.yongfeng.experiments;

import java.util.Random;

import sklse.yongfeng.data.FilesSearcher;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>This class <b>FoldResultsAve</b> calculates the 700 pairs of data, each project has 10 generated dataset,
 *  in which will have 10 folds results, thus we have 7*10*10 = 700 pairs of data.</p>
 *
 */
public class FoldResultsAve {
	
	public static void main(String[] args) throws Exception {
		
		String[] ARFFs = FilesSearcher.search("D:\\Users\\LEE\\Desktop\\New_Data");
//		
		/**10-fold cross validation*/
//		System.out.println("----------  700 results without Feature Selection  ----------");
//		for(int i=0; i<ARFFs.length; i++){
//			for(int k=1; k<2; k++){ // for each project, we conduct 10-fold cross validation for 10 times
//				showFolds(ARFFs[i], k); // without feature selection		
//			}
//		}
		
//		System.out.println("\n----------  700 results with Feature Selection  ----------");
		/**10-fold cross validation*/
		for(int i=0; i<ARFFs.length; i++){
			for(int k=1; k<2; k++){ // for each project, we conduct 10-fold cross validation for 10 times
				showFolds(ARFFs[i], k, 1); // with feature selection		
			}
		}

	}

	/***
	 * <p>Do not use Feature Selection method to get 10 folds results in the given project</p>
	 * @param path project path
	 * @throws Exception 
	 */
	public static void showFolds(String path, int k) throws Exception{
		
		Instances data1 = DataSource.read(path);
		data1.setClassIndex(data1.numAttributes()-1);
		if(!data1.classAttribute().isNominal()) // in case of noisy data, return
			return;

		/** Randomize and stratify the dataset*/
		data1.randomize(new Random(k)); 	
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
	public static void showFolds(String path, int k, int flag) throws Exception{
			
		Instances data1 = DataSource.read(path);
		data1.setClassIndex(data1.numAttributes()-1);
		if(!data1.classAttribute().isNominal()) // in case of noisy data, return
			return;
		
		/** Feature Selection: Correlation */
		CorrelationAttributeEval evall = new CorrelationAttributeEval();
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

