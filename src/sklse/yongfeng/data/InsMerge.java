package sklse.yongfeng.data;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSink;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * <p>This class <b>InsMerge</b> is used to merge each 500 crashes into a total dataset, which has 3,500 crashes.</p>
 *
 */
public class InsMerge {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String[] path = {"files/codec500.arff",
				"files/ormlite500.arff", "files/jsqlparser500.arff", "files/collections500.arff",
				"files/io500.arff", "files/jsoup500.arff", "files/mango500.arff"};
		getIns(path);
	}
	
	/***
	 * <p>Meraging the Instances in path array <b>path[]</b>
	 * </p>
	 * @param path String array of arff file
	 * @throws Exception
	 */
	public static void getIns(String[] path) throws Exception{
		
		Instances data = DataSource.read("files/total3500.arff");
//		Instances data = null;
		data.setClassIndex(data.numAttributes() - 1);
		
		int len = path.length;
		Instances[] temp = new Instances[len];
		
		for(int i=0; i<path.length; i++){
			
			temp[i] = DataSource.read(path[i]);
			temp[i].setClassIndex(temp[i].numAttributes() - 1);
			
			data.addAll(temp[i]);
			System.out.println("adding " + path[i] + " " + temp[i].numInstances());
			System.out.println("data" + data.numInstances() + "\n");
		}
		
		DataSink.write("files/new-total3500.arff", data);
		System.out.println("Writing the data into files/total3500.arff successfully.");
	}

}
