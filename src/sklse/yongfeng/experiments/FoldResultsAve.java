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
//import weka.filters.unsupervised.attribute.Remove;

/***
 * <p>This class <b>FoldResultsAve</b> calculates the 700 pairs of data, each project has 10 generated dataset,
 *  in which will have 10 folds results, thus we have 7*10*10 = 700 pairs of data.</p>
 *
 */
public class FoldResultsAve {
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("EQ. Top-10 most impact features in each project (average ranking of 10 dataset in one project).");
		System.out.println("-------------------------------------------------");
		System.out.println("1. Experiment setup");
		System.out.println("   Measurement Coefficient    : Pearson's Coefficient Correlation\n");
		System.out.println("2. Output format");
		System.out.println("   [project]: | top-1 | top-2 | ... | top-10 \n");
		System.out.println("3. Time Consumption");
		System.out.println("   It will take about 30 sec to get the final results.");
		System.out.println("-------------------------------------------------\n");
		
		String[] ARFFs = FilesSearcher.search("files/generated/");
//		
		/**10-fold cross validation*/
		System.out.println("----------  700 results without Feature Selection  ----------");
		for(int i=0; i<ARFFs.length; i++){
			for(int k=1; k<2; k++){ // for each project, we conduct 10-fold cross validation for 10 times
				showFolds(ARFFs[i], k); // without feature selection		
			}
		}
		
		System.out.println("\n----------  700 results with Feature Selection  ----------");
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
		
//		//////// Remember to comment it when you do another work!	
		
//		Remove remove = new Remove();
//		if(path.contains("codec")){
//			// Top-10 features for project Apache Commons Codec
//			// 21(CT10), 24(CT13), 25(CT14), 32(CT21), 33(CT22), 34(CT23), 40(AT06), 52(CB02), 56(CB06), 71(CB21), 90(class)
//			remove.setAttributeIndices("1-20, 22-23, 26-31, 35-39, 41-51, 53-55, 57-70, 72-89");
//			remove.setInputFormat(data1);			
//		}else if(path.contains("ormlite")){
//			// Top-10 features for project Ormlite-Core
//			// 18(CT07), 19(CT08), 30(CT19), 32(CT21), 33(CT22), 34(CT23), 40(AT06), 46(AT12), 48(AT14), 50(AT16), 90(class)
//			remove.setAttributeIndices("1-17, 20-29, 31, 35-39, 41-45, 47, 49, 51-89");
//			remove.setInputFormat(data1);	
//		}else if(path.contains("jsqlparser")){
//			// Top-10 features for project Jsieve
//			// 15(CT04), 20(CT09), 21(CT10), 22(CT11), 34(CT23), 37(AT03), 48(AT14), 50(AT16), 82(AB09), 83(AB10), 90(class)
//			remove.setAttributeIndices("1-14, 16-19, 23-33, 35-36, 38-47, 49, 51-81, 84-89");
//			remove.setInputFormat(data1);	
//		}else if(path.contains("collections")){
//			// Top-10 features for project Apache Commons Collections
//			// 1(ST01), 24(CT13), 37(AT03), 40(AT06), 48(AT14), 50(AT16), 63(CB13), 75(AB02), 77(AB04), 79(AB06), 90(class)
//			remove.setAttributeIndices("2-23, 25-36, 38-39, 41-47, 49, 51-62, 64-74, 76, 78, 80-89");
//			remove.setInputFormat(data1);	
//		}else if(path.contains("jsoup")){
//			// Top-10 features for project Jsoup
//			// 7(ST07), 40(AT06), 48(AT14), 49(AT15), 50(AT16), 58(CB08), 60(CB10), 69(CB19), 72(CB22), 73(CB23), 90(class)
//			remove.setAttributeIndices("1-6, 8-39, 41-47, 51-57, 59, 61-68, 70-71, 74-89");
//			remove.setInputFormat(data1);	
//		}else if(path.contains("mango")){
//			// Top-10 features for project Mango
//			// 15(CT04), 16(CT05), 35(AT01), 37(AT03), 48(AT14), 50(AT16), 56(CB06), 63(CB13), 72(CB22), 86(AB13), 90(class)
//			remove.setAttributeIndices("1-14, 17-34, 36, 38-47, 49, 51-55, 57-62, 64-71, 73-85, 87-89");
//			remove.setInputFormat(data1);	
//		}else{ // project io
//			// Top-10 features for project Apache Commons IO
//			// 24(CT13), 38(AT04), 40(AT06), 48(AT14), 50(AT16), 54(CB04), 63(CB13), 72(CB22), 77(AB04), 79(AB06), 90(class)
//			remove.setAttributeIndices("1-23, 25-37, 39, 41-47, 49, 51-53, 55-62, 64-71, 73-76, 78, 80-89");
//			remove.setInputFormat(data1);	
//		}
//		
//		data1 = Filter.useFilter(data1, remove);	
		
		////////
		
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

