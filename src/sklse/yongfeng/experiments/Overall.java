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
 * <p>Class <b>Overall</b> is used to get 10-fold cross validation result from 3,500 crashes.</p>
 * <p>We use <b>SMOTE</b> strategy combined with other classifiers( including <b>Random forest, C4.5, Bayesnet, SMO, Kstar, SVM</b> ) to train the model.</p>
 *
 */
public class Overall {
	
	public static void main(String[] args) throws Exception {
		//dataset loading and class index setting
		Instances all = DataSource.read("files\\new-total3500.arff");
		all.setClassIndex(all.numAttributes()-1);
		
		//Resampling strategy
//		Resample rsa = new Resample();
//		rsa.setInputFormat(all);
//		all = rsa.useFilter(all, rsa);
		
		// smote
		SMOTE smote = new SMOTE();
		smote.setInputFormat(all);
		
		FilteredClassifier fc = new FilteredClassifier();
		
		//Classifiers setting
		RandomForest rf = new RandomForest(); // rf
//		rf.setNumIterations(300);
		
		BayesNet bn = new BayesNet(); //bn
		
		SMO smo = new SMO(); //smo
		
		KStar ks = new KStar(); //kstar
		
		LibSVM svm = new LibSVM(); //svm
		
		J48 j48 = new J48();
		
//		FilteredClassifier fc = new FilteredClassifier();
//		fc.buildClassifier(all);
//		fc.setFilter(rsa);
		
		//classifiers array cfs[]
		Classifier[] cfs = {rf, j48, bn, smo, ks, svm};		
//		Classifier[] cfs = {rf};	
		
		//computing results among different classifiers
		for(int i=0;i<cfs.length;i++){

			fc.setClassifier(cfs[i]);
			fc.setFilter(smote);
			
			Evaluation eval = new Evaluation(all);
			eval.crossValidateModel(fc, all, 10, new Random(1)); //10-fold cross-validation 
			
			/**Latex format*/
			System.out.print(cfs[i].getClass().getSimpleName());
			System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf(" & %4.3f & %4.3f & %4.3f", eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf(" & %4.3f \\\\\\hline\n", (1-eval.errorRate()));
			
			/**HTML format*/
//			System.out.print("<tr><td>" + cfs[i].getClass().getSimpleName());
//			System.out.print("</td><td>" + df.format(eval.precision(0)) + "</td><td>" + df.format(eval.recall(0)) + "</td><td>" + df.format(eval.fMeasure(0)));
//			System.out.print("</td><td>" + df.format(eval.precision(1)) + "</td><td>" + df.format(eval.recall(1)) + "</td><td>" + df.format(eval.fMeasure(1)));
//			System.out.println("</td><td>" + df.format(1-eval.errorRate()) + "</td></tr>");

		}

	}

}
