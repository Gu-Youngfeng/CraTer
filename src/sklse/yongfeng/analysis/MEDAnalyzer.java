package sklse.yongfeng.analysis;

import java.util.List;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.code.CtAssignment;
import spoon.reflect.code.CtBinaryOperator;
import spoon.reflect.code.CtCatch;
import spoon.reflect.code.CtDo;
import spoon.reflect.code.CtFor;
import spoon.reflect.code.CtForEach;
import spoon.reflect.code.CtIf;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLocalVariable;
import spoon.reflect.code.CtLoop;
import spoon.reflect.code.CtReturn;
import spoon.reflect.code.CtStatement;
import spoon.reflect.code.CtTry;
import spoon.reflect.code.CtUnaryOperator;
import spoon.reflect.code.CtWhile;
import spoon.reflect.declaration.CtAnonymousExecutable;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.visitor.filter.TypeFilter;

public class MEDAnalyzer {

	public static void main(String[] args) {
		MEDAnalyzer iii;
		try {
//			iii = new MEDAnalyzer("src/main/resources/projs/Collection_4.1_parent/", "org.apache.commons.collections4.IterableUtils$2$1", "nextIterator", 180);
//			iii = new MEDAnalyzer("src/main/resources/projs/Collection_4.1_parent/", "org.apache.commons.collections4.trie.AbstractPatriciaTrie$PrefixRangeEntrySet$EntryIterator", "next", 2337);
//			iii = new MEDAnalyzer("src/main/resources/projs/Collection_4.1_parent/", "org.apache.commons.collections4.trie.AbstractPatriciaTrie$RangeEntrySet$EntryIterator", "next", 2056);
//			iii = new MEDAnalyzer("src/main/resources/projs/JSQL_parent/", "net.sf.jsqlparser.parser.SimpleNode", "jjtAddChild", 39);
//			iii = new MEDAnalyzer("src/main/resources/projs/JSQL_parent/", "net.sf.jsqlparser.parser.JJTCCJSqlParserState", "popNode", 53);
			iii = new MEDAnalyzer("src/main/resources/projs/JSQL_parent/", "net.sf.jsqlparser.parser.CCJSqlParserDefaultVisitor", "visit", 10);
			iii.showMEDFeatures();
		} catch (Exception e) {
			System.out.println("[NONE METHOD]");
			e.printStackTrace();
		}	
//				
//		String fullClass = "src/main/resources/projs/Commons-io-2.5_parent/src/main/java/org/apache/commons/io/comparator/CompositeFileComparator.java";
//		
	}
	
	/** CT07/CB07: LoC of the top/bottom function */
	private int loc;
	/** CT08/CB08: Number of parameters in the top/bottom function */
	private int params;
	/** CT09/CT09: Number of local variables in the top/bottom function */
	private int locals;
	/** CT10:CB10: Number of if-statements in the top/bottom function */
	private int ifs;
	/** CT11:CB11: Number of loops in the top/bottom function */
	private int loops;
	/** CT12:CB12: Number of for statements in the top/bottom function */
	private int fors;
	/** CT13:CB13: Number of for-each statements in the top/bottom function */
	private int for_eachs;
	/** CT14:CB14: Number of while statements in the top/bottom function */
	private int whiles;
	/** CT15/CB15: Number of do-while statements in the top/bottom function */
	private int do_whiles;
	/** CT16/CB16: Number of try blocks in the top/bottom function */
	private int try_blocks;
	/** CT17/CB17: Number of catch blocks in the top/bottom function */
	private int catchs;
	/** CT18:CB18: Number of finally blocks in the top/bottom function */
	private int finally_blocks;
	/** CT19:CB19: Number of assignment statements in the top/bottom function */
	private int assignments;
	/** CT20:CB20: Number of method calls in the top/bottom function */
	private int method_calls;
	/** CT21:CB21: Number of return statements in the top/bottom function */
	private int returns;
	/** CT22:CB22: Number of unary operators in the top/bottom function */
	private int unary_operators;
	/** CT23:CB23: Number of binary operators in the top/bottom function */
	private int binary_operators;
	
	public int startLine;
	
