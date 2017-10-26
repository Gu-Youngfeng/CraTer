package sklse.yongfeng.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import sklse.yongfeng.experiments.Overall;

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
 * <p>Class <b>Single</b> is used to get 10-fold cross validation result of 500 crashes from each project.</p>
 * <p>We use <b>SMOTE</b> strategy combined with other 6 classifiers( including <b>Random forest, J48, 
 * Bayes Net, SMO, KStar, SVM</b> ) to train the model. 
 * In {@link#main(String[])} class we provide 2 kinds of method to conduct 10-fold cross-validation in each single project. </p>
 * <h3><b>(i)</b> Directly get evaluation results from 1 dataset(each project has 1 dataset);</h3> 
 * <h3><b>(ii)</b> Calculate the average evaluation results from 10 datasets(each project has 10 datasets).</h3>
 * @version To be uploaded
 */
public class Single {
	
	/** To save all 10 results in one project**/
	private static double[][] results = new double[60][7];
	
	/** To save all 6 classifiers' name*/
	private static String[] classifiers = {"C4.5", "RandForest", "BayesNet", "SMO", "KStar", "SVM"};

	public static void main(String[] args) throws Exception {
		
		/** (i) Directly get evaluation results from 1 dataset(each project has 1 dataset) */
		//TODO: You must change the absolute path in search method.
//		String[] paths = {"files/codec500.arff",
//				"files/ormlite500.arff", "files/jsqlparser500.arff", "files/collections500.arff",
//				"files/io500.arff", "files/jsoup500.arff", "files/mango500.arff"};
//		for(String path: paths){
//			getEvalResult(path, 0);
//		}
		
		/** (ii) Calculate the average evaluation results from 10 datasets(each project has 10 datasets)*/
		//TODO: You must change the absolute path in search method.
		String[] pathsCode = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "codec");
		String[] pathsORM = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "ormlite");
		String[] pathsJSQ = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "jsqlparser");
		String[] pathsCOL = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "collections");
		String[] pathsIO = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "io");
		String[] pathsJSO = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "jsoup");
		String[] pathsMAN = FilesSearcher.search("D:/Users/LEE/Desktop/New_Data/", "mango");
		
		List<String[]> dataCollection = new ArrayList<>(); // add all 7 path arrays into the list
		dataCollection.add(pathsCode);
		dataCollection.add(pathsORM);
		dataCollection.add(pathsJSQ);
		dataCollection.add(pathsCOL);
		dataCollection.add(pathsIO);
		dataCollection.add(pathsJSO);
		dataCollection.add(pathsMAN);	
		
		for(String[] dataset: dataCollection){ // for each project
			int index = 0;
			for(String arffs: dataset){ // each project has 10 arff files
				getEvalResult(arffs, index);
				index += 6;
			}
			
			for(int j=0; j<6; j++){ // for each classifier
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
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b></p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResult(String path, int index) throws Exception{
		
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

}
