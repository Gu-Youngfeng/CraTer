package sklse.yongfeng.launcher;

import sklse.yongfeng.experiments.FeatureRankingAve;
import sklse.yongfeng.experiments.FeatureSelectionAve;
import sklse.yongfeng.experiments.ImbalanceProcessingAve;
import sklse.yongfeng.experiments.Overall;
import sklse.yongfeng.experiments.Single;
import sklse.yongfeng.experiments.TopTenFeatureEvaluation;
import sklse.yongfeng.analysis.Analysis;

/***
 * <p>This is the main launcher of the project.</p>
 * @author yongfeng
 */
public class Launcher {

	public static void main(String[] args){
		
		try{
			if(args != null && args.length == 1){
				switch(args[0]){
				case "-help":
					messageHELP();
					break;
				case "-analysis":
					Analysis.main(null);
					break;
				case "-total":
					Overall.main(null);
					break;
				case "-single":
					Single.main(null);
					break;
				case "-imbalance":
					ImbalanceProcessingAve.main(null);
					break;
				case "-feature":
					FeatureSelectionAve.main(null);
					break;
				case "-rank":
					FeatureRankingAve.main(null);
					break;
				case "-tenEval":
					TopTenFeatureEvaluation.main(null);
					break;
				default:
					System.out.println("[ERROR!] >> WrongCommandError! Try to use command: [java -jar CraTer.jar -help] ");
					break;
				}
				
			}else{
				System.out.println("[ERROR!] >> CommandNotFoundError! Try to use command: [java -jar CraTer.jar -help] ");
			}
		}catch(Exception e){
			System.out.println("[ERROR!] >> DatasetNotFoundError! Please make sure that the -jar is in the root directory of CraTer project.");
		}
	}
	
	public static void messageHELP(){
		System.out.println("\n-------------------------------------------------\n");
		System.out.println(">> PROJECT NAME: CraTer\n");
		System.out.println(">> DESCRIPTION : This project implements the EVALUATION PROCESS in paper [Does the fault reside in stack trace]. ");
		System.out.println("   The latest versions is updated on github [https://github.com/Gu-Youngfeng/CraTer].\n");
		System.out.println(">> USAGE       : -help       Output the help message of CraTer.");
		System.out.println("               : -analysis   Conduct static analysis of 7 projects.");
		System.out.println("               : -total      Evaluate the total datasets combined with 7 projects.");
		System.out.println("               : -single     Evaluate the datasets from each project.");
		System.out.println("               : -imbalance  Evaluate the impact of imbalanced data processing methods on the prediction results.");
		System.out.println("               : -feature    Evaluate the impact of feature selction methods on the prediction results.");
		System.out.println("               : -rank       Rank the top-10 feature list of each project.");
		System.out.println("               : -tenEval    Evaluate each project only using top-10 features");
		System.out.println("\n-------------------------------------------------\n");
	}

}
