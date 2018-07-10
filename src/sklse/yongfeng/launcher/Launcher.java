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
					printHELP();
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
					printHELP();
					break;
				}
				
			}else{
				printLOGO();
			}
		}catch(Exception e){
			System.out.println("[ERROR!] >> DatasetNotFoundError! Please make sure that the -jar is in the root directory of CraTer project.");
		}
	}
	
	public static void printHELP(){
		System.out.println("----------------------------------------------------------------------------------------------------------------");
		System.out.println("    PARAMETERS | [default]   Print the description of CraTer");
		System.out.println("               | -help       Print the parameter list of CraTer.");
		System.out.println("               | -analysis   Conduct static analysis of 7 projects.");
		System.out.println("               | -total      Evaluate the total datasets combined with 7 projects.");
		System.out.println("               | -single     Evaluate the datasets from each project.");
		System.out.println("               | -imbalance  Evaluate the impact of imbalanced data processing methods on the prediction results.");
		System.out.println("               | -feature    Evaluate the impact of feature selction methods on the prediction results.");
		System.out.println("               | -rank       Rank the top-10 feature list of each project.");
		System.out.println("               | -tenEval    Evaluate each project only using top-10 features");
		System.out.println("----------------------------------------------------------------------------------------------------------------");
	}
	
	public static void printLOGO(){
		System.out.println("_________________________________________________________________________________________________________________");
		System.out.println("   00000    000000         00     000000000   000000    000000   ");
		System.out.println("  000       00   00       0000        00      00        00   00  ");
		System.out.println("  00        000000       00  00       00      000000    000000   ");
		System.out.println("  000       00   00     00000000      00      00        00   00  ");
		System.out.println("   00000    00    00   00      00     00      000000    00    00 ");
		System.out.println("_________________________________________________________________________________________________________________");
		System.out.println("      PROJECT | CraTer");
		System.out.println("______________|__________________________________________________________________________________________________");
		System.out.println("  DESCRIPTION | This project is the proptotype proposed in paper [Does the fault reside in a stack trace]. ");
		System.out.println("              | The latest versions is updated on github   [https://github.com/Gu-Youngfeng/CraTer].");
		System.out.println("______________|__________________________________________________________________________________________________");
		System.out.println("   PARAMETERS | [default]   Print the description of CraTer");
		System.out.println("              | -help       Print the parameter list of CraTer.");
		System.out.println("              | -analysis   Conduct static analysis of 7 projects.");
		System.out.println("              | -total      Evaluate the total datasets combined with 7 projects.");
		System.out.println("              | -single     Evaluate the datasets from each project.");
		System.out.println("              | -imbalance  Evaluate the impact of imbalanced data processing methods on the prediction results.");
		System.out.println("              | -feature    Evaluate the impact of feature selction methods on the prediction results.");
		System.out.println("              | -rank       Rank the top-10 feature list of each project.");
		System.out.println("              | -tenEval    Evaluate each project only using top-10 features");
		System.out.println("______________|__________________________________________________________________________________________________");
		System.out.println("");
	}

}
