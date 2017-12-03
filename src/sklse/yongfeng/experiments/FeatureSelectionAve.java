package sklse.yongfeng.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sklse.yongfeng.data.FilesSearcher;
import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.attributeSelection.ReliefFAttributeEval;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>The difference of <b>FeatureSelectionAve</b> and <b>FeatureSelection</b> is that 
 * the former class get the average result from each 10 dataset for one project.</p>
 * @version To be uploaded
 */
public class FeatureSelectionAve {
	
	/** To save all 10 results in one project**/
	private static double[][] results = new double[10][7];
	
	/**short-cut name for projects*/
	private static String[] projectNames = {"codec", "ormlite", "jsqlparser", "collections", "io", "jsoup", "mango"};

	public static void main(String[] args) throws Exception{
		
		System.out.println("EQ. Contrast experiments among using different feature selection methods.");
		System.out.println("-------------------------------------------------");
		System.out.println("1. Experiment setup");
		System.out.println("   Default           : C4.5 + SMOTE");
		System.out.println("   Chi-Square        : C4.5 + SMOTE + Chi-Square");
		System.out.println("   Information Gain  : C4.5 + SMOTE + Information Gain");
		System.out.println("   ReliefF           : C4.5 + SMOTE + ReliefF\n");
		System.out.println("2. Output format");
		System.out.println("   [project] | precision(inTrace) recall(inTrace) fmeasure(inTrace) precision(outTrace) recall(outTrace) fmeasure(outTrace) Accuracy\n");
		System.out.println("3. Time Consumption");
		System.out.println("   It will take about 2 minutes to get the final results.");
		System.out.println("-------------------------------------------------");

		/** add all 7 path arrays into the list dataCollection */
		List<String[]> dataCollection = new ArrayList<>(); 
		
		for(int i=0; i< projectNames.length ;i++){
			String[] paths = FilesSearcher.search("files/generated/", projectNames[i]);
			dataCollection.add(paths);
		}	
		
		/** Using different methods to process imbalanced data. */
		selectDefault(dataCollection);	
		selectChiSquare(dataCollection);
		selectInfoGain(dataCollection);
		selectReliefF(dataCollection);		
		
	}
	
