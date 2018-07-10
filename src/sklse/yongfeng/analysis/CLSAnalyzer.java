package sklse.yongfeng.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtComment;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.visitor.filter.TypeFilter;

public class CLSAnalyzer {

	public static void main(String[] args) {

		CLSAnalyzer clsr = new CLSAnalyzer("src/main/resources/projs/JSQL_parent/", "net.sf.jsqlparser.statement.insert.InsertModifierPriority");		
		clsr.showCLSFeatures();
	}
	
	/**meta model of spoon*/
	private CtModel metaModel;
	@SuppressWarnings("rawtypes")
	private CtClass clas;
	@SuppressWarnings("rawtypes")
	public CtClass getCtClass(){
		return clas;
	}
	
	/** CT01/CB01: Number of local variables in the top/bottom class */
	int localVariables;
	/** CT02/CB02: Number of fields in the top/bottom class */
	int fielDs;
	/** CT03/CB03: Number of functions (except constructor functions) in the top/bottom class*/
	int methoDs;
	/** CT04/CB04: Number of imported packages in the top/bottom class */
	int packagEs;
	/** CT05/CB05: Whether the top/bottom class is inherited from others */
	int inheriTs;
	/** CT06/CB06: LoC of comments in the top/bottom class */
	int commenTs;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	CLSAnalyzer(String proj, String clsName){
		
		String OuterName = clsName;
		
		if(clsName.contains("$")){ // inner class
			
			OuterName = clsName.substring(0, clsName.indexOf("$"));
//			System.out.println("[outer class]: " + OuterName + " [inner class]: " + InnerName);
			
			/** Building the meta model */
			String fullClass = proj + "src/main/java/" + OuterName.replaceAll("\\.", "/") + ".java";
			Launcher launcher = new Launcher();
			launcher.addInputResource(fullClass);
			launcher.getEnvironment().setCommentEnabled(true);
			metaModel = launcher.buildModel();
			
			List<CtClass> lscls = metaModel.getElements(new TypeFilter(CtClass.class));
//			for(CtClass cls: lscls) {System.out.println(cls.getSimpleName());}
			for(CtClass cls: lscls){

				if(clsName.equals(cls.getQualifiedName())){
					clas = cls;
//					System.out.println(">> " + cls.getQualifiedName());
					break;
				}
			}
			
			/** extract the features */
			extractFeatures(fullClass);
		}else{ // outer class
			
//			System.out.println("[outer class]: " + OuterName);
			
			/** Building the meta model */
			String fullClass = proj + "src/main/java/" + OuterName.replaceAll("\\.", "/") + ".java";
			Launcher launcher = new Launcher();
			launcher.addInputResource(fullClass);
			launcher.getEnvironment().setCommentEnabled(true);
			metaModel = launcher.buildModel();
			
			List<CtClass> lscls = metaModel.getElements(new TypeFilter(CtClass.class));
			for(CtClass cls: lscls){
				if(OuterName.equals(cls.getQualifiedName())){
					clas = cls;
//					System.out.println(">> " + cls.getQualifiedName());
					break;
				}
			}
			
			/** extract the features */
			extractFeatures(fullClass);
		}
		
		
	}
	
	public void showCLSFeatures(){
		System.out.print(localVariables + "," + fielDs + "," + methoDs + "," + packagEs + "," + inheriTs + "," + commenTs + ",");
	}
	

