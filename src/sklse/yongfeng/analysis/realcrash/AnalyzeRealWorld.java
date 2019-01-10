package sklse.yongfeng.analysis.realcrash;

import java.util.List;
import sklse.yongfeng.analysis.realcrash.RepsUtilier;

/***
 * <p>This file is only used for analyzing features in Apache Commons Lang project at the moment.
 * The analysis on other real-world projects will be updated soon.
 * </p>
 * @author yongfeng
 *
 */
public class AnalyzeRealWorld {
	
	public static final int[] BUGGY_ID = {20,36,1, 56 ,16,12, 51 ,39, 54 ,13,6,19, 57 , 59 ,44, 58 ,5,45,27,33,43,47,37};

	public static void main(String[] args) throws Exception {
		
		String root = "E://Datasets/Lang/Lang/";
		String path = "resources/crashrep/lang.txt";
		
		List<CrashNode> lsCrash = RepsUtilier.getSingleCrash(path);

		for(int i=0; i<BUGGY_ID.length; i++){

			String proj = root + "lang_" + BUGGY_ID[i] + "_buggy";
			proj = (proj + "/").replace("\\", "/");
//			System.out.println("[subfile]:" + proj);
			RepsUtilier.getFeatures(lsCrash, proj, i);
			RepsUtilier.getLabels(lsCrash, i);

		}
	}

}