	/***
	 * <p>To print the average result using Default.</p>
	 * @param dataCollection
	 * @throws Exception
	 */
	public static void selectDefault(List<String[]> dataCollection) throws Exception{
		
		System.out.println("\nAverage Results Using ** Default **: \n");
		
		for(int k=0; k<dataCollection.size(); k++){ // for each project
			int index = 0;
			for(String arffs: dataCollection.get(k)){ // each project has 10 arff files
				getEvalResultbyDefault(arffs, index);
//				getEvalResultbyChiSquare(arffs, index);
//				getEvalResultbyInfoGain(arffs, index);
//				getEvalResultbyGainRatio(arffs, index);
//				getEvalResultbyCorrelation(arffs, index);
//				getEvalResultbyReliefF(arffs, index);
				index += 1;
			}
			double p0 = 0.0d, 
					   p1 = 0.0d, 
					   r0 = 0.0d, 
					   r1 = 0.0d,
					   f0 = 0.0d,
					   f1 = 0.0d,
					   acc = 0.0d;
			for(int m=0; m<10; m++){
				p0 += results[m][0];
				r0 += results[m][1];
				f0 += results[m][2];
				p1 += results[m][3];
				r1 += results[m][4];
				f1 += results[m][5];
				acc += results[m][6];
			}
			System.out.printf("%-15s | %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f\n", 
					"[" +projectNames[k] + "]", p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
		}
	}
	
	/***
	 * <p>To print the average result using Chi-Square.</p>
	 * @param dataCollection
	 * @throws Exception
	 */
	public static void selectChiSquare(List<String[]> dataCollection) throws Exception{
		
		System.out.println("\nAverage Results Using ** Chi-Square **: \n");
		
		for(int k=0; k<dataCollection.size(); k++){ // for each project
			int index = 0;
			for(String arffs: dataCollection.get(k)){ // each project has 10 arff files
//				getEvalResultbyDefault(arffs, index);
				getEvalResultbyChiSquare(arffs, index);
//				getEvalResultbyInfoGain(arffs, index);
//				getEvalResultbyGainRatio(arffs, index);
//				getEvalResultbyCorrelation(arffs, index);
//				getEvalResultbyReliefF(arffs, index);
				index += 1;
			}
			double p0 = 0.0d, 
					   p1 = 0.0d, 
					   r0 = 0.0d, 
					   r1 = 0.0d,
					   f0 = 0.0d,
					   f1 = 0.0d,
					   acc = 0.0d;
			for(int m=0; m<10; m++){
				p0 += results[m][0];
				r0 += results[m][1];
				f0 += results[m][2];
				p1 += results[m][3];
				r1 += results[m][4];
				f1 += results[m][5];
				acc += results[m][6];
			}
			System.out.printf("%-15s | %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f\n", 
					"[" +projectNames[k] + "]", p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
		}
	}
	
	/***
	 * <p>To print the average result using Information Gain.</p>
	 * @param dataCollection
	 * @throws Exception
	 */
	public static void selectInfoGain(List<String[]> dataCollection) throws Exception{
		
		System.out.println("\nAverage Results Using ** Information Gain **: \n");
		
		for(int k=0; k<dataCollection.size(); k++){ // for each project
			int index = 0;
			for(String arffs: dataCollection.get(k)){ // each project has 10 arff files
//				getEvalResultbyDefault(arffs, index);
//				getEvalResultbyChiSquare(arffs, index);
				getEvalResultbyInfoGain(arffs, index);
//				getEvalResultbyGainRatio(arffs, index);
//				getEvalResultbyCorrelation(arffs, index);
//				getEvalResultbyReliefF(arffs, index);
				index += 1;
			}
			double p0 = 0.0d, 
					   p1 = 0.0d, 
					   r0 = 0.0d, 
					   r1 = 0.0d,
					   f0 = 0.0d,
					   f1 = 0.0d,
					   acc = 0.0d;
			for(int m=0; m<10; m++){
				p0 += results[m][0];
				r0 += results[m][1];
				f0 += results[m][2];
				p1 += results[m][3];
				r1 += results[m][4];
				f1 += results[m][5];
				acc += results[m][6];
			}
			System.out.printf("%-15s | %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f\n", 
					"[" +projectNames[k] + "]", p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
		}
	}
	
	/***
	 * <p>To print the average result using ReliefF.</p>
	 * @param dataCollection
	 * @throws Exception
	 */
	public static void selectReliefF(List<String[]> dataCollection) throws Exception{
		
		System.out.println("\nAverage Results Using ** ReliefF **: \n");
		
		for(int k=0; k<dataCollection.size(); k++){ // for each project
			int index = 0;
			for(String arffs: dataCollection.get(k)){ // each project has 10 arff files
//				getEvalResultbyDefault(arffs, index);
//				getEvalResultbyChiSquare(arffs, index);
//				getEvalResultbyInfoGain(arffs, index);
//				getEvalResultbyGainRatio(arffs, index);
//				getEvalResultbyCorrelation(arffs, index);
				getEvalResultbyReliefF(arffs, index);
				index += 1;
			}
			double p0 = 0.0d, 
					   p1 = 0.0d, 
					   r0 = 0.0d, 
					   r1 = 0.0d,
					   f0 = 0.0d,
					   f1 = 0.0d,
					   acc = 0.0d;
			for(int m=0; m<10; m++){
				p0 += results[m][0];
				r0 += results[m][1];
				f0 += results[m][2];
				p1 += results[m][3];
				r1 += results[m][4];
				f1 += results[m][5];
				acc += results[m][6];
			}
			System.out.printf("%-15s | %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f %6.3f\n", 
					"[" +projectNames[k] + "]", p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
		}
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyDefault(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b>, combined with <b>Chi-Square</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyChiSquare(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		/**chi-squared filter to process the whole dataset first*/
		ChiSquaredAttributeEval evall = new ChiSquaredAttributeEval();	
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();
		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(ins);
		ins = Filter.useFilter(ins, selector);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b>, combined with <b>Information Gain</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyInfoGain(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		/**information gain filter to process the whole dataset first*/
		InfoGainAttributeEval evall = new InfoGainAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();
		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(ins);
		ins = Filter.useFilter(ins, selector);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b>, combined with <b>Information Gain Ratio</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyGainRatio(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		/**information gain ratio filter to process the whole dataset first*/
		GainRatioAttributeEval evall = new GainRatioAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();
		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(ins);
		ins = Filter.useFilter(ins, selector);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b>, combined with <b>Correlation</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyCorrelation(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		/** correlation filter to process the whole dataset first*/
		CorrelationAttributeEval evall = new CorrelationAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();
		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(ins);
		ins = Filter.useFilter(ins, selector);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * <p>Use C4.5 and <b>SMOTE</b>, combined with <b>ReliefF</b> to classify the dataset.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultbyReliefF(String path, int index) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		/** correlation filter to process the whole dataset first*/
		ReliefFAttributeEval evall = new ReliefFAttributeEval();
		Ranker ranker = new Ranker();
		AttributeSelection selector = new AttributeSelection();
		
		selector.setEvaluator(evall);
		selector.setSearch(ranker);
		selector.setInputFormat(ins);
		ins = Filter.useFilter(ins, selector);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		J48 j48 = new J48();
//		j48.setConfidenceFactor(0.4f);
		j48.buildClassifier(ins);

		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(j48);
		fc.setFilter(smote);
			
		Evaluation eval = new Evaluation(ins);	
		eval.crossValidateModel(fc, ins, 10, new Random(1));
		
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//		System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//		System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
		results[index][0] = eval.precision(0);
		results[index][1] = eval.recall(0);
		results[index][2] = eval.fMeasure(0);
		results[index][3] = eval.precision(1);
		results[index][4] = eval.recall(1);
		results[index][5] = eval.fMeasure(1);
		results[index][6] = 1-eval.errorRate();
				
	}
	
}