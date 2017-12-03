package sklse.yongfeng.experiments;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sklse.yongfeng.data.FilesSearcher;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.supervised.instance.SMOTE;
import weka.filters.unsupervised.attribute.Remove;

/***
 * <p></p>
 * @version To be uploaded
 */
public class TopTenFeatureEvaluation {
	
	/** To save all 10 results in one project**/
	private static double[][] results = new double[60][7];
	
	private static String[] projectNames = {"codec", "ormlite", "jsqlparser", "collections", "io", "jsoup", "mango"};

	public static void main(String[] args) throws Exception {
		
		System.out.println("EQ. The performance of using only 10 selected feature(10 features are from top-10 ranking list).");
		System.out.println("-------------------------------------------------");
		System.out.println("1. Experiment setup");
		System.out.println("   Classifiers      : C4.5 + SMOTE\n");
		System.out.println("2. Output format");
		System.out.println("   [project] | precision(inTrace) recall(inTrace) fmeasure(inTrace) precision(outTrace) recall(outTrace) fmeasure(outTrace) Accuracy\n");
		System.out.println("3. Time Consumption");
		System.out.println("   It will take about 10 sec to get the final results.");
		System.out.println("-------------------------------------------------\n");
		
		/** Collect all dataset in dataCollection list*/
		//TODO: You must change the absolute path in search method.
		List<String[]> dataCollection = new ArrayList<>(); 
		
		for(int i=0; i< projectNames.length ;i++){
			String[] paths = FilesSearcher.search("files/generated/", projectNames[i]);
			dataCollection.add(paths);
		}	
		
//		System.out.println("<tr><th> Project <th/><th> Features <th/><th> P(Intrace) <th/><th> R(Intrace) <th/><th> F(Intrace) <th/><th></tr>");
//		System.out.println("| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |");
		
		for(int m=0; m<dataCollection.size(); m++){
			int index = 0;
			for(String arffs: dataCollection.get(m)){ // each project has 10 arff files
				getEvalResultByTop10(arffs, index);
				index += 6;
			}
			System.out.printf("%-18s", "[" + projectNames[m] + "]: ");
			for(int j=0; j<1; j++){ // for each classifier
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
				
				System.out.printf("%4.3f  %4.3f  %4.3f  %4.3f  %4.3f  %4.3f  %4.3f \n", 
						p0*1.0/10.0, r0*1.0/10.0, f0*1.0/10.0, p1*1.0/10.0, r1*1.0/10.0, f1*1.0/10.0, acc*1.0/10.0);
				//print the average of 10 times
			}
		}
				
	}
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b>.</p>
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
		J48 j48 = new J48();
		
		FilteredClassifier fc = new FilteredClassifier();

		Classifier[] cfs = {j48};	
		
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
	
	
	
	/***
	 * <p>To get 10-fold cross validation in one single arff in <b>path</b>.
	 *  Note that we only consider the <b>top-10</b> features in each project.</p>
	 * @param path dataset path
	 * @throws Exception
	 */
	public static void getEvalResultByTop10(String path, int index) throws Exception{
		
//		System.out.println(path);
		
		Instances ins = DataSource.read(path);
		Remove remove = new Remove();
		if(path.contains("codec")){
			// Top-10 features for project Apache Commons Codec
			// 21(CT10), 24(CT13), 25(CT14), 32(CT21), 33(CT22), 34(CT23), 40(AT06), 52(CB02), 56(CB06), 71(CB21), 90(class)
			remove.setAttributeIndices("1-20, 22-23, 26-31, 35-39, 41-51, 53-55, 57-70, 72-89");
			remove.setInputFormat(ins);			
		}else if(path.contains("ormlite")){
			// Top-10 features for project Ormlite-Core
			// 18(CT07), 19(CT08), 30(CT19), 32(CT21), 33(CT22), 34(CT23), 40(AT06), 46(AT12), 48(AT14), 50(AT16), 90(class)
			remove.setAttributeIndices("1-17, 20-29, 31, 35-39, 41-45, 47, 49, 51-89");
			remove.setInputFormat(ins);	
		}else if(path.contains("jsqlparser")){
			// Top-10 features for project Jsieve
			// 15(CT04), 20(CT09), 21(CT10), 22(CT11), 34(CT23), 37(AT03), 48(AT14), 50(AT16), 82(AB09), 83(AB10), 90(class)
			remove.setAttributeIndices("1-14, 16-19, 23-33, 35-36, 38-47, 49, 51-81, 84-89");
			remove.setInputFormat(ins);	
		}else if(path.contains("collections")){
			// Top-10 features for project Apache Commons Collections
			// 1(ST01), 24(CT13), 37(AT03), 40(AT06), 48(AT14), 50(AT16), 63(CB13), 75(AB02), 77(AB04), 79(AB06), 90(class)
			remove.setAttributeIndices("2-23, 25-36, 38-39, 41-47, 49, 51-62, 64-74, 76, 78, 80-89");
			remove.setInputFormat(ins);	
		}else if(path.contains("jsoup")){
			// Top-10 features for project Jsoup
			// 7(ST07), 40(AT06), 48(AT14), 49(AT15), 50(AT16), 58(CB08), 60(CB10), 69(CB19), 72(CB22), 73(CB23), 90(class)
			remove.setAttributeIndices("1-6, 8-39, 41-47, 51-57, 59, 61-68, 70-71, 74-89");
			remove.setInputFormat(ins);	
		}else if(path.contains("mango")){
			// Top-10 features for project Mango
			// 15(CT04), 16(CT05), 35(AT01), 37(AT03), 48(AT14), 50(AT16), 56(CB06), 63(CB13), 72(CB22), 86(AB13), 90(class)
			remove.setAttributeIndices("1-14, 17-34, 36, 38-47, 49, 51-55, 57-62, 64-71, 73-85, 87-89");
			remove.setInputFormat(ins);	
		}else{ // project io
			// Top-10 features for project Apache Commons IO
			// 24(CT13), 38(AT04), 40(AT06), 48(AT14), 50(AT16), 54(CB04), 63(CB13), 72(CB22), 77(AB04), 79(AB06), 90(class)
			remove.setAttributeIndices("1-23, 25-37, 39, 41-47, 49, 51-53, 55-62, 64-71, 73-76, 78, 80-89");
			remove.setInputFormat(ins);	
		}
		
		Instances insNew = Filter.useFilter(ins, remove);
		
		int numAttr = insNew.numAttributes();
		insNew.setClassIndex(numAttr - 1);
		
		SMOTE smote = new SMOTE();
		smote.setInputFormat(insNew);
		
		/** classifiers setting*/
		J48 j48 = new J48();
		
		FilteredClassifier fc = new FilteredClassifier();

		Classifier[] cfs = {j48};	
		
		/**No Format*/
		for(int i=0;i<cfs.length;i++){
			
			/** fc is the FilteredClassifier*/
			fc.setClassifier(cfs[i]);
			fc.setFilter(smote);
			
//			String clfName = cfs[i].getClass().getSimpleName();
			
			Evaluation eval = new Evaluation(insNew);
			
			eval.crossValidateModel(fc, insNew, 10, new Random(1));
			
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
