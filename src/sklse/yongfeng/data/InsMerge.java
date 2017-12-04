package sklse.yongfeng.data;

import java.util.ArrayList;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * <p>This class <b>InsMerge</b> is used to merge dataset from each project into the total dataset.</p>
 * <p>Function {@link#getIns(path, dirpath)} will take as input the dataset array <b>path</b>, and output the total arff file, 
 * then save in the <b>dirpath</b> directory.</p>
 * <p><b>* In our experiment, we use this class to combine dataset from each project to generate the total dataset</b></p>
 */
public class InsMerge {

	public static void main(String[] args) throws Exception {
		
		String[] paths = new String[10];
		
		for(int i=1; i<=10; i++){ // Find the dataset with the same index in generated directory
			paths = FilesSearcher.search("files/generated/", i);
			getIns(paths, "files/total/");
		}	
	
	}
	
	/***
	 * <p>To Merge the datasets in path array and save the total dataset in dirpath.
	 * </p>
	 * @param path String array of arff file
	 * @throws Exception
	 */
	public static void getIns(String[] path, String dirpath) throws Exception{
		
		/** Create a empty dataset total*/
		Instances total = new Instances("total3500", getStandAttrs(), 1);
		
		total.setClassIndex(total.numAttributes() - 1);
		
		int len = path.length;
		Instances[] temp = new Instances[len];
		
		for(int i=0; i<path.length; i++){
			
			temp[i] = DataSource.read(path[i]);
			temp[i].setClassIndex(temp[i].numAttributes() - 1);
			
			total.addAll(temp[i]);
			System.out.println("adding " + path[i] + " " + temp[i].numInstances());
//			System.out.println("data" + total.numInstances() + "\n");
		}
		
		String totalName = dirpath+"total3500" + String.valueOf(System.currentTimeMillis()) + ".arff";
		
		DataSink.write(totalName,
				total);
		System.out.println("Writing the data into [" + totalName + "] successfully.\n");
	}
	
	/***
	 * <p>To get standard attribute list from <b>files/empty.arff</b></p>
	 * @return lsAttr
	 */
	public static ArrayList<Attribute> getStandAttrs(){
		ArrayList<Attribute> lsAttr = new ArrayList<>();
		
		try {
			Instances empty = DataSource.read("files/empty.arff");
			int numAttr = empty.numAttributes();
//			empty.setClassIndex(numAttr - 1);
			for(int i=0; i<numAttr; i++){
				lsAttr.add(empty.attribute(i));
			}
		} catch (Exception e) {
			System.out.println("reading empty arff error!");
			e.printStackTrace();
		}
		
		return lsAttr;
	}

}