	/** AT01/AB01: CT08/CT07*/
	float at01;
	/** AT02/AB02: CT09/CT07*/
	float at02;
	/** AT03/AB03: CT10/CT07*/
	float at03;
	/** AT04/AB04: CT11/CT07*/
	float at04;
	/** AT05/AB05: CT12/CT07*/
	float at05;
	/** AT06/AB06: CT13/CT07*/
	float at06;
	/** AT07/AB07: CT14/CT07*/
	float at07;
	/** AT08/AB08: CT15/CT07*/
	float at08;
	/** AT09/AB09: CT16/CT07*/
	float at09;
	/** AT10/AB10: CT17/CT07*/
	float at10;
	/** AT11/AB11: CT18/CT07*/
	float at11;
	/** AT12/AB12: CT19/CT07*/
	float at12;
	/** AT13/AB13: CT20/CT07*/
	float at13;
	/** AT14/AB14: CT21/CT07*/
	float at14;
	/** AT15/AB15: CT22/CT07*/
	float at15;
	/** AT16/AB16: CT23/CT07*/
	float at16;
	
	@SuppressWarnings("rawtypes")
	public int getStartLine(CtMethod method){
		if(method.getBody() == null){
			return 1;
		}else{
			return method.getBody().getPosition().getLine();
		}
	}
	@SuppressWarnings("rawtypes")
	public int getStartLine(CtConstructor method){
		if(method.getBody() == null){
			return 1;
		}else{
			return method.getBody().getPosition().getLine();
		}
	}
	public int getStartLine(CtAnonymousExecutable method){
		if(method.getBody() == null){
			return 1;
		}else{
			return method.getBody().getPosition().getLine();
		}
	}
	
	@SuppressWarnings("rawtypes")
	public int getLoc(CtMethod method){
		int count=0;
		int startLine;
		int endLine;
		if(method.getBody() == null){
			return 1;
		}else{
			startLine = method.getBody().getPosition().getLine();
			endLine = method.getBody().getPosition().getEndLine();
		}
		count = endLine - startLine + 1;
//		System.out.println("[start]: " + startLine + ", [end]: " + endLine);
		return count;
	}
	@SuppressWarnings("rawtypes")
	public int getLoc(CtConstructor method){
		int count=0;
		int startLine;
		int endLine;
		if(method.getBody() == null){
			return 1;
		}else{
			startLine = method.getBody().getPosition().getLine();
			endLine = method.getBody().getPosition().getEndLine();
		}
		count = endLine - startLine + 1;
//		System.out.println("[start]: " + startLine + ", [end]: " + endLine);
		return count;
	}
	
