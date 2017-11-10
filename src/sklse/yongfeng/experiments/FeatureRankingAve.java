package sklse.yongfeng.experiments;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import sklse.yongfeng.data.FilesSearcher;
import weka.attributeSelection.CorrelationAttributeEval;

/***
 * <p>This class <b>FeatureRanking</b> is used to generate ranking list of top-10 features in each project.</p>
 * <p>We use the <b>Pearson's Correlation</b> to calculate the pearson's score for each feature in project, and then rank them.</p>
*/
public class FeatureRankingAve {
	
	private static String[][] attrRanks = new String[89][10];
	
	private static Map<Feature, Integer> mapFeatures = new HashMap<>();
	
	private static Map<Feature, Integer> mapFeaturesTotal = new HashMap<>();

	public static void main(String[] args) throws Exception {
		
		String[] ARFFs = FilesSearcher.search("D:\\Users\\LEE\\Desktop\\New_Data", "mango");
		
		/** Ranking features by Pearson's Correlation*/
		for(int i=0; i<ARFFs.length; i++){
			int index = ARFFs[i].lastIndexOf("/");
			String name = ARFFs[i].substring(index+1);
			System.out.println("--------- " + name + " -----------");
			Instances data1 = DataSource.read(ARFFs[i]);
			showAttributes(data1, i, i);
//			System.out.println("");
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
		
		System.out.print( "----------- MAP -------------\n");
		System.out.println(mapFeatures.size());
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
		
		Collections.sort(lsF, new RankComparator());
				
		for(Map.Entry<Feature, Integer> entry: lsF){
			System.out.println(entry.getKey().getName() + "  " + entry.getValue()*1.0/10);
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
		
		Collections.sort(sl, new featureComparator());
		
//		System.out.println(sl.size());
		
		for(int j=0; j<sl.size(); j++){
			attrRanks[j][k] = sl.get(j).getName();
			sl.get(j).addRanks((j+1));
			System.out.println(sl.get(j).getName() + ":" + sl.get(j).getScore());
			mapFeatures.put(sl.get(j), sl.get(j).getRanks());
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






