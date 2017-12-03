package sklse.yongfeng.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sklse.yongfeng.data.FilesSearcher;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.KStar;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>Class <b>Overall</b> is used to get 10-fold cross validation result from 3,500 crashes.</p>
 * <p>We use <b>SMOTE</b> strategy combined with other classifiers( including <b>Random forest, C4.5, Bayesnet, SMO, Kstar, SVM</b> ) to train the model. 
 */
public class Overall {
	
	public static double[][] results = new double[60][7];
	
	public static List<String[]> datasets = new ArrayList<>();
	
	/** To save all 6 classifiers' name*/
	private static String[] classifiers = {"C4.5", "RandForest", "BayesNet", "SMO", "KStar", "SVM"};
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("EQ. Results on total dataset (average result of 10 total dataset).");
		System.out.println("-------------------------------------------------");
		System.out.println("1. Experiment setup");
		System.out.println("   Classifiers      : C4.5, Random Forest, Bayes Net, SMO, KStar, SVM\n");
		System.out.println("2. Output format");
		System.out.println("   [classifier] | precision(inTrace) recall(inTrace) fmeasure(inTrace) precision(outTrace) recall(outTrace) fmeasure(outTrace) Accuracy\n");
		System.out.println("3. Time Consumption");
		System.out.println("   It will take about 30 minutes to get the final results.");

		System.out.println("-------------------------------------------------");
		
		/** Get average results from 10 datasets (each of which has 3500 crashes).*/			
		getEvalResultByAve("files/total/");

	}
	
	/***
	 * <p>To get 10-fold cross validation of dataset(arff file) in <b>path</b>.</p>
	 * @param path arff file
	 * @throws Exception
	 */
	public static void getEvalResult(String path, int index) throws Exception{
		
		System.out.println("\nDealing with [ " + path + " ] ...\n");
				
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr - 1);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(ins);
		
		/** classifiers setting*/
		RandomForest rf = new RandomForest();		
		BayesNet bn = new BayesNet();	
		SMO smo = new SMO();	
		KStar ks = new KStar();	
		LibSVM svm = new LibSVM();
		J48 j48 = new J48();
		
		FilteredClassifier fc = new FilteredClassifier();

		Classifier[] cfs = {j48, rf, bn, smo, ks, svm};		
		
		/**No Format*/
		for(int i=0;i<cfs.length;i++){
			
			/** fc is the FilteredClassifier*/
			fc.setClassifier(cfs[i]);
			fc.setFilter(smote);
			
			Evaluation eval = new Evaluation(ins);
			
			eval.crossValidateModel(fc, ins, 10, new Random(1));
			
//			System.out.println("--------------");
//			System.out.printf("%-15s: ", classifiers[i]);
//			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//			System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
			results[index + i][0] = eval.precision(0);
			results[index + i][1] = eval.recall(0);
			results[index + i][2] = eval.fMeasure(0);
			results[index + i][3] = eval.precision(1);
			results[index + i][4] = eval.recall(1);
			results[index + i][5] = eval.fMeasure(1);
			results[index + i][6] = 1-eval.errorRate();
			
		}
		
		/**HTML Format*/
//	    System.out.print("<tr><td rowspan='7'>" + projectNames[0] + "</td>");
//		for(int i=0;i<cfs.length;i++){
//			
//		cfs[i].buildClassifier(ins);
//			fc.setClassifier(cfs[i]);
//			fc.setFilter(smote);
//			
//			String clfName = cfs[i].getClass().getSimpleName();
//			
//			Evaluation eval = new Evaluation(ins);
//			
//			eval.crossValidateModel(fc, ins, 10, new Random(1));
//	
//			System.out.print("<tr><td>" + clfName);
//			System.out.printf("</td><td>%4.3f</td><td>%4.3f</td><td>%4.3f",eval.precision(0), eval.recall(0), eval.fMeasure(0));
//			System.out.printf("</td><td>%4.3f</td><td>%4.3f</td><td>%4.3f",eval.precision(1), eval.recall(1), eval.fMeasure(1));
//			System.out.printf("</td><td>%4.3f</td></tr>\n", (1-eval.errorRate()));
//		}
		
		/**Latex format*/
//		System.out.print("\\hline\\hline\n\\multirow{5}{*}{\\rotatebox{-90}{" + projectNames[0] + "$^{\\ddag}$}} ");
//		for(int i=0;i<cfs.length;i++){
//			
//		cfs[i].buildClassifier(ins);
//			fc.setClassifier(cfs[i]);
//			fc.setFilter(smote);
//			
//			String clfName = cfs[i].getClass().getSimpleName();
//			
//			Evaluation eval = new Evaluation(ins);
//			
//			eval.crossValidateModel(fc, ins, 10, new Random(1));
//			
//			// print in LaTex format
//			System.out.print("& " + clfName);
//			System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//			System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//			System.out.printf(" & %4.3f \\\\\n", (1-eval.errorRate()));
//		}
//		System.out.println("");
		
	}
	
	/***
	 * <p>To get average results of 10 datasets using 6 classifers</p>
	 * <p>Specifically, we first generate 10 matrices (6*7) to record the 10-fold cross validation results.
	 *  Then construct one total matrix (6*10*7) by combining above 10 matrices.</p>
	 * @param folder
	 * @throws Exception
	 */
	public static void getEvalResultByAve(String folder) throws Exception{
		String[] paths = FilesSearcher.search(folder, "total"); // Search for the total3500XXX.arff in files/total/
		
		for(int i=0; i<paths.length; i++){ // for each dataset, get evaluation results
			getEvalResult(paths[i], 6*i);
		}
		
		for(int j=0; j<6; j++){
			double p0 = 0.0d, 
					   p1 = 0.0d, 
					   r0 = 0.0d, 
					   r1 = 0.0d,
					   f0 = 0.0d,
					   f1 = 0.0d,
					   acc = 0.0d;
				for(int i=j; i<60; i+=6){	// for each time
					p0 += results[i][0];
					r0 += results[i][1];
					f0 += results[i][2];
					p1 += results[i][3];
					r1 += results[i][4];
					f1 += results[i][5];
					acc += results[i][6];
				}
				
				//print the average of 10 times
				System.out.printf("%-15s: %6.3f, %6.3f, %6.3f, %6.3f, %6.3f, %6.3f, %6.3f\n", 
						"[" + classifiers[j] + "]", p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
				
		}
	}

}
