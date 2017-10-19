package sklse.yongfeng.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sklse.yongfeng.data.FilesSearcher;
import sklse.yongfeng.data.InsMerge;
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
 * <p>We use <b>SMOTE</b> strategy combined with other classifiers( including <b>Random forest, C4.5, Bayesnet, SMO, Kstar, SVM</b> ) to train the model.</p>
 *
 */
public class Overall {
	
	public static double[] results = new double[7];
	
	public static List<String[]> datasets = new ArrayList<>();
	
	public static void main(String[] args) throws Exception {
		
//		/** Find the dataset with the same index*/
//		for(int i=0; i<10; i++){
//			String[] paths = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", (i+1));
//			datasets.add(paths); 
//		}	
//		
//		/** Merge the dataset with the same index*/
//		for(String[] data: datasets){  
//			InsMerge.getIns(data, "D:/Users/LEE/Desktop/New_Data/");
//		}
			
		/** Search for the new-total3500XXX.arff*/
		String[] paths = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "new-total");

		/** Get Result*/
		for(String path: paths){
			getEvalResult(path);
		}

	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * @param path arff file
	 * @throws Exception
	 */
	public static void getEvalResult(String path) throws Exception{
		
//		System.out.println(path);
		
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
			
//			String clfName = cfs[i].getClass().getSimpleName();
			
			Evaluation eval = new Evaluation(ins);
			
			eval.crossValidateModel(fc, ins, 10, new Random(1));
			
			System.out.printf("%-15s: ", cfs[i].toString());
			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
//			results[0] = eval.precision(0);
//			results[1] = eval.recall(0);
//			results[2] = eval.fMeasure(0);
//			results[3] = eval.precision(1);
//			results[4] = eval.recall(1);
//			results[5] = eval.fMeasure(1);
//			results[6] = 1-eval.errorRate();
			
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

}