	public int getLoc(CtAnonymousExecutable method){
		int count=0;
		int startLine;
		int endLine;
		if(method.getBody() == null){
			return 1;
		}else{
			startLine = method.getBody().getPosition().getLine();
			endLine = method.getBody().getPosition().getEndLine();
		}
		count = endLine - startLine + 1;
//		System.out.println("[start]: " + startLine + ", [end]: " + endLine);
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getParams(CtMethod method){
		int count=0;
		List<CtParameter> lsParams = method.getElements(new TypeFilter(CtParameter.class));
//		for(CtParameter param: lsParams){
//			System.out.println("[param]: " + param.getSimpleName());
//		}
		count = lsParams.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getParams(CtConstructor method){
		int count=0;
		List<CtParameter> lsParams = method.getElements(new TypeFilter(CtParameter.class));
//		for(CtParameter param: lsParams){
//			System.out.println("[param]: " + param.getSimpleName());
//		}
		count = lsParams.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getParams(CtAnonymousExecutable method){
		int count=0;
		List<CtParameter> lsParams = method.getElements(new TypeFilter(CtParameter.class));
//		for(CtParameter param: lsParams){
//			System.out.println("[param]: " + param.getSimpleName());
//		}
		count = lsParams.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLocals(CtMethod method){
		int count=0;
		List<CtLocalVariable> lsLocals = method.getElements(new TypeFilter(CtLocalVariable.class));
//		for(CtLocalVariable param: lsLocals){
//			System.out.println("[local variable]: " + param.getSimpleName());
//		}
		count = lsLocals.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLocals(CtConstructor method){
		int count=0;
		List<CtLocalVariable> lsLocals = method.getElements(new TypeFilter(CtLocalVariable.class));
//		for(CtLocalVariable param: lsLocals){
//			System.out.println("[local variable]: " + param.getSimpleName());
//		}
		count = lsLocals.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLocals(CtAnonymousExecutable method){
		int count=0;
		List<CtLocalVariable> lsLocals = method.getElements(new TypeFilter(CtLocalVariable.class));
//		for(CtLocalVariable param: lsLocals){
//			System.out.println("[local variable]: " + param.getSimpleName());
//		}
		count = lsLocals.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getIfs(CtMethod method){
		int count=0;
		List<CtIf> lsIfs = method.getElements(new TypeFilter(CtIf.class));
//		for(CtIf param: lsIfs){
//			System.out.println("[if]: " + param.toString());
//		}
		count = lsIfs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getIfs(CtConstructor method){
		int count=0;
		List<CtIf> lsIfs = method.getElements(new TypeFilter(CtIf.class));
//		for(CtIf param: lsIfs){
//			System.out.println("[if]: " + param.toString());
//		}
		count = lsIfs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getIfs(CtAnonymousExecutable method){
		int count=0;
		List<CtIf> lsIfs = method.getElements(new TypeFilter(CtIf.class));
//		for(CtIf param: lsIfs){
//			System.out.println("[if]: " + param.toString());
//		}
		count = lsIfs.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLoops(CtMethod method){
		int count=0;
		List<CtLoop> lsLoops = method.getElements(new TypeFilter(CtLoop.class));
//		for(CtLoop param: lsLoops){
//			System.out.println("[loop]: " + param.toString());
//		}
		count = lsLoops.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLoops(CtConstructor method){
		int count=0;
		List<CtLoop> lsLoops = method.getElements(new TypeFilter(CtLoop.class));
//		for(CtLoop param: lsLoops){
//			System.out.println("[loop]: " + param.toString());
//		}
		count = lsLoops.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getLoops(CtAnonymousExecutable method){
		int count=0;
		List<CtLoop> lsLoops = method.getElements(new TypeFilter(CtLoop.class));
//		for(CtLoop param: lsLoops){
//			System.out.println("[loop]: " + param.toString());
//		}
		count = lsLoops.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getFors(CtMethod method){
		int count=0;
		List<CtFor> lsFors = method.getElements(new TypeFilter(CtFor.class));
//		for(CtFor param: lsFors){
//			System.out.println("[for]: " + param.toString());
//		}
		count = lsFors.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getFors(CtConstructor method){
		int count=0;
		List<CtFor> lsFors = method.getElements(new TypeFilter(CtFor.class));
//		for(CtFor param: lsFors){
//			System.out.println("[for]: " + param.toString());
//		}
		count = lsFors.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getFors(CtAnonymousExecutable method){
		int count=0;
		List<CtFor> lsFors = method.getElements(new TypeFilter(CtFor.class));
//		for(CtFor param: lsFors){
//			System.out.println("[for]: " + param.toString());
//		}
		count = lsFors.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getForEachs(CtMethod method){
		int count=0;
		List<CtForEach> lsForEachs = method.getElements(new TypeFilter(CtForEach.class));
//		for(CtForEach param: lsForEachs){
//			System.out.println("[for each]: " + param.toString());
//		}
		count = lsForEachs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getForEachs(CtConstructor method){
		int count=0;
		List<CtForEach> lsForEachs = method.getElements(new TypeFilter(CtForEach.class));
//		for(CtForEach param: lsForEachs){
//			System.out.println("[for each]: " + param.toString());
//		}
		count = lsForEachs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getForEachs(CtAnonymousExecutable method){
		int count=0;
		List<CtForEach> lsForEachs = method.getElements(new TypeFilter(CtForEach.class));
//		for(CtForEach param: lsForEachs){
//			System.out.println("[for each]: " + param.toString());
//		}
		count = lsForEachs.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getWhiles(CtMethod method){
		int count=0;
		List<CtWhile> lsWhiles = method.getElements(new TypeFilter(CtWhile.class));
//		for(CtWhile param: lsWhiles){
//			System.out.println("[while]: " + param.toString());
//		}
		count = lsWhiles.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getWhiles(CtConstructor method){
		int count=0;
		List<CtWhile> lsWhiles = method.getElements(new TypeFilter(CtWhile.class));
//		for(CtWhile param: lsWhiles){
//			System.out.println("[while]: " + param.toString());
//		}
		count = lsWhiles.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getWhiles(CtAnonymousExecutable method){
		int count=0;
		List<CtWhile> lsWhiles = method.getElements(new TypeFilter(CtWhile.class));
//		for(CtWhile param: lsWhiles){
//			System.out.println("[while]: " + param.toString());
//		}
		count = lsWhiles.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getDoWhiles(CtMethod method){
		int count=0;
		List<CtDo> lsDoWhiles = method.getElements(new TypeFilter(CtDo.class));
//		for(CtDo param: lsDoWhiles){
//			System.out.println("[do while]: " + param.toString());
//		}
		count = lsDoWhiles.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getDoWhiles(CtConstructor method){
		int count=0;
		List<CtDo> lsDoWhiles = method.getElements(new TypeFilter(CtDo.class));
//		for(CtDo param: lsDoWhiles){
//			System.out.println("[do while]: " + param.toString());
//		}
		count = lsDoWhiles.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getDoWhiles(CtAnonymousExecutable method){
		int count=0;
		List<CtDo> lsDoWhiles = method.getElements(new TypeFilter(CtDo.class));
//		for(CtDo param: lsDoWhiles){
//			System.out.println("[do while]: " + param.toString());
//		}
		count = lsDoWhiles.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getTryBlocks(CtMethod method){
		int count=0;
		List<CtTry> lsTryBlocks = method.getElements(new TypeFilter(CtTry.class));
//		for(CtTry param: lsTryBlocks){
//			System.out.println("[try block]: " + param.toString());
//		}
		count = lsTryBlocks.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getTryBlocks(CtConstructor method){
		int count=0;
		List<CtTry> lsTryBlocks = method.getElements(new TypeFilter(CtTry.class));
//		for(CtTry param: lsTryBlocks){
//			System.out.println("[try block]: " + param.toString());
//		}
		count = lsTryBlocks.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getTryBlocks(CtAnonymousExecutable method){
		int count=0;
		List<CtTry> lsTryBlocks = method.getElements(new TypeFilter(CtTry.class));
//		for(CtTry param: lsTryBlocks){
//			System.out.println("[try block]: " + param.toString());
//		}
		count = lsTryBlocks.size();
		return count;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCatchs(CtMethod method){
		int count=0;
		List<CtCatch> lsCatchs = method.getElements(new TypeFilter(CtCatch.class));
//		for(CtCatch param: lsCatchs){
//			System.out.println("[catch]: " + param.toString());
//		}
		count = lsCatchs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCatchs(CtConstructor method){
		int count=0;
		List<CtCatch> lsCatchs = method.getElements(new TypeFilter(CtCatch.class));
//		for(CtCatch param: lsCatchs){
//			System.out.println("[catch]: " + param.toString());
//		}
		count = lsCatchs.size();
		return count;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getCatchs(CtAnonymousExecutable method){
		int count=0;
		List<CtCatch> lsCatchs = method.getElements(new TypeFilter(CtCatch.class));
//		for(CtCatch param: lsCatchs){
//			System.out.println("[catch]: " + param.toString());
//		}
		count = lsCatchs.size();
		return count;
	}
	
	@SuppressWarnings({ "rawtypes" })
	public int getFinallys(CtMethod method){
		int count=0;
		if(method.getBody() == null){
			return 0;
		}
		for(CtStatement state: method.getBody().getStatements()){
			if(state.toString().contains("finally")){
//				System.out.println("[fianlly block]: " + state.toString());
				count++;
			}
		}
		return count;
	}
	@SuppressWarnings({ "rawtypes" })
	public int getFinallys(CtConstructor method){
		int count=0;
		if(method.getBody() == null){
			return 0;
		}
		for(CtStatement state: method.getBody().getStatements()){
			if(state.toString().contains("finally")){
//				System.out.println("[fianlly block]: " + state.toString());
				count++;
			}
		}
		return count;
	}

	public int getFinallys(CtAnonymousExecutable method){
		int count=0;
		if(method.getBody() == null){
			return 0;
		}
		for(CtStatement state: method.getBody().getStatements()){
			if(state.toString().contains("finally")){
//				System.out.println("[fianlly block]: " + state.toString());
				count++;
			}
		}
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getAssignments(CtMethod method){
		int count=0;
		List<CtAssignment> lsAssignments = method.getElements(new TypeFilter(CtAssignment.class));
//		for(CtAssignment param: lsAssignments){
//			System.out.println("[assignment]: " + param.toString());
//		}
		count = lsAssignments.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getAssignments(CtConstructor method){
		int count=0;
		List<CtAssignment> lsAssignments = method.getElements(new TypeFilter(CtAssignment.class));
//		for(CtAssignment param: lsAssignments){
//			System.out.println("[assignment]: " + param.toString());
//		}
		count = lsAssignments.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getAssignments(CtAnonymousExecutable method){
		int count=0;
		List<CtAssignment> lsAssignments = method.getElements(new TypeFilter(CtAssignment.class));
//		for(CtAssignment param: lsAssignments){
//			System.out.println("[assignment]: " + param.toString());
//		}
		count = lsAssignments.size();
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getInvocations(CtMethod method){
		int count=0;
		List<CtInvocation> lsInvocations = method.getElements(new TypeFilter(CtInvocation.class));
//		for(CtInvocation param: lsInvocations){
//			System.out.println("[invocation]: " + param.toString());
//		}
		count = lsInvocations.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getInvocations(CtConstructor method){
		int count=0;
		List<CtInvocation> lsInvocations = method.getElements(new TypeFilter(CtInvocation.class));
//		for(CtInvocation param: lsInvocations){
//			System.out.println("[invocation]: " + param.toString());
//		}
		count = lsInvocations.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getInvocations(CtAnonymousExecutable method){
		int count=0;
		List<CtInvocation> lsInvocations = method.getElements(new TypeFilter(CtInvocation.class));
//		for(CtInvocation param: lsInvocations){
//			System.out.println("[invocation]: " + param.toString());
//		}
		count = lsInvocations.size();
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getReturns(CtMethod method){
		int count=0;
		List<CtReturn> lsReturns = method.getElements(new TypeFilter(CtReturn.class));
//		for(CtReturn param: lsReturns){
//			System.out.println("[return]: " + param.toString());
//		}
		count = lsReturns.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getReturns(CtConstructor method){
		int count=0;
		List<CtReturn> lsReturns = method.getElements(new TypeFilter(CtReturn.class));
//		for(CtReturn param: lsReturns){
//			System.out.println("[return]: " + param.toString());
//		}
		count = lsReturns.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getReturns(CtAnonymousExecutable method){
		int count=0;
		List<CtReturn> lsReturns = method.getElements(new TypeFilter(CtReturn.class));
//		for(CtReturn param: lsReturns){
//			System.out.println("[return]: " + param.toString());
//		}
		count = lsReturns.size();
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getUnaryOperators(CtMethod method){
		int count=0;
		List<CtUnaryOperator> lsuops = method.getElements(new TypeFilter(CtUnaryOperator.class));
//		for(CtUnaryOperator param: lsuops){
//			System.out.println("[unary operator]: " + param.toString());
//		}
		count = lsuops.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getUnaryOperators(CtConstructor method){
		int count=0;
		List<CtUnaryOperator> lsuops = method.getElements(new TypeFilter(CtUnaryOperator.class));
//		for(CtUnaryOperator param: lsuops){
//			System.out.println("[unary operator]: " + param.toString());
//		}
		count = lsuops.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getUnaryOperators(CtAnonymousExecutable method){
		int count=0;
		List<CtUnaryOperator> lsuops = method.getElements(new TypeFilter(CtUnaryOperator.class));
//		for(CtUnaryOperator param: lsuops){
//			System.out.println("[unary operator]: " + param.toString());
//		}
		count = lsuops.size();
		return count;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getBinaryOperators(CtMethod method){
		int count=0;
		List<CtBinaryOperator> lsbops = method.getElements(new TypeFilter(CtBinaryOperator.class));
//		for(CtBinaryOperator param: lsbops){
//			System.out.println("[binary operator]: " + param.toString());
//		}
		count = lsbops.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getBinaryOperators(CtConstructor method){
		int count=0;
		List<CtBinaryOperator> lsbops = method.getElements(new TypeFilter(CtBinaryOperator.class));
//		for(CtBinaryOperator param: lsbops){
//			System.out.println("[binary operator]: " + param.toString());
//		}
		count = lsbops.size();
		return count;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getBinaryOperators(CtAnonymousExecutable method){
		int count=0;
		List<CtBinaryOperator> lsbops = method.getElements(new TypeFilter(CtBinaryOperator.class));
//		for(CtBinaryOperator param: lsbops){
//			System.out.println("[binary operator]: " + param.toString());
//		}
		count = lsbops.size();
		return count;
	}
	
	@SuppressWarnings("rawtypes")
	public MEDAnalyzer(String proj, String clsName, String medName, int medLine) throws Exception {
		
//		System.out.printf("[class]:%s, [method]:%s, [line]:%d\n", clsName, medName, medLine);
		
		if(clsName.contains("$2$1")){ // patch for "org.apache.commons.collections4.IterableUtils$2$1"
			clsName = clsName.substring(0, clsName.indexOf("$"));
		}
		
		if(clsName.contains("$")){ // inner class
			String innerClass = clsName.substring(clsName.lastIndexOf("$")+1);
			clsName = clsName.substring(0, clsName.indexOf("$"));
			
//			System.out.println("[inner]:" + innerClass);
			/** Building the meta model */
			String fullClass = proj + "src/main/java/" + clsName.replaceAll("\\.", "/") + ".java";		
			Launcher launcher = new Launcher();
			launcher.addInputResource(fullClass);
			launcher.getEnvironment().setCommentEnabled(true);
			CtModel metaModel = launcher.buildModel();
			
			/** Building the CtMethod */	
//			String simpleClsName = clsName.split("\\.")[clsName.split("\\.").length-1];
//			System.out.println("[simpleClsName]: " + simpleClsName);
			if(medName.equals(innerClass)){ // Constructor
				CtConstructor constructor = getCtConstructor(metaModel, clsName, "<init>", medLine);
				/** extract the features from constructor */
				extractFeatures(constructor);
				
			}else if(medName.equals("<clinit>")){ // static block
				CtAnonymousExecutable staticc = getCtStatic(metaModel, clsName, "<clinit>", medLine);
				extractFeatures(staticc);
			}else{ // method
				CtMethod method = getCtMethod(metaModel, clsName, medName, medLine);
				if(method == null){ // inherited method appear
//					System.out.println("NONE METHOD");
//					System.out.println("######### Exception:" + clsName + " >> " + medName + " >> " + medLine);
					throw new Exception();
				}
				/** extract the features from method */
				extractFeatures(method);
			}
		}else{ // no inner class
			/** Building the meta model */
			String fullClass = proj + "src/main/java/" + clsName.replaceAll("\\.", "/") + ".java";		
			Launcher launcher = new Launcher();
			launcher.addInputResource(fullClass);
			launcher.getEnvironment().setCommentEnabled(true);
			CtModel metaModel = launcher.buildModel();
			
			/** Building the CtMethod */	
			String simpleClsName = clsName.split("\\.")[clsName.split("\\.").length-1];
//			System.out.println("[simpleClsName]: " + simpleClsName);
			if(medName.equals(simpleClsName)){ // Constructor
				CtConstructor constructor = getCtConstructor(metaModel, clsName, "<init>", medLine);
				/** extract the features from constructor */
				extractFeatures(constructor);
				
			}else if(medName.equals("<clinit>")){ // static block
				CtAnonymousExecutable staticc = getCtStatic(metaModel, clsName, "<clinit>", medLine);
				extractFeatures(staticc);
			}else{ // method
				CtMethod method = getCtMethod(metaModel, clsName, medName, medLine);
				if(method == null){ // inherited method appear
//					System.out.println("NONE METHOD");
//					System.out.println("######### Exception:" + clsName + " >> " + medName + " >> " + medLine);
					throw new Exception();
				}
				/** extract the features from method */
				extractFeatures(method);
			}
		}
		
//		System.out.println(clsName + "," + medName + "," + medLine);
		
		
		extractArtifactFeatures();	
		
	}
	
	/**
	 * To get static block from given metaModel, class name, method name, line number. 
	 * @param metaModel
	 * @param clsName class name
	 * @param medName method name
	 * @param medLine line number
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CtAnonymousExecutable getCtStatic(CtModel metaModel, String clsName, String medName, int medLine){
		
		List<CtClass> cls = metaModel.getElements(new TypeFilter(CtClass.class));
		CtClass curCls = cls.get(0);
		for(CtClass currentCls: cls){
			if(currentCls.getQualifiedName().equals(clsName)){
				curCls = currentCls;
				break;
			}
		}
		
		List<CtAnonymousExecutable> lsMethods = curCls.getElements(new TypeFilter(CtAnonymousExecutable.class));
		CtAnonymousExecutable methodModel = null; // the unique founded method
		
		for(CtAnonymousExecutable method: lsMethods){
			int startLine;
			int endLine;
			if(method.getBody() == null) {
				startLine = medLine;
				endLine = medLine;
			}else{
				startLine = method.getBody().getPosition().getLine();
				endLine = method.getBody().getPosition().getEndLine();
			}
						
			if(medLine >= startLine && medLine <= endLine){
				
				methodModel = method;
//				String simpleName = method.getSimpleName();
//				System.out.println("method: " + method.getSignature());				
//				System.out.println("range : " + startLine + "," + endLine);
				break;
			}
		}
		
//		if(methodModel == null){
//			System.out.println("[MEDAnalyzer Error]: We cannot find static at [" + clsName + "." + medName + ":" + medLine + "]");
//		}
		
		return methodModel;
	}
	
	/**
	 * To get method from given metaModel, class name, method name, line number. 
	 * @param metaModel
	 * @param clsName class name
	 * @param medName method name
	 * @param medLine line number
	 * @return
	 * @throws SuperClassException 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CtMethod getCtMethod(CtModel metaModel, String clsName, String medName, int medLine) {
		
		List<CtClass> cls = metaModel.getElements(new TypeFilter(CtClass.class));
		CtClass curCls = cls.get(0);
		for(CtClass currentCls: cls){
			if(currentCls.getQualifiedName().equals(clsName)){
				curCls = currentCls;
//				System.out.println("[qualified inner class]: " + curCls.getQualifiedName());
				break;
			}
		}
		
		List<CtMethod> lsMethods = curCls.getElements(new TypeFilter(CtMethod.class));
		CtMethod methodModel = null; // the unique founded method
//		System.out.println("class: " + curCls.getSimpleName());	
		for(CtMethod method: lsMethods){
			int startLine;
			int endLine;
			if(method.getBody() == null) {
				startLine = medLine;
				endLine = medLine;
			}else{
				startLine = method.getBody().getPosition().getLine();
				endLine = method.getBody().getPosition().getEndLine();
			}
			
			String simpleName = method.getSimpleName();
//			System.out.println("method: " + method.getSignature() + ". [" + startLine + "," + endLine + "]");	
			if(simpleName.equals(medName) && (medLine >= startLine && medLine <= endLine)){
				
				methodModel = method;
//				System.out.println("method: " + method.getSignature());				
//				System.out.println("range : " + startLine + "," + endLine);
				break;
			}
		}
		
//		if(methodModel == null){ // we can not find method at this location
//			System.out.println("[MEDAnalyzer Error]: We cannot find method at [" + clsName + "." + medName + ":" + medLine + "]");
//		}
		
		return methodModel;
	}
	
	/**
	 * To get constructor from given metaModel, class name, method name, line number. 
	 * @param metaModel
	 * @param clsName class name
	 * @param medName method name
	 * @param medLine line number
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CtConstructor getCtConstructor(CtModel metaModel, String clsName, String medName, int medLine){
		
		List<CtClass> cls = metaModel.getElements(new TypeFilter(CtClass.class));
		CtClass curCls = cls.get(0);
		for(CtClass currentCls: cls){
			if(currentCls.getQualifiedName().equals(clsName)){
				curCls = currentCls;
				break;
			}
		}
		
		List<CtConstructor> lsConstructors = curCls.getElements(new TypeFilter(CtConstructor.class));
		CtConstructor constructorModel = null; // the unique founded constructor

		for(CtConstructor method: lsConstructors){
			int startLine = method.getBody().getPosition().getLine();
			int endLine = method.getBody().getPosition().getEndLine();
			String simpleName = method.getSimpleName();
			
			if(simpleName.equals(medName) && (medLine >= startLine && medLine <= endLine)){
				constructorModel = method;
//				System.out.println("constructor: " + method.getSignature());				
//				System.out.println("range      : " + startLine + "," + endLine);
				break;
			}
		}
		
//		if(constructorModel == null){
//			System.out.println("[MEDAnalyzer Error]: We cannot find constructor at [" + clsName + "." + medName + ":" + medLine + "]");
//		}
		
		return constructorModel;
	}
	
	public void showMEDFeatures(){
		System.out.print(loc + "," + params + "," + locals + "," + ifs + "," + loops + "," + fors
				+ "," + for_eachs + "," + whiles + "," + do_whiles + "," + try_blocks + "," + catchs
				+ "," + finally_blocks + "," + assignments + "," + method_calls + "," + returns
				+ "," + unary_operators + "," + binary_operators
				
				+ "," + at01 + "," + at02 + "," + at03 + "," + at04 + "," + at05 + "," + at06
				+ "," + at07 + "," + at08 + "," + at09 + "," + at10 + "," + at11 + "," + at12
				+ "," + at13 + "," + at14 + "," + at15 + "," + at16 + ",");
		
	}
	
	/** extract the features from method */
	@SuppressWarnings("rawtypes")
	public void extractFeatures(CtMethod method){
		loc = getLoc(method);
		params = getParams(method);
		locals = getLocals(method);
		ifs = getIfs(method);
		loops = getLoops(method);
		fors = getFors(method);
		for_eachs = getForEachs(method);
		whiles = getWhiles(method);
		do_whiles = getDoWhiles(method);
		try_blocks = getTryBlocks(method);
		catchs = getCatchs(method);
		finally_blocks = getFinallys(method);
		assignments = getAssignments(method);
		method_calls = getInvocations(method);
		returns = getReturns(method);
		unary_operators = getUnaryOperators(method);
		binary_operators = getBinaryOperators(method);
		
		startLine = getStartLine(method);
	}
	
	/** extract the features from constructor */
	@SuppressWarnings("rawtypes")
	public void extractFeatures(CtConstructor method){
		loc = getLoc(method);
		params = getParams(method);
		locals = getLocals(method);
		ifs = getIfs(method);
		loops = getLoops(method);
		fors = getFors(method);
		for_eachs = getForEachs(method);
		whiles = getWhiles(method);
		do_whiles = getDoWhiles(method);
		try_blocks = getTryBlocks(method);
		catchs = getCatchs(method);
		finally_blocks = getFinallys(method);
		assignments = getAssignments(method);
		method_calls = getInvocations(method);
		returns = getReturns(method);
		unary_operators = getUnaryOperators(method);
		binary_operators = getBinaryOperators(method);
		
		startLine = getStartLine(method);
	}
	
	/** extract the features from static blocks */
	public void extractFeatures(CtAnonymousExecutable method){
		loc = getLoc(method);
		params = getParams(method);
		locals = getLocals(method);
		ifs = getIfs(method);
		loops = getLoops(method);
		fors = getFors(method);
		for_eachs = getForEachs(method);
		whiles = getWhiles(method);
		do_whiles = getDoWhiles(method);
		try_blocks = getTryBlocks(method);
		catchs = getCatchs(method);
		finally_blocks = getFinallys(method);
		assignments = getAssignments(method);
		method_calls = getInvocations(method);
		returns = getReturns(method);
		unary_operators = getUnaryOperators(method);
		binary_operators = getBinaryOperators(method);
		
		startLine = getStartLine(method);
	}
	
	public void extractArtifactFeatures(){
		at01 = (float) (params*1.0/loc*1.0);
		at02 = (float) (locals*1.0/loc*1.0);
		at03 = (float) (ifs*1.0/loc*1.0);
		at04 = (float) (loops*1.0/loc*1.0);
		at05 = (float) (fors*1.0/loc*1.0);
		at06 = (float) (for_eachs*1.0/loc*1.0);
		at07 = (float) (whiles*1.0/loc*1.0);
		at08 = (float) (do_whiles*1.0/loc*1.0);
		at09 = (float) (try_blocks*1.0/loc*1.0);
		at10 = (float) (catchs*1.0/loc*1.0);
		at11 = (float) (finally_blocks*1.0/loc*1.0);
		at12 = (float) (assignments*1.0/loc*1.0);
		at13 = (float) (method_calls*1.0/loc*1.0);
		at14 = (float) (returns*1.0/loc*1.0);
		at15 = (float) (unary_operators*1.0/loc*1.0);
		at16 = (float) (binary_operators*1.0/loc*1.0);
	}

	public int returnLOC(){
		return loc;
	}
}
