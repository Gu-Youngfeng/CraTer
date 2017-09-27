package sklse.yongfeng.experiments;

import java.util.Random;

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
 * <p>We use <b>SMOTE</b> strategy combined with other classifiers( including <b>Random forest, C4.5, Bayesnet, SMO, Kstar, SVM</b> ) to train the model.</p>
 *
 */
public class Single {
	
	private static String[] projectNames = {"Codec", "Ormlite-core", "JSqlParser", "Collections", "IO", "Jsoup", "Mango"};
	
	private static Instances dataCodec;
	private static Instances dataJsi;
	private static Instances dataJsql;
	private static Instances dataCol;
	private static Instances dataIO;
	private static Instances dataJX;
	private static Instances dataDBC;

	public static void main(String[] args) throws Exception {

		/** load dataset*/
		dataCodec = DataSource.read("files\\codec500.arff");
		dataJsi = DataSource.read("files\\ormlite500.arff");  // attention jsieve -> time -> ormlite-core
		dataJsql = DataSource.read("files\\jsqlparser500.arff");
		dataCol = DataSource.read("files\\collections500.arff");
		dataIO = DataSource.read("files\\io500.arff");
		dataJX = DataSource.read("files\\jsoup500.arff"); // attention jxpath -> jsoup
		dataDBC = DataSource.read("files\\mango500.arff"); // attention dbcp -> mango
		
		/** set class index*/
		dataCodec.setClassIndex(dataCodec.numAttributes()-1);
		dataJsi.setClassIndex(dataJsi.numAttributes()-1);
		dataJsql.setClassIndex(dataJsql.numAttributes()-1);
		dataCol.setClassIndex(dataCol.numAttributes()-1);
		dataIO.setClassIndex(dataIO.numAttributes()-1);
		dataJX.setClassIndex(dataJX.numAttributes()-1);
		dataDBC.setClassIndex(dataDBC.numAttributes()-1);
		
		/** 2. Resampling strategy*/
//		Resample rsa1 = new Resample();
//		Resample rsa2 = new Resample();
//		Resample rsa3 = new Resample();
//		Resample rsa4 = new Resample();
//		Resample rsa5 = new Resample();
//		Resample rsa6 = new Resample();
//		Resample rsa7 = new Resample();
//		
//		rsa1.setInputFormat(dataCodec);
//		rsa2.setInputFormat(dataJsi);
//		rsa3.setInputFormat(dataJsql);
//		rsa4.setInputFormat(dataCol);
//		rsa5.setInputFormat(dataIO);
//		rsa6.setInputFormat(dataJX);
//		rsa7.setInputFormat(dataDBC);
//		
//		Resample[] rsas = {rsa1, rsa2, rsa3, rsa4, rsa5, rsa6, rsa7};

		
		/** 3. SMOTE*/
		SMOTE smote1 = new SMOTE();
		SMOTE smote2 = new SMOTE();
		SMOTE smote3 = new SMOTE();
		SMOTE smote4 = new SMOTE();
		SMOTE smote5 = new SMOTE();
		SMOTE smote6 = new SMOTE();
		SMOTE smote7 = new SMOTE();
		
		smote1.setInputFormat(dataCodec);
		smote2.setInputFormat(dataJsi);
		smote3.setInputFormat(dataJsql);
		smote4.setInputFormat(dataCol);
		smote5.setInputFormat(dataIO);
		smote6.setInputFormat(dataJX);
		smote7.setInputFormat(dataDBC);
		
		SMOTE[] smotes = {smote1, smote2, smote3, smote4, smote5, smote6, smote7};	
		
		/** classifiers setting*/
		RandomForest rf = new RandomForest();
//		rf.setNumIterations(200);
		
		BayesNet bn = new BayesNet();
		
		SMO smo = new SMO();
		
		KStar ks = new KStar();
		
		LibSVM svm = new LibSVM();
		
		J48 j48 = new J48();
		
		//IBk ibk = new IBk();
		
		FilteredClassifier fc = new FilteredClassifier();
		
		Instances[] ins = {dataCodec, dataJsi, dataJsql, dataCol, dataIO, dataJX, dataDBC};
//		Instances[] ins = {dataDBC};
		Classifier[] cfs = {rf, j48, bn, smo, ks, svm};	
//		Classifier[] cfs = {rf, j48};
		
		/**for each projects in ins[], we evaluate it using classifiers in cfs[]*/
		for(int j=0;j<ins.length;j++){
			
			/**Latex format*/
//			System.out.print("\\hline\\hline\n\\multirow{5}{*}{\\rotatebox{-90}{" + projectNames[j] + "$^{\\ddag}$}} ");
//			for(int i=0;i<cfs.length;i++){
//				
////			cfs[i].buildClassifier(ins[j]);
//				fc.setClassifier(cfs[i]);
//				fc.setFilter(smotes[j]);
//				
//				String clfName = cfs[i].getClass().getSimpleName();
//				
//				Evaluation eval = new Evaluation(ins[j]);
//				
//				eval.crossValidateModel(fc, ins[j], 10, new Random(1));
//				
//				// print in LaTex format
//				System.out.print("& " + clfName);
//				System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
//				System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
//				System.out.printf(" & %4.3f \\\\\n", (1-eval.errorRate()));
//			}
//			System.out.println("");
			
			/**HTML format*/
//			System.out.print("<tr><td rowspan='7'>" + projectNames[j] + "</td>");
//			for(int i=0;i<cfs.length;i++){
//				
////			cfs[i].buildClassifier(ins[j]);
//				fc.setClassifier(cfs[i]);
//				fc.setFilter(smotes[j]);
//				
//				String clfName = cfs[i].getClass().getSimpleName();
//				
//				Evaluation eval = new Evaluation(ins[j]);
//				
//				eval.crossValidateModel(fc, ins[j], 10, new Random(1));
//
//				System.out.print("<tr><td>" + clfName);
//				System.out.printf("</td><td>%4.3f</td><td>%4.3f</td><td>%4.3f",eval.precision(0), eval.recall(0), eval.fMeasure(0));
//				System.out.printf("</td><td>%4.3f</td><td>%4.3f</td><td>%4.3f",eval.precision(1), eval.recall(1), eval.fMeasure(1));
//				System.out.printf("</td><td>%4.3f</td></tr>\n", (1-eval.errorRate()));
//			}
			
			/**None format*/
			System.out.println(projectNames[j]);
			for(int i=0;i<cfs.length;i++){
				
				/** fc is the FilteredClassifier*/
				//cfs[i].buildClassifier(ins[j]);
				fc.setClassifier(cfs[i]);
				fc.setFilter(smotes[j]);
				
				String clfName = cfs[i].getClass().getSimpleName();
				
				Evaluation eval = new Evaluation(ins[j]);
				
				eval.crossValidateModel(fc, ins[j], 10, new Random(1));
				
				System.out.printf("%-15s: ", clfName);
				System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
				System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
				System.out.printf(" & %4.3f \\\\\\hline\n\n", (1-eval.errorRate()));
			}
			System.out.println("---------- ---------- ---------- ---------- ---------- ");
			
		}
		
	}

}
