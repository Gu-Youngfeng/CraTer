package sklse.yongfeng.data;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * <p>This class <b>StatisticalProject</b> is used to get the information of the distribution
 *  in each dataset.</p>
 *
 * <p><b>* In our experiment, this class is used to calculate the distribution information of InTrace and OutTrace in each dataset.</b></p>
 */
public class StatisticalProject {
	
	/**NOTE: This main function is only used for testing the function getDist(path)*/
	public static void main(String[] args) throws Exception {

		System.out.println("-----  Distribution Information in dataset  -----\n");
		
		String[] path = {"files/data/codec.arff",
				"files/data/ormlite.arff", "files/data/jsqlparser.arff", "files/data/collections.arff",
				"files/data/io.arff", "files/data/jsoup.arff", "files/data/mango.arff"
				,"files/data/lang.arff" // Lang project
				};
		
		for(int i=0; i<path.length; i++){

			getDist(path[i]);
		}

	}

	/**
	 * <p>To get the distribution of inTrace and outTrace instance in given dataset in <b>path</b>.</p>
	 * @param ins Instances of each project
	 * @throws Exception 
	 */
	public static void getDist(String path) throws Exception{
		
		Instances ins = DataSource.read(path);
		int numAttr = ins.numAttributes();
		ins.setClassIndex(numAttr-1);
		
		int numIns = ins.numInstances();
		int intrace = 0;
		int outtrace = 0;
		for(int i=0; i<numIns; i++){
			if(ins.get(i).stringValue(ins.attribute(ins.classIndex())).equals("InTrace")){
				intrace++;
			}else{	
				outtrace++;
			}
		}
		
		System.out.printf("%-30s | inTrace: %-5d | outTrace: %-5d |\n", path, intrace, outtrace);
	}
}
