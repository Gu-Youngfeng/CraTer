package sklse.yongfeng.experiments;

import java.util.Random;

import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.attributeSelection.CorrelationAttributeEval;
import weka.attributeSelection.GainRatioAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>This class <b>FeatureSelection</b> is used to conduct the contrast experiments among different feature selection methods.</p>
 * <p>There are 5 methods: <b>[ default, chi-square, information gain, gain ratio, correlation ]</b></p>
 * <p>We use C4.5 as well as SMOTE to train model.</p>
 * 
 */
public class FeatureSelection {
	
	/**short-cut name for projects*/
//	private static String[] projectNames = {"COD", "ORM", "JSQ", "COL", "IO", "JSO", "MAN"};
	
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
		
//		/** 2. Resampling strategy*/
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
//		/** Resampling array*/
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
		
		/** Instances array*/
		Instances[] ins = {dataCodec, dataJsi, dataJsql, dataCol, dataIO, dataJX, dataDBC};
		
		/** FilteredClassifier */
		FilteredClassifier fc = new FilteredClassifier();
		
		/** Decision Tree C4.5*/
		J48 rf = new J48();
//		RandomForest rf = new RandomForest();
//		rf.setNumIterations(300);
		
		showDefault(ins, smotes, fc, rf);
		
		showChiSquare(ins, smotes, fc, rf);
		
		showInforGain(ins, smotes, fc, rf);
		
		showGainRatio(ins, smotes, fc, rf);
		
		showCorrelation(ins, smotes, fc, rf);
		
	}
	
	public static void showDefault(Instances[] ins, Filter[] filters, FilteredClassifier fc, Classifier classifier) throws Exception{
		
		System.out.println("##  Evaluation Results Using NO feature selection method  ##");
		for(int i=0; i<ins.length; i++){
			
			/**filtered-classifier fc*/
			fc.setClassifier(classifier);
			fc.setFilter(filters[i]);
			
			Evaluation eval = new Evaluation(ins[i]);
			eval.crossValidateModel(fc, ins[i], 10, new Random(1));
			
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	public static void showChiSquare(Instances[] ins, Filter[] filters, FilteredClassifier fc, Classifier classifier) throws Exception{
		
		System.out.println("##  Evaluation Results Using Chi-square  ##");
	
		for(int i=0; i<ins.length; i++){
			
			/**chi-squared filter to process the whole dataset first*/
			ChiSquaredAttributeEval evall = new ChiSquaredAttributeEval();	
			Ranker ranker = new Ranker();
			AttributeSelection selector = new AttributeSelection();
			
			selector.setEvaluator(evall);
			selector.setSearch(ranker);
			selector.setInputFormat(ins[i]);
			ins[i] = Filter.useFilter(ins[i], selector);
			
			/**filtered-classifier fc choose classifier and filter*/
			fc.setClassifier(classifier);
			fc.setFilter(filters[i]);
			
			Evaluation eval = new Evaluation(ins[i]);
			eval.crossValidateModel(fc, ins[i], 10, new Random(1));
			
//			System.out.printf("%-5s : ", projectNames[i]);
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	public static void showInforGain(Instances[] ins, Filter[] filters, FilteredClassifier fc, Classifier classifier) throws Exception{
		
		System.out.println("##  Evaluation Results Using Information Gain  ##");
	
		for(int i=0; i<ins.length; i++){
			
			/**chi-squared filter to process the whole dataset first*/
			InfoGainAttributeEval evall = new InfoGainAttributeEval();	
			Ranker ranker = new Ranker();
			AttributeSelection selector = new AttributeSelection();
			
			selector.setEvaluator(evall);
			selector.setSearch(ranker);
			selector.setInputFormat(ins[i]);
			ins[i] = Filter.useFilter(ins[i], selector);
			
			/**filtered-classifier fc choose classifier and filter*/
			fc.setClassifier(classifier);
			fc.setFilter(filters[i]);
			
			Evaluation eval = new Evaluation(ins[i]);
			eval.crossValidateModel(fc, ins[i], 10, new Random(1));
			
//			System.out.printf("%-5s : ", projectNames[i]);
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	public static void showGainRatio(Instances[] ins, Filter[] filters, FilteredClassifier fc, Classifier classifier) throws Exception{
		
		System.out.println("##  Evaluation Results Using Gain Ratio ##");
	
		for(int i=0; i<ins.length; i++){
			
			/**chi-squared filter to process the whole dataset first*/
			GainRatioAttributeEval evall = new GainRatioAttributeEval();	
			Ranker ranker = new Ranker();
			AttributeSelection selector = new AttributeSelection();
			
			selector.setEvaluator(evall);
			selector.setSearch(ranker);
			selector.setInputFormat(ins[i]);
			ins[i] = Filter.useFilter(ins[i], selector);
			
			/**filtered-classifier fc choose classifier and filter*/
			fc.setClassifier(classifier);
			fc.setFilter(filters[i]);
			
			Evaluation eval = new Evaluation(ins[i]);
			eval.crossValidateModel(fc, ins[i], 10, new Random(1));
			
//			System.out.printf("%-5s : ", projectNames[i]);
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
	public static void showCorrelation(Instances[] ins, Filter[] filters, FilteredClassifier fc, Classifier classifier) throws Exception{
		
		System.out.println("##  Evaluation Results Using Correlation ##");
	
		for(int i=0; i<ins.length; i++){
			
			/**pearson filter to process the whole dataset first*/
			CorrelationAttributeEval evall = new CorrelationAttributeEval();	
			Ranker ranker = new Ranker();
			AttributeSelection selector = new AttributeSelection();
			
			selector.setEvaluator(evall);
			selector.setSearch(ranker);
			selector.setInputFormat(ins[i]);
			ins[i] = Filter.useFilter(ins[i], selector);
			
			/**filtered-classifier fc choose classifier and filter*/
			fc.setClassifier(classifier);
			fc.setFilter(filters[i]);
			
			Evaluation eval = new Evaluation(ins[i]);
			eval.crossValidateModel(fc, ins[i], 10, new Random(1));
			
//			System.out.printf("%-5s : ", projectNames[i]);
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("%4.3f %4.3f %4.3f ", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("%4.3f\n", (1-eval.errorRate()));
		}
		System.out.println("");
	}
	
}
