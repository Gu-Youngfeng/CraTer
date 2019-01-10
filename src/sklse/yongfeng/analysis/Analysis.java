package sklse.yongfeng.analysis;

/***
 * 
 * @author yongfeng
 *
 */
public class Analysis {
	/** Total number of mutated projects */
	public static final int PRO_SIZE = 7;

	public static void main(String[] args) {
		
		System.out.println("Static Analysis on {" + PRO_SIZE + "} Projects.");
		System.out.println("-------------------------------------------------");	
		
		for(int i=1; i<=PRO_SIZE; i++){
			projectAnalyzer(i);
		}	
		
	}
	
	public static void projectAnalyzer(int projId){
		String path = "";
		String proj = "";
		
		switch(projId){
		case 1:
			path = "resources/crashrep/codec_mutants.txt";
			proj = "resources/projs/Codec_parent/";
			break;
		case 2:
			path = "resources/crashrep/collection_mutants.txt";
			proj = "resources/projs/Collection_4.1_parent/";
			break;
		case 3:
			path = "resources/crashrep/io_mutants.txt";
			proj = "resources/projs/Commons-io-2.5_parent/";
			break;
		case 4:
			path = "resources/crashrep/jsoup_mutants.txt";
			proj = "resources/projs/jsoup_parent/";
			break;
		case 5:
			path = "resources/crashrep/jsqlpraser_mutants.txt";
			proj = "resources/projs/JSQL_parent/";
			break;
		case 6:
			path = "resources/crashrep/mango_mutants.txt";
			proj = "resources/projs/mango_parent/";
			break;
		case 7:
			path = "resources/crashrep/ormlite_mutants.txt"; 
			proj = "resources/projs/ormlite_parent/";
			break;
		default:
			System.out.println("[ERROR]: No such project id <" + projId + ">");
			break;
		}
		
		System.out.println("[project]: " + path);
		
		// analysis the program and get features
		RepsUtilier.getFeatures(path, proj);
	} 

}
