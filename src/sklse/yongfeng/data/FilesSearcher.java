package sklse.yongfeng.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/***
 * <p>This class <b>FilesSearcher</b> is used to search datasets(<b>arff</b> files) under the given directories. 
 * It provide three methods to return datasets: 
 * <li>{@link#search(directory)}, which return the datasets under the directory.</li>
 * <li>{@link#search(directory, filter)}, which return the datasets under the directory and name startsWith filter.</li>
 * <li>{@link#search(directory, index)}, which return the datasets under the directory and name contains index number.</li>
 * </p>
 */
public class FilesSearcher {
	
	/***
	 * <p>To search all arff files under the directory</p>
	 * @param directory search scope
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
	 * <p>To search filtered arff files under the directory. Note that we ONLY collect 
	 * the files whose file name startsWith specify <b>filter</b> string.</p>
	 * @param directory search scope
	 * @param filter filename must startsWith this string.
	 * @return String[] paths
	 */
	public static String[] search(String directory, String filter){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff") && files[i].getName().startsWith(filter)){
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
	 * <p>To search filtered .arff files under the directory. Note that we ONLY collect 
	 * the files whose file name contains specify <b>index</b> number.</p>
	 * @param directory path
	 * @param index filename must contain this integer
	 * @return String[] paths
	 */
	public static String[] search(String directory, int index){
		
		File file = new File(directory);
		File[] files = file.listFiles();
		
		List<String> lspaths = new ArrayList<String>();
		
		for(int i=0; i<files.length; i++){
			if(files[i].isFile() && files[i].getPath().contains(".arff") && files[i].getName().contains(String.valueOf(index) + ".")){
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
