package sklse.yongfeng.experiments;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import weka.attributeSelection.CorrelationAttributeEval;

/***
 * <p>This class <b>FeatureRanking</b> is used to generate ranking list of top-10 features in each project.</p>
 * <p>We use the <b>Pearson's Correlation</b> to calculate the pearson's score for each feature in project, and then rank them.</p>
*/
public class FeatureRanking {
	
	private static String[][] attrRanks = new String[10][7];

	public static void main(String[] args) throws Exception {
		
		String[] ARFFs = {"files\\codec500.arff",
				"files\\ormlite500.arff",
				"files\\jsqlparser500.arff",
				"files\\collections500.arff",
				"files\\io500.arff",
				"files\\jsoup500.arff",
				"files\\mango500.arff"};
		
		/** Ranking features by Pearson's Correlation*/
		for(int i=0; i<ARFFs.length; i++){
			int index = ARFFs[i].lastIndexOf("/");
			String name = ARFFs[i].substring(index+1);
			System.out.println("--------- " + name + " -----------");
			Instances data1 = DataSource.read(ARFFs[i]);
			showAttributes(data1, i, i);
			System.out.println("");
		}
		
		/** Generating a top-10 ranking list*/
		for(int i=0; i<10; i++){
//			System.out.print("<tr><td>" + (i+1) + "</td>");
			System.out.print(i+1);
			for(int j=0; j<7; j++){
				System.out.print( " & " + attrRanks[i][j]);
//				System.out.print( "<td>" + attrRanks[i][j] + "</td>");
			}
			System.out.println("\\\\\n\\hline");
//			System.out.println("</tr>");
			
		}

	}
	
	/***
	 * <p>To show feature ranking list</p>
	 * @param data1
	 * @param index
	 * @throws Exception
	 */
	public static void showAttributes(Instances data1, int index, int k) throws Exception{
		data1.setClassIndex(data1.numAttributes()-1);
		
		CorrelationAttributeEval cae = new CorrelationAttributeEval();
		cae.buildEvaluator(data1);
		
		List<Feature> sl = new ArrayList<>();
		
		/** get Correlation Score for each feature*/
		for(int i=0; i<data1.numAttributes(); i++){ 
			
			String attributeName = data1.attribute(i).name();
			double correlationValue = cae.evaluateAttribute(i);
			
			Feature fea = new Feature(attributeName, correlationValue);
			sl.add(fea);
			
			Collections.sort(sl, new featureComparator());

		}
		
		for(int j=0; j<10; j++){

			System.out.println(sl.get(j).getName() + ": " + sl.get(j).getScore());
			attrRanks[j][k] = sl.get(j).getName();

		}
		
	}
	
}

/***
 * <p>This class <b>Feature</b> is used to save the name and Pearson's score of each feature.</p>
 */
class Feature{
	private String name;
	private double score;
	
	Feature(String name, double score){
		this.name = name;
		this.score = score;
	}
	
	public String getName(){
		return this.name;
	}
	
	public double getScore(){
		return this.score;
	}

}

/***
 * <p>Constructing feature comparator, whose score is bigger, it is better.</p>
 */
class featureComparator implements Comparator<Feature>{

	@Override
	public int compare(Feature o1, Feature o2) {
		
		if(o1.getScore() < o2.getScore()){ // here in order to sort the object in descending order, we use < operator.
			return 1;
		}else{
			return -1;
		}
	}
}
