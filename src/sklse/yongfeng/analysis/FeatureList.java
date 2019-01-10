package sklse.yongfeng.analysis;

/***
 * <p>This enum <b>FeatureList</b> collects all the features we extracted from the <b>source code</b> and <b>stack trace</b>.
 * The features mining operations are provided by functions as follows,</p>
 * 
 * <li>ST01~ST09: {@link sklse.yongfeng.analysis.RepsUtilier#showSTFeatures}</li> 
 * <li>ST10~ST10: {@link sklse.yongfeng.analysis.SRCAnalyzer#showSRCFeatures}</li>
 * <li>CT01~CT06, CB01~CB06: {@link sklse.yongfeng.analysis.CLSAnalyzer#showCLSFeatures}</li>
 * <li>CT07~CT23, CB07~CB23: {@link sklse.yongfeng.analysis.MEDAnalyzer#showMEDFeatures}</li>
 * <li>AT01~AT16, AB01~AB16: {@link sklse.yongfeng.analysis.MEDAnalyzer#showMEDFeatures}</li>
 * 
 * @author yongfeng
 * @update 2019.01.10
 */
public enum FeatureList {
	
	// Extracted from sklse.yongfeng.analysis.RepsUtilsier.showSTFeatures()
	/**ST01: Type of the exception in the crash*/ ST01, 
	/**ST02: Number of frames of the stack trace*/ ST02, 
	/**ST03: Number of classes in the stack trace*/ ST03, 
	/**ST04: Number of methods in the stack trace*/ ST04, 
	/**ST05: Whether an overloaded method exists in the stack trace*/ ST05, 
	/**ST06: Length of the name in the top class*/ ST06, 
	/**ST07: Length of the name in the top function*/ ST07, 
	/**ST08: Length of the name in the bottom class*/ ST08, 
	/**ST09: Length of the name in the bottom function*/ ST09, 
	
	// Extracted from sklse.yongfeng.analysis.SRCAnalyzer.showSRCFeatures()
	/**ST10: Number of Java files in the project */ ST10, 
	/**ST11: Number of classes in the project */ ST11, 
	
	// Extracted from sklse.yongfeng.analysis.CLSAnalyzer.showCLSFeatures()
	/** CT01/CB01: Number of local variables in the top/bottom class */ CT01, 
	/** CT02/CB02: Number of fields in the top/bottom class */ CT02, 
	/** CT03/CB03: Number of functions (except constructor functions) in the top/bottom class*/ CT03, 
	/** CT04/CB04: Number of imported packages in the top/bottom class */ CT04, 
	/** CT05/CB05: Whether the top/bottom class is inherited from others */ CT05, 
	/** CT06/CB06: LoC of comments in the top/bottom class */ CT06, 
	
	// Extracted from sklse.yongfeng.analysis.MEDAnalyzer.showMEDFeatures()
	/** CT07/CB07: LoC of the top/bottom function */ CT07,
	/** CT08/CB08: Number of parameters in the top/bottom function */ CT08,
	/** CT09/CT09: Number of local variables in the top/bottom function */ CT09,
	/** CT10/CB10: Number of if-statements in the top/bottom function */ CT10,
	/** CT11/CB11: Number of loops in the top/bottom function */ CT11,
	/** CT12/CB12: Number of for statements in the top/bottom function */ CT12,
	/** CT13/CB13: Number of for-each statements in the top/bottom function */ CT13,
	/** CT14/CB14: Number of while statements in the top/bottom function */ CT14,
	/** CT15/CB15: Number of do-while statements in the top/bottom function */ CT15,
	/** CT16/CB16: Number of try blocks in the top/bottom function */ CT16,
	/** CT17/CB17: Number of catch blocks in the top/bottom function */ CT17,
	/** CT18/CB18: Number of finally blocks in the top/bottom function */ CT18,
	/** CT19/CB19: Number of assignment statements in the top/bottom function */ CT19,
	/** CT20/CB20: Number of method calls in the top/bottom function */ CT20,
	/** CT21/CB21: Number of return statements in the top/bottom function */ CT21,
	/** CT22/CB22: Number of unary operators in the top/bottom function */ CT22,
	/** CT23/CB23: Number of binary operators in the top/bottom function */ CT23,
	
	// Extracted from sklse.yongfeng.analysis.MEDAnalyzer.showMEDFeatures()
	/** AT01: CT08/CT07*/ AT01,
	/** AT02: CT09/CT07*/ AT02,
	/** AT03: CT10/CT07*/ AT03,
	/** AT04: CT11/CT07*/ AT04,
	/** AT05: CT12/CT07*/ AT05,
	/** AT06: CT13/CT07*/ AT06,
	/** AT07: CT14/CT07*/ AT07,
	/** AT08: CT15/CT07*/ AT08,
	/** AT09: CT16/CT07*/ AT09,
	/** AT10: CT17/CT07*/ AT10,
	/** AT11: CT18/CT07*/ AT11,
	/** AT12: CT19/CT07*/ AT12,
	/** AT13: CT20/CT07*/ AT13,
	/** AT14: CT21/CT07*/ AT14,
	/** AT15: CT22/CT07*/ AT15,
	/** AT16: CT23/CT07*/ AT16,
	
	// Extracted from sklse.yongfeng.analysis.MEDAnalyzer.showMEDFeatures()
	/** AB01: CB08/CB07*/ AB01,
	/** AB02: CB09/CB07*/ AB02,
	/** AB03: CB10/CB07*/ AB03,
	/** AB04: CB11/CB07*/ AB04,
	/** AB05: CB12/CB07*/ AB05,
	/** AB06: CB13/CB07*/ AB06,
	/** AB07: CB14/CB07*/ AB07,
	/** AB08: CB15/CB07*/ AB08,
	/** AB09: CB16/CB07*/ AB09,
	/** AB10: CB17/CB07*/ AB10,
	/** AB11: CB18/CB07*/ AB11,
	/** AB12: CB19/CB07*/ AB12,
	/** AB13: CB20/CB07*/ AB13,
	/** AB14: CB21/CB07*/ AB14,
	/** AB15: CB22/CB07*/ AB15,
	/** AB16: CB23/CB07*/ AB16,

}
