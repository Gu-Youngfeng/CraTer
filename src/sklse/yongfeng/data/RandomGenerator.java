package sklse.yongfeng.data;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.supervised.instance.SMOTE;

/***
 * <p>class <b>RandomGenerator</b> is used to proportional randomly select 500 instances 
 * from original dataset for 20 times. And we only choose 1 dataset from the 20 dataset.
 * </p>
 * <p>
 * ---------------<br>
 * INPUT >> files/codec.arff<br>
 * OUTPUT >> codec1.arff, codec2.arff, ..., codec10.arff
 * </p>
 * @see#Generator(String, int, int)
 */
public class RandomGenerator {
	
	/** print format object*/
	private static DecimalFormat df = new DecimalFormat("#.###");
	
	/** List to stored accuracies of each sample*/
	private static List<Double> acc = new ArrayList<Double>();

	public static void main(String[] args) throws Exception {
		
		for(int i=1; i<=10; i++){
			Generator("files/ormlite.arff", i, 500); // generate 10 sample according to different random seeds
		}		
		
		System.out.println("\n---------- Accuracy Evaluation ---------\n");
		
		System.out.println(acc);
		
		Sorting(acc);

	}
	
	/**<p>Generate Random sample according to random seed on Desktop, each sample has the same distribution of InTrace/OutTrace
	 *  and have 200 instances.
	 * </p>
	 * @param path original arff file to be sampled in path
	 * @param rand random seed selection
	 * @param num the number of selection
	 * */
	public static void Generator(String path, int rand, int num) throws Exception{
		/*** original dataset reading */
		Instances data = DataSource.read(path);
		data.setClassIndex(data.numAttributes()-1);
		
		/*** randomize the dataset */
		data.randomize(new Random(rand));
		
		/*** instances in InTrace class */
		Instances dataIn = DataSource.read("files\\in.arff");
		dataIn.setClassIndex(data.numAttributes()-1);

		/*** instances in OutTrace class */
		Instances dataOut = DataSource.read("files\\out.arff");
		dataOut.setClassIndex(data.numAttributes()-1);
		
		
		/*** initialize of OutTrace/InTrace instances */
		for(int i=0; i<data.numInstances(); i++){
			if(data.get(i).stringValue(data.get(i).classAttribute()).equals("OutTrace")){
				dataOut.add(data.get(i));
			}
		}
//		DataSink.write("D:\\Users\\LEE\\Desktop\\out1.arff", dataOut);
		
		for(int i=0; i<data.numInstances(); i++){
			if(data.get(i).stringValue(data.get(i).classAttribute()).equals("InTrace")){
				dataIn.add(data.get(i));
			}
		}
//		DataSink.write("D:\\Users\\LEE\\Desktop\\in1.arff", dataIn);
		
//		System.out.println("Instances: " + data.numInstances());
//		System.out.println("InTrace: " + dataIn.numInstances() + ", OutTrace: " + dataOut.numInstances());
		
		/*** ratio in original dataset */
		int inTrace = dataIn.numInstances();
		int outTrace = dataOut.numInstances();
		double ratioI = inTrace*1.0/(outTrace + inTrace);
		
		/*** expected number to select from original dataset*/
		int intrace = (int) (num * ratioI);
		int outtrace = num - intrace;
		
//		System.out.println("ratio: " + ratioI + ":" + ratioII);
//		System.out.println("instances: " + intrace + ":" + outtrace);
		
		Instances train = DataSource.read("files\\r200.arff");
	
		for(int i=0; i<intrace; i++){
			train.add(dataIn.get(i));
		}
		
		for(int j=0; j<outtrace; j++){
			train.add(dataOut.get(j));
		}
		
		/*generated dataset path is filename, on the Desktop**/
		String filename = "D:/Users/LEE/Desktop/New_Data/" + filterName(path) + rand + ".arff";
		DataSink.write(filename, train);
		
		acc.add(getRand(filename));

	}
	
	/**To get your file name from the path.*/
	public static String filterName(String path){
		String name = null;
		int indexXie = path.lastIndexOf("/");
		int indexDian = path.lastIndexOf(".");
		name = path.substring(indexXie+1, indexDian);
		return name;
	}
	
	/**get 10-fold cross-validation result*/
	public static double getRand(String path) throws Exception{
		
		Instances all = DataSource.read(path);
		all.setClassIndex(all.numAttributes()-1);
		
		//Resampling strategy
//		Resample rsa = new Resample();
//		rsa.setInputFormat(all);
//		all = rsa.useFilter(all, rsa);
		
		/**SMOTE*/
		SMOTE smote = new SMOTE();
		smote.setInputFormat(all);
		
		//Classifiers setting
//		RandomForest rf = new RandomForest(); // rf
//		rf.setNumIterations(1000);
		
		J48 rf = new J48();
		
		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(rf);
		fc.setFilter(smote);
		
		double acc = 0.0d;		

		Evaluation eval = new Evaluation(all);
		eval.crossValidateModel(fc, all, 10, new Random(1)); //10-fold cross-validation 
		
		/**Latex format*/
//			System.out.print(cfs[i].getClass().getSimpleName());
//			System.out.print(" & " + df.format(eval.precision(0)) + " & " + df.format(eval.recall(0)) + " & " + df.format(eval.fMeasure(0)));
//			System.out.print(" & " + df.format(eval.precision(1)) + " & " + df.format(eval.recall(1)) + " & " + df.format(eval.fMeasure(1)));
//			System.out.println(" & " + df.format(1-eval.errorRate()) + "\\\\\n\\hline");
		
		/**HTML format*/
//			System.out.print("<tr><td>" + cfs[i].getClass().getSimpleName());
//			System.out.print("</td><td>" + df.format(eval.precision(0)) + "</td><td>" + df.format(eval.recall(0)) + "</td><td>" + df.format(eval.fMeasure(0)));
//			System.out.print("</td><td>" + df.format(eval.precision(1)) + "</td><td>" + df.format(eval.recall(1)) + "</td><td>" + df.format(eval.fMeasure(1)));
//			System.out.println("</td><td>" + df.format(1-eval.errorRate()) + "</td></tr>");
		
		/**None format*/
		System.out.print(fc.getClass().getSimpleName() + ">> ");
		System.out.print(df.format(eval.precision(0)) + ", " + df.format(eval.recall(0)) + ", " + df.format(eval.fMeasure(0)) + ", ");
		System.out.print(df.format(eval.precision(1)) + ", " + df.format(eval.recall(1)) + ", " + df.format(eval.fMeasure(1)) + ", ");
		System.out.println(df.format(1-eval.errorRate()));

		acc = Double.valueOf(df.format(1-eval.errorRate()));
		
		return acc;
		
	}
	
	/**sorting List &lt;double&gt; acc, and reset the acc. */
	public static void Sorting(List<Double> acc){
		int size = acc.size();
		
		double[] accArray = new double[size];
		
		/**List transform to Array*/
		for(int i=0; i<size; i++){
			accArray[i] = acc.get(i);
		}
		
		for(int i=0; i<size-1; i++){
			for(int j=0; j<size-i-1; j++){
				if(accArray[j] > accArray[j+1]){
					double temp;
					temp = accArray[j];
					accArray[j] = accArray[j+1];
					accArray[j+1] = temp;
				}
			}
		}
		
		/**Array transform to List*/
		for(int i=0; i<size; i++){
			acc.set(i, accArray[i]);
		}
		
		/**print the List acc*/
		System.out.println(acc);
		
	}

}
