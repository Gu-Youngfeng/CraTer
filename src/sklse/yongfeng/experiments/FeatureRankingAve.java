package sklse.yongfeng.experiments;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import sklse.yongfeng.data.FilesSearcher;
import weka.attributeSelection.CorrelationAttributeEval;

/***
 * <p>This class <b>FeatureRankingAve</b> is used to generate ranking list of top-10 features in each project. 
 * We first rank features list in each dataset of one project, then calculate the average ranking number of each feature.</p>
 * <p>We use the <b>Pearson's Correlation</b> to calculate the pearson's score for each feature in project, and then rank them.</p>
*/
public class FeatureRankingAve {
	
	/** Feature ranking list (89 x 10) of 10 dataset, the elements are feature names.*/
	private static String[][] attrRanks = new String[89][10];
	
	private static String[] projectNames = {"codec", "ormlite", "jsqlparser", "collections", "io", "jsoup", "mango"};
	/** To save (Feature, Rank) pair*/
	private static Map<Feature, Integer> mapFeatures = new HashMap<>();
	
	private static Map<Feature, Integer> mapFeaturesTotal = new HashMap<>();

	public static void main(String[] args) throws Exception {
		
		System.out.println("EQ. Top-10 most impact features in each project (average ranking of 10 dataset in one project).");
		System.out.println("-------------------------------------------------");
		System.out.println("1. Experiment setup");
		System.out.println("   Measurement Coefficient    : Pearson's Coefficient Correlation\n");
		System.out.println("2. Output format");
		System.out.println("   [project]: | top-1 | top-2 | ... | top-10 \n");
		System.out.println("3. Time Consumption");
		System.out.println("   It will take about 1 minutes to get the final results.");
		System.out.println("-------------------------------------------------\n");
		
		/**show average ranking list of each project*/
		for(int i=0; i<projectNames.length; i++){
			showAveRanking(projectNames[i]);
			mapFeatures.clear();
			mapFeaturesTotal.clear();
		}

	}
	
	/***
	 * <p>To show the average ranking of each project. 
	 * 
	 * </p>
	 * @throws Exception
	 */
	public static void showAveRanking(String projectName) throws Exception{
		
		for(int k=0; k<1; k++){
			String[] ARFFs = FilesSearcher.search("files/generated/", projectName);
			
			/** Ranking features by Pearson's Correlation*/
			for(int i=0; i<ARFFs.length; i++){

				Instances data1 = DataSource.read(ARFFs[i]);
				showAttributes(data1, i, i);
	//			System.out.println("");
			}
			
			/** Generating a top-10 ranking list*/
			for(int i=0; i<attrRanks[0].length; i++){
				int index = ARFFs[i].lastIndexOf("\\");
				String name = ARFFs[i].substring(index+1);
				System.out.printf("[ %-18s]: ", name);
//				System.out.print(i+1);
				for(int j=0; j<10; j++){
					System.out.print( " | " + attrRanks[j][i]);
	//				System.out.print( "<td>" + attrRanks[i][j] + "</td>");
				}
				System.out.println("");
	//			System.out.println("</tr>");			
			}
			
			System.out.print( "------------------------\n");
//			System.out.println(mapFeatures.size());
			Set<Feature> features = mapFeatures.keySet();
			for(Feature fe: features){
				int rankSum = 0;
				for(Feature feinn: features){
					if(fe.getName().equals(feinn.getName())){
	//					System.out.println(fe.getName() + ":" + mapFeatures.get(fe));
						rankSum += feinn.getRanks();
					}
				}
				mapFeaturesTotal.put(fe, rankSum);
			}
			
			List<Map.Entry<Feature, Integer>> lsF = new ArrayList<Map.Entry<Feature, Integer>>(mapFeaturesTotal.entrySet());
			/** rank feature by total feature ranking number*/
			Collections.sort(lsF, new RankComparator());
					
			System.out.printf("[ %-18s]: ", "AVE of " + projectName);
			Set<String> setFeature = new HashSet<>();
			int count = 0;
			for(Entry<Feature, Integer> entry: lsF){
				if(count >= 10){
					break;
				}
				if(setFeature.contains(entry.getKey().getName())){
					continue;
				}
				setFeature.add(entry.getKey().getName());
				count++;
				System.out.printf(" | " + entry.getKey().getName());

			}
			
			System.out.println("\n");
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
		for(int i=0; i<data1.numAttributes()-1; i++){ 
			
			String attributeName = data1.attribute(i).name();
			double correlationValue = cae.evaluateAttribute(i);
			
			Feature fea = new Feature(attributeName, correlationValue);
			sl.add(fea);
		}
		
		/** rank Feature object in sl */
		Collections.sort(sl, new featureComparator());
		
		for(int j=0; j<sl.size(); j++){
			attrRanks[j][k] = sl.get(j).getName();
			sl.get(j).addRanks((j+1));
//			System.out.println(sl.get(j).getName() + ":" + sl.get(j).getScore());
			mapFeatures.put(sl.get(j), sl.get(j).getRanks());
		}		
		
	}
	
}

/***
 * <p>This class <b>Feature</b> is used to save the name and Pearson's score of each feature.
 * The commonly construct of Feature is <b>(name, score, rank)</b>, rank is tha total ranks in 10 list.</p>
 */
class Feature{
	
	private String name;
	private double score;
	private int ranks = 0;
	
	Feature(String name, double score){
		this.name = name;
		this.score = score;
	}
	
	public void addRanks(int r){
		this.ranks += r;
	}
	
	public int getRanks(){
		return this.ranks;
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

class RankComparator implements Comparator<Map.Entry<Feature, Integer>>{

	@Override
	public int compare(Entry<Feature, Integer> arg0, Entry<Feature, Integer> arg1) {
		int v0 = arg0.getValue();
		int v1 = arg1.getValue();
		if(v0 > v1){
			return 1;
		}else if(v0 < v1){
			return -1;
		}else{
			return 0;
		}

	}
	
}
