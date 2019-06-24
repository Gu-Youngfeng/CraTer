package sklse.yongfeng.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***
 * 
 * @author yongfeng
 * @date 2019.06.24
 * <p>This file is used to search the java code in the specific directory, and then write to a file.</p>
 *
 */
public class ContextSearcher {
	
	private static List<String> lspaths;

	public static void main(String[] args) {
		
		ContextSearcher cs = new ContextSearcher();
		cs.collectJavaCodes("src/", "E:/result.txt");
			
	}
	
	public void collectJavaCodes(String Directory, String resultFile) {
		
		lspaths = new ArrayList<String>();
		searchJavaFiles(Directory);
		for(String file: lspaths) {
			String fileName = ">> " + file + "\n";
			String fileSpace = "\n\n\n";
			System.out.println(fileName);
			String content = readJavaFile(file);
			writeJavaFileTo(fileName, resultFile);
			boolean flag = writeJavaFileTo(content, resultFile);
			if(flag == false) {
				System.err.println("[ERROR]: failed with " + file);
				break;
			}
			writeJavaFileTo(fileSpace, resultFile);
//			break;
		}
	}
	
	/***
	 * <p>To search all arff files under the directory iteratively.</p>
	 * @param directory search scope
	 * @return String[] paths
	 */
	public void searchJavaFiles(String directory){
		
		File file = new File(directory);
		if(file.isFile()) {
			if(file.getPath().endsWith(".java")) {
				lspaths.add(file.getAbsolutePath());
			}
			return;
		}
		
		File[] dirs = file.listFiles();
		for(File dir:dirs) {
			searchJavaFiles(dir.getAbsolutePath());
		}
		
	}
	
	public String readJavaFile(String path) {
		
		String content = "";
		
		File file = new File(path);
		if(!file.exists()) {
			System.err.println("[ERROR]:" + path + " is not exist.");
			return null; 
		}
		
		try {
			BufferedReader fr = new BufferedReader(new FileReader(file));
			String line = "";
			while((line = fr.readLine())!=null) {
				content += line + "\n";
			}
			fr.close();
		} catch (Exception e) {
			System.err.println("[ERROR]: cannot open " + path);
			e.printStackTrace();
		}
		
		return content;	
	}
	
	public boolean writeJavaFileTo(String content, String path) {
		
		boolean flag = false;
		
		if(content == "" || content == null) {
			return flag;
		}
		
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(content);
			flag = true;
			bw.flush();
			bw.close();
		} catch (IOException e) {
			System.err.println("[ERROR]: cannot write " + path);
			e.printStackTrace();
		}
		
		return flag;		
	}

}
