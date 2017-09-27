package sklse.yongfeng.experiments;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.CostMatrix;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.CostSensitiveClassifier;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>Class <b>ImbalanceProcessing</b> is used to conduct contrast experiments among different imbalanced data processing strategies.</p> 
 * <p>There are 4 strategies: <b>[ No strategy, Cost-sensitive Learning, SMOTE and Resampling ]</b>.</p>
 * <p>* We use the C4.5 as our default classifier.</p>
 *
 */
class ImbalanceProcessing {
	
	/**short-cut name for projects*/
	private static String[] projectNames = {"COD", "ORM", "JSQ", "COL", "IO", "JSO", "MAN"};
	
	private static Instances dataCodec;
	private static Instances dataJsi;
	private static Instances dataJsql;
	private static Instances dataCol;
	private static Instances dataIO;
	private static Instances dataJX;
	private static Instances dataDBC;

	public static void main(String[] args) throws Exception{
		
		/** load dataset*/
		dataCodec = DataSource.read("files\\codec500.arff");
		dataJsi = DataSource.read("files\\ormlite500.arff"); // Jsi -> Ormlite
		dataJsql = DataSource.read("files\\jsqlparser500.arff");
		dataCol = DataSource.read("files\\collections500.arff");
		dataIO = DataSource.read("files\\io500.arff");
		dataJX = DataSource.read("files\\jsoup500.arff");
		dataDBC = DataSource.read("files\\mango500.arff");
		
		/** set class index*/
		dataCodec.setClassIndex(dataCodec.numAttributes()-1);
		dataJsi.setClassIndex(dataJsi.numAttributes()-1);
		dataJsql.setClassIndex(dataJsql.numAttributes()-1);
		dataCol.setClassIndex(dataCol.numAttributes()-1);
		dataIO.setClassIndex(dataIO.numAttributes()-1);
		dataJX.setClassIndex(dataJX.numAttributes()-1);
		dataDBC.setClassIndex(dataDBC.numAttributes()-1);
		
		/** 2. Resampling strategy*/
		Resample rsa1 = new Resample();
		Resample rsa2 = new Resample();
		Resample rsa3 = new Resample();
		Resample rsa4 = new Resample();
		Resample rsa5 = new Resample();
		Resample rsa6 = new Resample();
		Resample rsa7 = new Resample();
		
		rsa1.setInputFormat(dataCodec);
		rsa2.setInputFormat(dataJsi);
		rsa3.setInputFormat(dataJsql);
		rsa4.setInputFormat(dataCol);
		rsa5.setInputFormat(dataIO);
		rsa6.setInputFormat(dataJX);
		rsa7.setInputFormat(dataDBC);
		
		Resample[] rsas = {rsa1, rsa2, rsa3, rsa4, rsa5, rsa6, rsa7};

		
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
		
		/** classifiers C4.5 */
//		RandomForest rf = new RandomForest();
//		rf.setNumIterations(300);
		J48 rf = new J48();
		
		FilteredClassifier fc = new FilteredClassifier();
		
		Instances[] ins = {dataCodec, dataJsi, dataJsql, dataCol, dataIO, dataJX, dataDBC};
//		Instances[] ins = {dataDBC};
//		Classifier[] cfs = {rf, bn, smo, ks, svm};	
		
		/**for each projects in ins[], we evaluate it using classifiers in cfs[]*/
			
		showNo(ins, rf);
		
		showCostsensitive(ins, rf);
//		
		showSMOTE(ins, rf, fc, smotes);
//		
		showResampling(ins, rf, fc, rsas);

	}
	
	/***
	 * <p>to show 10-fold cross validation result using <b>No strategy</b></p>
	 * @param ins instances array
	 * @param rf classifier
	 * @throws Exception
	 */
	public static void showNo(Instances[] ins, Classifier rf) throws Exception{
		
		System.out.println("##  Evaluation Results Using No strategy");
		for(int j=0;j<ins.length;j++){
			
			Evaluation eval = new Evaluation(ins[j]);
			
			eval.crossValidateModel(rf, ins[j], 10, new Random(1));
			
			System.out.printf("%-5s : ", projectNames[j]);
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	/***
	 * <p>to show 10-fold cross validation result using <b>Cost-sensitive Learning strategy</b></p>
	 * @param ins instances array
	 * @param rf classifier
	 * @throws Exception
	 */
	public static void showCostsensitive(Instances[] ins, Classifier rf) throws Exception{
		
		System.out.println("##  Evaluation Results Using Cost-sensitive Learning");
		for(int j=0;j<ins.length;j++){
			
			System.out.printf("%-5s : ", projectNames[j]);
			//String clfName = rf.getClass().getSimpleName();
			
			/** csc is the CostSensitiveClassifier*/
			CostSensitiveClassifier csc = new CostSensitiveClassifier();
			csc.setClassifier(rf);
			csc.setCostMatrix(new CostMatrix(new BufferedReader(new FileReader("files/costm"))));
			
			Evaluation eval = new Evaluation(ins[j]);
			
			eval.crossValidateModel(csc, ins[j], 10, new Random(1));
			
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	
	/***
	 * <p>to show 10-fold cross validation result using <b>SMOTE strategy</b></p>
	 * @param ins instances array
	 * @param rf classifier
	 * @throws Exception
	 */
	public static void showSMOTE(Instances[] ins, Classifier rf, FilteredClassifier fc, Filter[] filters) throws Exception{
		
		System.out.println("##  Evaluation Results Using SMOTE strategy");
		for(int j=0;j<ins.length;j++){
			
			System.out.printf("%-5s : ", projectNames[j]);
			
			/** fc is the FilteredClassifier*/
			fc.setFilter(filters[j]);
			fc.setClassifier(rf);		
			
			//String clfName = rf.getClass().getSimpleName();
			
			Evaluation eval = new Evaluation(ins[j]);
			
			eval.crossValidateModel(fc, ins[j], 10, new Random(1));
			
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	/***
	 * <p>to show 10-fold cross validation result using <b>Resampling strategy</b></p>
	 * @param ins instances array
	 * @param rf classifier
	 * @throws Exception
	 */
	public static void showResampling(Instances[] ins, Classifier rf, FilteredClassifier fc, Filter[] filters) throws Exception{
		
		System.out.println("##  Evaluation Results Using Resampling strategy");
		for(int j=0;j<ins.length;j++){
			
			System.out.printf("%-5s : ", projectNames[j]);
			
			/** fc is the FilteredClassifier*/
			fc.setClassifier(rf);
			fc.setFilter(filters[j]);
			
			//String clfName = rf.getClass().getSimpleName();
			
			Evaluation eval = new Evaluation(ins[j]);
			
			eval.crossValidateModel(fc, ins[j], 10, new Random(1));
			
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
}
