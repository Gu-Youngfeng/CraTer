package sklse.yongfeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * <p>This class <b>FilesSearcher</b> is used to search .arff files under the given directories.</p>
 * <p>It provide two methods to return suitable files: {@link#search(String)} and {@link#search(String, String)}</p>
 *
 */
public class FilesSearcher {
	
	/***
	 * <p>To search all .arff files under the directory</p>
	 * @param directory path
	 * @return String[] paths
	 */
	public static String[] search(String directory){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff")){
				lspaths.add(files[i].getAbsolutePath());
			}
		}
		
		String[] paths = new String[lspaths.size()];
		
		
		for(int k=0; k<lspaths.size(); k++){
			paths[k] = lspaths.get(k);
		}
		
		return paths;
	}
	
	/***
	 * <p>To search filtered .arff files under the directory. Note that we ONLY collect the files whose path contains <b>filter</b> string.</p>
	 * @param directory path
	 * @param filter filter string
	 * @return String[] paths
	 */
	public static String[] search(String directory, String filter){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff") && files[i].getPath().contains(filter)){
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
