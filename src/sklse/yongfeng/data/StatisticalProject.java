package sklse.yongfeng.data;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/***
 * <p>This class <b>StatisticalProject</b> is used to statistic the distribution of 500 crashes in each project.</p>
 *
 */
public class StatisticalProject {
	
	private static String[] inssName = {"Codec", "Ormlite-core", "JSqlparser", "Collections", "IO", "Jsoup", "Mango"};
		
	private static Instances dataCodec;
	private static Instances dataJsi;
	private static Instances dataJsql;
	private static Instances dataCol;
	private static Instances dataIO;
	private static Instances dataJX;
	private static Instances dataDBC;

	public static void main(String[] args) throws Exception {

		/** load dataset*/
		dataCodec = DataSource.read("files\\codec500.arff");
		dataJsi = DataSource.read("files\\ormlite500.arff"); // Jsi -> Ormlite
		dataJsql = DataSource.read("files\\jsqlparser500.arff");
		dataCol = DataSource.read("files\\collections500.arff");
		dataIO = DataSource.read("files\\io500.arff");
		dataJX = DataSource.read("files\\jsoup500.arff");
		dataDBC = DataSource.read("files\\mango500.arff");
		
		/** set class index*/
		dataCodec.setClassIndex(dataCodec.numAttributes()-1);
		dataJsi.setClassIndex(dataJsi.numAttributes()-1);
		dataJsql.setClassIndex(dataJsql.numAttributes()-1);
		dataCol.setClassIndex(dataCol.numAttributes()-1);
		dataIO.setClassIndex(dataIO.numAttributes()-1);
		dataJX.setClassIndex(dataJX.numAttributes()-1);
		dataDBC.setClassIndex(dataDBC.numAttributes()-1);
		
		Instances[] inss = {dataCodec, dataJsi, dataJsql, dataCol, dataIO, dataJX, dataDBC};
		
//		System.out.print("<th><td>Project</td></td># InTrace</td><td># OutTrace</td></td># Randomly Selection</td></th>");
		System.out.println("-------  Distribution Information of 500 crashes in each Project  -------\n");
		
		for(int i=0; i<inss.length; i++){
//			System.out.print("<tr><td>" + inssName[i] + "</td>");
			System.out.printf("%-15s ", inssName[i]);
			statistic(inss[i]);
		}

	}

	/**
	 * <p>To get the inTrace and outTrace data in 500 crashes in each project.</p>
	 * @param ins Instances of each project
	 */
	public static void statistic(Instances ins){
		int len = ins.numInstances();
		int intrace = 0;
		int outtrace = 0;
		for(int i=0; i<len; i++){
			if(ins.get(i).stringValue(ins.attribute(ins.classIndex())).equals("InTrace")){
				intrace++;
			}else{
				
				outtrace++;
			}
		}
//		System.out.println("<td>" + intrace + "</td><td>" + outtrace + "</td><td>" + len + "</td></tr>");
		System.out.printf("inTrace:%4d, outTrace:%4d. -> randomly selection:%4d\n", intrace, outtrace, len);
	}
}
