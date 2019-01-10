package sklse.yongfeng.analysis.realcrash;

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
 * <p>This file is used to conduct evaluation on crashes from real-world project, e.g., Apache Commons Lang project.</p>
 * @author yongfeng
 *
 */
public class EvaluationOnReal {

	public static void main(String[] args) throws Exception {
		
		Instances ins = DataSource.read("files/data/lang.arff"); // read dataset
		ins.setClassIndex(ins.numAttributes()-1);
		
		SMOTE smote = new SMOTE(); // smote
		smote.setInputFormat(ins);
		
		J48 j48 = new J48();
		RandomForest rf = new RandomForest(); // random forest classifier
		BayesNet bn = new BayesNet(); // bayes net classifier
		SMO smo = new SMO(); // smo classifier
		KStar kstar = new KStar(); // kstar classifier
		LibSVM svm = new LibSVM();
//		rf.buildClassifier(ins);
		
		Classifier[] clss = {j48, rf, bn, smo, kstar, svm};
		
		for(Classifier cls: clss){
			FilteredClassifier fc = new FilteredClassifier();
			fc.setClassifier(cls);
			fc.setFilter(smote);
			
			Evaluation eval = new Evaluation(ins);
			eval.crossValidateModel(fc, ins, ins.numInstances(), new Random(0)); // leave-one-out validation
			
			System.out.print(cls.getClass().getSimpleName() + " ");
			System.out.printf("& %5.3f & %5.3f & %5.3f",eval.precision(0), eval.recall(0), eval.fMeasure(0));
			System.out.printf("& %5.3f & %5.3f & %5.3f",eval.precision(1), eval.recall(1), eval.fMeasure(1));
			System.out.printf("& %5.3f", (1-eval.errorRate()));
			System.out.println(" \\\\ \\hline");
		}
	}

}