	public void extractFeatures(String fullpath){
		localVariables = this.getLocalVariables();
		fielDs = this.getFields();
		methoDs = this.getMethods();
		try {
			packagEs = this.getImports(fullpath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		inheriTs = this.getInherited();
		commenTs = this.getComments();
		
	}
	
	/***
	 * <pre>To get extends or implements class of from class</pre>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getInherited(){
		int count = 0;
		List<CtClass> lsCls = clas.getElements(new TypeFilter(CtClass.class));
		
		if(lsCls.size() == 0) return -1;
		
		if(lsCls.get(0).getSuperclass()==null && lsCls.get(0).getSuperInterfaces().size()==0){
			count = 1;
		}else{
			count = 0;
		}	
//		System.out.println("[is inherited]: " + count);
		
		return count;
	}
	
	/***
	 * <pre>To get fields of from class</pre>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getFields(){
		int count=0;
		
		List<CtField> lsFL = clas.getElements(new TypeFilter(CtField.class));
//		System.out.println("[field size]: " + lsFL.size());
//		for(int i=0;i<lsFL.size();i++){
//			System.out.println("[field name]: " + lsFL.get(i).getSimpleName());
//		}
		count = lsFL.size();
		
		return count;
	}
	
	/***
	 * <pre>To get local variables of from class</pre>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLocalVariables(){
		int count = 0;
		
		List<CtLocalVariable> lsLV = clas.getElements(new TypeFilter(CtLocalVariable.class));
//		System.out.println("[local variable size]: " + lsLV.size());
//		for(int i=0;i<lsLV.size();i++){
//			System.out.println("[local variable name]: " + lsLV.get(i).toString());
//		}
		count = lsLV.size();
		
		return count;
	}
	
	/***
	 * <pre>To get methods of from class</pre>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getMethods(){
		int count = 0;
		
		List<CtMethod> lsMD = clas.getElements(new TypeFilter(CtMethod.class));
//		System.out.println("[method size]: " + lsMD.size());
//		for(int i=0;i<lsMD.size();i++){
//			System.out.println("[method name]: " + lsMD.get(i).getSignature());
//		}
		count = lsMD.size();
		
		return count;
	}
	
	/***
	 * <pre>To get comments from class.</pre>
	 * @param path class full path
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public int getComments(String path) throws Exception{
	public int getComments(){
		int count = 0;
		/** WAY-1 */
//		File file = new File(path);
//		if(!file.exists())
//			return 0;
//		BufferedReader fr = new BufferedReader(new FileReader(file));
//		String str = "";
//		while((str = fr.readLine()) != null){
//			if(str.trim().contains("//") || str.trim().startsWith("/*") || str.trim().startsWith("*")){
////				lsComment.add(str);
////				System.out.println(">> " + str);
//				count++;
//			}
//		}
//		fr.close();
		
		/** WAY-2 */
		List<CtComment> lsIP = clas.getElements(new TypeFilter(CtComment.class)); // No Import
//		System.out.println("[comments size]: " + lsIP.size());
		for(int i=0;i<lsIP.size();i++){
			int loc = lsIP.get(i).toString().split("\n").length<=1?1:lsIP.get(i).toString().split("\n").length;
//			System.out.println(lsIP.get(i).toString().split("\n").length);
//			System.out.println("[comments loc]: " + loc);
//			System.out.println(lsIP.get(i).toString());
			count += loc;
		}
		
		return count;
	}
	
	/***
	 * <pre>To get number of "import" statements from class. ATTENTION!</pre>
	 * @param path class full path
	 * @return
	 * @throws Exception
	 */
	public int getImports(String path) throws Exception{	
		int count = 0;
		/** WAY-1 */
		File file = new File(path);
		if(!file.exists())
			return 0;
		BufferedReader fr = new BufferedReader(new FileReader(file));
		String str = "";
		while((str = fr.readLine()) != null){
			if(str.startsWith("import")){
//				System.out.println(">> " + str);
				count++;
			}
		}
		fr.close();
		
		/** WAY-2 */
//		List<CtImport> lsIP = metaModel.getElements(new TypeFilter(CtImport.class)); // No Import
//		System.out.println("[import size]: " + lsIP.size());
//		for(int i=0;i<lsIP.size();i++){
//			System.out.println("[import name]: " + lsIP.get(i).toString());
//		}
//		count = lsIP.size();
		
		return count;	
		
	}

}
