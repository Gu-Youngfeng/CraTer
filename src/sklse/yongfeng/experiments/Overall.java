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
 * <p>We use <b>SMOTE</b> strategy combined with other classifiers( including <b>Random forest, C4.5, Bayesnet, SMO, Kstar, SVM</b> ) to train the model. 
 * In {@link#main(String[])}, we provide 2 kinds of methods to conduct the 10-fold cross-validation in 3500 crashes.</p>
 * <h3><b>(i)</b> Directly get results from 1 dataset with 3500 crashes;</h3>
 * <h3><b>(ii)</b> Get Average results from 10 datasets (each of which has 3500 crashes).</h3>
 * @version To be uploaded
 */
public class Overall {
	
	public static double[][] results = new double[60][7];
	
	public static List<String[]> datasets = new ArrayList<>();
	
	/** To save all 6 classifiers' name*/
	private static String[] classifiers = {"C4.5", "RandForest", "BayesNet", "SMO", "KStar", "SVM"};
	
	public static void main(String[] args) throws Exception {
		
		/**(i) Directly get results from 1 dataset with 3500 crashes;*/
		//TODO: You must change the absolute path in search method.
//		getEvalResult("files/new-total3500.arff");
		
		/**(ii) Get Average results from 10 datasets (each of which has 3500 crashes).*/
		//TODO: You must change the absolute path in search method. And if you already have dataset, you don't need to process STEP 1 and 2.
//		for(int i=0; i<10; i++){ // STEP 1. Find the dataset with the same index in generated directory
//			String[] paths = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", (i+1));
//			datasets.add(paths); 
//		}	
//		
//		for(String[] data: datasets){  // STEP 2. Merge the dataset with the same index
//			InsMerge.getIns(data, "D:/Users/LEE/Desktop/New_Data/");
//		}
			
		String[] paths = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "new-total"); // STEP 3. Search for the new-total3500XXX.arff

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
				
				System.out.printf("%-15s: %6.3f, %6.3f, %6.3f, %6.3f, %6.3f, %6.3f, %6.3f\n", 
						classifiers[j], p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
				//print the average of 10 times
		}

	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * @param path arff file
	 * @throws Exception
	 */
	public static void getEvalResult(String path, int index) throws Exception{
				
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
//		Classifier[] cfs = {j48, rf, bn};	
		
		/**No Format*/
		for(int i=0;i<cfs.length;i++){
			
			/** fc is the FilteredClassifier*/
			fc.setClassifier(cfs[i]);
			fc.setFilter(smote);
			
			Evaluation eval = new Evaluation(ins);
			
			eval.crossValidateModel(fc, ins, 10, new Random(1));
			
			System.out.printf("%-15s: ", cfs[i].getClass().getSimpleName());
			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf(" %4.3f %4.3f %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf(" %4.3f \n\n", (1-eval.errorRate()));
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

}
