package sklse.yongfeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * <p>This class <b>FilesSearcher</b> is used to search .arff files under the given directories.</p>
 * @author LEE
 *
 */
public class FilesSearcher {
	
	public static String[] search(String directory){
		
		
		
		File file = new File(directory);
		File[] files = file.listFiles();
//		System.out.println("# There are following arff files under the : " + file.getAbsolutePath() + "\n");
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff")){
//				System.out.println(files[i].getAbsolutePath());
				lspaths.add(files[i].getAbsolutePath());

			}
		}
		
		String[] paths = new String[lspaths.size()];
		
		
		for(int k=0; k<lspaths.size(); k++){
			paths[k] = lspaths.get(k);
		}
		
		return paths;
	}

}
