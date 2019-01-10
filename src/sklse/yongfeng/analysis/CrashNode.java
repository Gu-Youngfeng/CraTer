package sklse.yongfeng.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * <p><b>CrashNode</b> is a crash node class, which save the basic information from single crash.
 * This information includes {@link#exceptionType}, {@link#loc}, {@link#topClassName}, {@link#topMethodName}, {@link#topMethodLine}, 
 *  {@link#bottomClassName}, {@link#bottomMethodName}, {@link#bottomMethodLine}.
 * </p>
 * @author youngfeng
 * @see#CrashNode
 */
public class CrashNode {
	
	/**exception type*/
	private int exceptionType;
	/**stack trace size*/
	private int loc;
	/**number of classes in the stack trace*/
	private int classNum;
	/**number of methods in the stack trace*/
	private int methodNum;
	/**whether an overloaded method exists in the stack trace*/
	private boolean isOverLoaded;
	/**stack trace contains analysis-available method*/
	public List<String> stackTraces = new ArrayList<String>();
	/**fault index in stack trace: <b>-1</b> indicates a OutTrace crash; <b>0,1,2</b> indicates a InTrace crash*/
	public int InTrace;
	public String MutationLine;
	
	/**top class name*/
	private String topClassName;
	/**top method name*/
	private String topMethodName;
	/**line number in top method*/
	private int topMethodLine;
	
	/**bottom class name*/
	private String bottomClassName;
	/**bottom method name*/
	private String bottomMethodName;
	/**bottom number in top method*/
	private int bottomMethodLine;
	
	private String bottom2ClassName;
	private String bottom2MethodName;
	private int bottom2MethodLine;
	
	/**
	 * <p>to initialize a crash node object from crash, the crash is a single crash in the following format,</p>
	 * <pre>
	 * --- org.apache.commons.codec.XXXTest::testXX
	 * java.lang.XXXException:ABC
	 * 	at org.apache.commons.codec.XX.XXX(XX.java:abc)
	 * 	at org.apache.commons.codec.XXXTest.testXX(XXXTest.java:abc)
	 * </pre>
	 * @param crash lines list of single crash
	 */
	CrashNode(List<String> crash){
		
		stackTraces = getStackTraces(crash);
		InTrace = getMutationLine(crash);
		
		String topLine = getTopLine(crash);
		String bottomLine = getBottomLine(crash);
		String bottom2Line = getBottom2Line(crash);
		
		topClassName = getClassName(topLine);
		topMethodName = getMethodName(topLine);
		topMethodLine = getMethodLine(topLine);
		
		bottomClassName = getClassName(bottomLine);
		bottomMethodName = getMethodName(bottomLine);
		bottomMethodLine = getMethodLine(bottomLine);
		
		bottom2ClassName = getClassName(bottom2Line);
		bottom2MethodName = getMethodName(bottom2Line);
		bottom2MethodLine = getMethodLine(bottom2Line);		
		
		String exceptName = getExceptionName(crash.get(1));
//		System.out.println("exception type: " + exceptName);
		exceptionType = getExceptionType(exceptName);
		loc = crash.size()-3;
		classNum = getClassNum(crash);
		methodNum = getMethodNum(crash);
		isOverLoaded = isOverLoaded(crash);
	}
	
	public List<String> getStackTraces(List<String> crash){
		List<String> avaTrace = new ArrayList<String>();
		
		for(String line: crash){
			if(isMethodLine(line)){
				avaTrace.add(line);
			}
		}
		
		return avaTrace;
	}
	
	/**
	 * <p>to extract class name from the line, we can get <b>java.lang.StringBuilder</b> from the following example,</p>
	 * <pre>at java.lang.StringBuilder.setCharAt(StringBuilder.java:76)</pre>
	 * <p>note that if CLass name contains $, then it will appear in class name either.</p>
	 * @param line frame line in stack trace
	 * @return
	 */
	public static String getClassName(String line){
		String tcn = "";
		
//		System.out.println(line);
		String reg = "(\\tat\\s)(.*)\\.([^\\.]*)\\((.*):(\\d*)\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			tcn = matcher.group(2);
		}else{
			System.out.println("[ERROR]: Mismatched! " + line);
		}
		
//		System.out.println(">> " + tcn);
		
//		if(tcn.contains("$")){ // inner class
//			tcn = tcn.split("\\$")[0];
//			System.out.println(tcn.split("\\$")[0]);
//		}
		
		return tcn;
	}

	/**
	 * <p>to extract method name from the line, we can get <b>setChartAt</b> from the following example,</p>
	 * <pre>at java.lang.StringBuilder.setCharAt(StringBuilder.java:76)</pre>
	 * <p>Note that the &lt;init&gt; will be replaced with the true class name.</p>
	 * @param line frame line in stack trace
	 * @return
	 */
	public static String getMethodName(String line){
		String tmn = "";
		
//		System.out.println(line);
		String reg = "(\\tat\\s)(.*)\\.([^\\.]*)\\((.*):(\\d*)\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			tmn = matcher.group(3);
		}else{
			System.out.println("[ERROR]: Mismatched! " + line);
		}
		
		if(tmn.equals("<init>")){ // constructor
			String cls = getClassName(line);
			String[] clss = cls.split("\\.");
			tmn = clss[clss.length-1];
			
//			int inn = cls.indexOf("\\.");
//			tmn = cls.substring(inn, cls.length());
		}
		
		if(tmn.contains("$")){
			tmn = tmn.substring(tmn.lastIndexOf("$")+1);
		}
		
		return tmn;
	}
	
	/**
	 * <p>to extract method line from the line, we can get <b>76</b> from the following example,</p>
	 * <pre>at java.lang.StringBuilder.setCharAt(StringBuilder.java:76)</pre>
	 * @param line frame line in stack trace
	 * @return
	 */
	public static int getMethodLine(String line){
		String tml = "";
		int loc = -1;
		
		String reg = "(\\tat\\s)(.*)\\.([^\\.]*)\\((.*):(\\d*)\\)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			tml = matcher.group(5);
		}else{
			System.out.println("[ERROR]: Mismatched! " + line);
		}
//		System.out.println(">> " + tml);
		loc = Integer.valueOf(tml); // NumberFormatException
			
		return loc;
	}
	
	/**
	 * <p>To get Exception Type of the line, we can get <b>IllegalArgumentException</b> from the following line,</p>
	 * <pre>java.lang.IllegalArgumentException: Negative initial size: 1024</pre>
	 * @param line second line in stack trace
	 * @return
	 */
	public static String getExceptionName(String line){
		String eName = "";
		
//		if(!line.contains(":")){
//			System.out.println("[ERROR]: No Exception Pattern in: " + line);
//		}
		line = line.split(":")[0];
		
		String reg = "(.*)\\.([^\\.]*)";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(line);
		if(matcher.find()){
			eName = matcher.group(2);
		}
		
		return eName;
	}
	
	public static int getExceptionType(String ExType){
		int num = -1;
		switch(ExType){
			case "CloneNotSupportedException":
				num = 1;
				break;
			case "InterruptedException":
				num = 2;
				break;
			case "ClassNotFoundException":
				num = 3;
				break;	
			case "IllegalAccessException":
				num = 4;
				break;	
			case "InstantiationException":
				num = 5;
				break;
			case "NoSuchFieldException":
				num = 6;
				break;
			case "NoSuchMethodException":
				num = 7;
				break;	
			case "ArithmeticException":
				num = 8;
				break;
			case "ArrayStoreException":
				num = 9;
				break;	
			case "ClassCastException":
				num = 10;
				break;
			case "IllegalArgumentException":
				num = 11;
				break;
			case "IllegalStateException":
				num = 12;
				break;
			case "IndexOutOfBoundsException":
				num = 13;
				break;
			case "ArrayIndexOutOfBoundsException":
				num = 14;
				break;
			case "StringIndexOutOfBoundsException":
				num = 15;
				break;
			case "NullPointerException":
				num = 16;
				break;
			case "IllegalThreadStateException":
				num = 17;
				break;
			case "NumberFormatException":
				num = 18;
				break;
			case "UnsupportedOperationException":
				num = 19;
				break;
			case "RuntimeException":
				num = 20;
				break;
			default:
				num = 21;
				break;
		}
		return num;
	}
	
	/**
	 * <p>To find the top method line of stack trace.</p>
	 * @param crash crash lines list
	 * @return
	 */
	String getTopLine(List<String> crash){
		String topLine = "";
		
		topLine = stackTraces.get(0);
		
//		System.out.println("[TOP]: " + topLine);
		return topLine;
	}
	
	boolean isMethodLine(String line){
		boolean flag = false;
		
		if ( (line.startsWith("\tat org.") || line.startsWith("\tat com.j256") || line.startsWith("\tat net.sf"))
				&& !line.contains("Test.java") && !line.contains("com.j256.ormlite.h2.H2DatabaseConnection.queryForOne") 
				&& !line.contains("TestCase.java") && !line.contains("TestUtils.java")
				&& !line.contains("TestData.java") && !line.contains(".access$")){
			flag = true;
		}
		
		return flag;
	}
	
	boolean isTestLine(String line){
		boolean flag = false;
		
		if ( (line.startsWith("\tat org.") || line.startsWith("\tat com.j256") || line.startsWith("\tat net.sf")) 
				&& (line.contains("Test.java") || line.contains("com.j256.ormlite.h2.H2DatabaseConnection.queryForOne") 
						|| line.contains("TestCase.java") || line.contains("TestUtils.java")
						|| line.contains("TestData.java")) ){
			flag = true;
		}
		
		return flag;
	}
	
	/**
	 * <p>To find the bottom method line of stack trace.</p>
	 * @param crash crash crash lines list
	 * @return
	 */
	String getBottomLine(List<String> crash){
		String bottomLine = "";
		int index = stackTraces.size()-1;
		bottomLine = stackTraces.get(index);
		
//		System.out.println("[BOT]: " + bottomLine);
		return bottomLine;
	}
	
	/**
	 * <p>To find the last 2nd bottom method line of stack trace.</p>
	 * @param crash crash crash lines list
	 * @return
	 */
	String getBottom2Line(List<String> crash){
		String bottom2Line = "";
		int index = stackTraces.size()-2;
		if(index<0){
			index = 0;
		}
		bottom2Line = stackTraces.get(index);
		
//		System.out.println("[BOT-2]: " + bottom2Line);
		return bottom2Line;
	}
	
	/**
	 * <p>to return the fault index of crash, the negatives and positives indicate the OutTrace and InTrace crash respectively.</p>
	 * @param crash
	 * @return mutLine: fault index in stack trace
	 */
	int getMutationLine(List<String> crash){
		String mut = crash.get(crash.size()-1);
		MutationLine = mut;
		int mutLine = -1; // default OutTrace
		String reg = "MUTATIONID:<<(.*),(.*),(\\d*)>>";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(mut);
		if(matcher.find()){
			String cls = matcher.group(1).trim();
			String med = matcher.group(2).trim();
			String lin = matcher.group(3).trim();
			
			for(int i=0; i<stackTraces.size(); i++){
				if(stackTraces.get(i).contains(cls) && stackTraces.get(i).contains(med) && stackTraces.get(i).contains(lin)){
					// mutation line inTrace
					mutLine = i;
					break;
				}
			}
//			mutLine = "\tat " + cls + "." + med +
		}
		
		return mutLine;
		
	}
	
	/**
	 * <p>To find the unique classes reside in stack trace.</p>
	 * @param crash crash crash lines list
	 * @return
	 */
	int getClassNum(List<String> crash){
		int count=0;
		Set<String> setCls = new HashSet<String>();
		for(String line: crash){
			if(isMethodLine(line)){
				setCls.add(getClassName(line));
			}
		}
		count = setCls.size();
//		for(String cls: setCls){System.out.println("-- " + cls);}
		return count;
	}
	
	/**
	 * <p>To find the unique method reside in stack trace.</p>
	 * @param crash crash crash lines list
	 * @return
	 */
	int getMethodNum(List<String> crash){
		int count=0;

		for(String line: crash){
			if(isMethodLine(line)){
				count++;
			}
		}
		
//		for(String cls: setMel){System.out.println("-- " + cls);}
		return count;
	}
	
	/**
	 * <p>To find the overloaded  reside in stack trace.</p>
	 * @param crash crash crash lines list
	 * @return
	 */
	boolean isOverLoaded(List<String> crash){
		
		Set<String> setMel = new HashSet<String>();
		int count = 0;
		for(String line: crash){
			if(isMethodLine(line)){
				count ++;
				String fullMethodName = getClassName(line) + "." + getMethodName(line);
				setMel.add(fullMethodName);
			}
		}
		
		return count > setMel.size();
	}
	
	// getter of private variables //////////////////////////////////////////

	public int getBottom2MethodLine(){
		return bottom2MethodLine;
	}
	
	public String getBottom2MethodName(){
		return bottom2MethodName;
	}
	
	public String getBottom2ClassName(){
		return bottom2ClassName;
	}
	
	public int getBottomMethodLine(){
		return bottomMethodLine;
	}
	
	public String getBottomMethodName(){
		return bottomMethodName;
	}
	
	public String getBottomClassName(){
		return bottomClassName;
	}
	
	public int getTopMethodLine(){
		return topMethodLine;
	}
	
	public String getTopMethodName(){
		return topMethodName;
	}
	
	public String getTopClassName(){
		return topClassName;
	}
	
	public int getLOC(){
		return loc;
	}
	
	public int getType(){
		return exceptionType;
	}
	
	public int getClassNum(){
		return classNum;
	}
	
	public int getMethodNum(){
		return methodNum;
	}
	
	public boolean getisOverLoaded(){
		return isOverLoaded;
	}
	
	public void showBasicInfo(){
		System.out.println("------------------## CRASH ##------------------");
//		System.out.println("[TYP]: " + getType());
//		System.out.println("[LOC]: " + loc);
//		System.out.println("[CLS]: " + getClassNum());
//		System.out.println("[MED]: " + getMethodNum());
//		System.out.println("[OLD]: " + getisOverLoaded());
		System.out.println("[TOP  ]: " + getTopClassName() + ", " + getTopMethodName() + ", " + getTopMethodLine());
		System.out.println("[BOT  ]: " + getBottomClassName() + ", " + getBottomMethodName() + ", " + getBottomMethodLine());
		System.out.println("[BOT-2]: " + getBottom2ClassName() + ", " + getBottom2MethodName() + ", " + getBottom2MethodLine());
		System.out.println("------------------------------------------------\n");
	}
	
	public void showStackTrace(List<String> lines){
		for(String line: lines){
			System.out.println(">> " + line);
		}
		System.out.println("++++++++++");
	}
	
	
	////////////////////////////////////////////
}
