package sklse.yongfeng.analysis.realcrash;

import java.io.File;
import java.util.List;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.TypeFilter;

public class SRCAnalyzer {
	/**ST10: Number of Java files in the project */
	private static int javaFiles;
	/**ST11: Number of classes in the project */
	private static int allClasses;
	
	SRCAnalyzer(String proj){
		javaFiles = 0;
		allClasses = 0;
		String projMain = proj + "src/main/java/";
		if(!new File(projMain).exists()){
			projMain = proj + "src/java/";
		}
		getJavaFileUnderDir(projMain);
		getAllClassUnderDir(projMain);
	}
	
	private static void getJavaFileUnderDir(String proj){
		File file = new File(proj);
		if(!file.exists()){
			return;
		}
		
		for(File subfile: file.listFiles()){
			if(subfile.isDirectory()){
				getJavaFileUnderDir(subfile.getAbsolutePath());
			}else if(subfile.isFile() && subfile.getName().endsWith("java")){
				javaFiles ++;
			}else{
				continue;
			}
		}
				
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void getAllClassUnderDir(String proj){
		Launcher launcher = new Launcher();
		launcher.addInputResource(proj);
		launcher.getEnvironment().setAutoImports(false);
		launcher.buildModel();
		CtModel model = launcher.getModel();
		List<CtClass> lsCls = model.getElements(new TypeFilter(CtClass.class));
		allClasses = lsCls.size();
	} 
	
	/**
	 * <p>Extract ST10 - ST11 from the stack trace. <p>
	 * */
	public void showSRCFeatures(){
		System.out.print(javaFiles + "," + allClasses + ",");
	}

}
