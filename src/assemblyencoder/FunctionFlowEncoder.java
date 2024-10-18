package assemblyencoder;

public class FunctionFlowEncoder {
	
	private String funcName;
	private int funcNumOfVars;
	
	private int function_count = 0;
	
	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}
	
	public void setFuncNumOfVars(int funcNumOfVars) {
		this.funcNumOfVars = funcNumOfVars;
	}
	
	public String getBootstrapEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// Bootstrap code\n");
		encoded.append(stackPointerInitializationEncoding());
		setFuncName("Sys.init");
		encoded.append(getFunctionCallEncoding());
		
		return encoded.toString();
	}
	
	public String getFunctionCallEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// function call\n");
		encoded.append(pushReturnAddress());
		encoded.append(pushSegment("LCL"));
		encoded.append(pushSegment("ARG"));
		encoded.append(pushSegment("THIS"));
		encoded.append(pushSegment("THAT"));
		encoded.append(repositionArgument());
		encoded.append(repositionLocal());
		encoded.append(jumpToFunction());
		encoded.append(declareReturnAddressLabel());
		
		return encoded.toString();
	}
	
	public String getFunctionDefinitionEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// function definition\n");
		encoded.append(declareFunctionLabel());
		encoded.append(initializeLocalSegment());
		
		return encoded.toString();
	}
	
	public String getFunctionReturnEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// function return\n");
		encoded.append(saveEndframe());
		encoded.append(saveReturnAddress());
		encoded.append(popReturnValue());
		encoded.append(repositionStackPointer());
		encoded.append(restoreSegment("THAT", 1));
		encoded.append(restoreSegment("THIS", 2));
		encoded.append(restoreSegment("ARG", 3));
		encoded.append(restoreSegment("LCL", 4));
		encoded.append(gotoReturnAddress());
		
		return encoded.toString();
	}
	
	private String stackPointerInitializationEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@256\n");
		encoded.append("D=A\n");
		encoded.append("@SP\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(4);
		
		return encoded.toString();
	}
	
	private String pushReturnAddress() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@return").append(function_count).append(" // save return address\n");
		encoded.append("D=A\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	private String pushSegment(String arg) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@").append(arg).append(" // push segment\n");
		encoded.append("A=M\n");
		encoded.append("D=A\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(8);
		
		return encoded.toString();
	}
	
	private String repositionArgument() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@SP // reposition ARG\n");
		encoded.append("D=M\n");
		encoded.append("@5\n");
		encoded.append("D=D-A\n");
		encoded.append("@").append(funcNumOfVars).append("\n");
		encoded.append("D=D-A\n");
		encoded.append("@ARG\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(8);
		
		return encoded.toString();
	}
	
	private String repositionLocal() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@SP // reposition LCL\n");
		encoded.append("D=M\n");
		encoded.append("@LCL\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(4);
		
		return encoded.toString();
	}
	
	private String jumpToFunction() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@").append(funcName).append(" // goto func\n");
		encoded.append("0;JMP\n");
		
		incrementNumberOfCurrentCommands(2);
		
		return encoded.toString();
	}
	
	private String declareReturnAddressLabel() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("(return").append(function_count).append(")\n");
		
		incrementFunctionCount();
		
		return encoded.toString();
	}
	
	private String declareFunctionLabel() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("(").append(funcName).append(")\n");
		
		return encoded.toString();
	}
	
	private String initializeLocalSegment() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@").append(funcNumOfVars).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(MemoryAccessEncoder.numberOfCurrentCommands + 12).append("\n");
		encoded.append("D;JEQ\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=0\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		encoded.append("D=D-1\n");
		encoded.append("@").append(MemoryAccessEncoder.numberOfCurrentCommands + 4).append("\n");
		encoded.append("D;JGT\n");
		
		incrementNumberOfCurrentCommands(12);
		
		return encoded.toString();
	}
	
	private String saveEndframe() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@LCL // save endframe\n");
		encoded.append("D=M\n");
		encoded.append("@R13\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(4);
		
		return encoded.toString();
	}
	
	private String saveReturnAddress() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@R13 // save return address\n");
		encoded.append("D=M\n");
		encoded.append("@5\n");
		encoded.append("A=D-A\n");
		encoded.append("D=M\n");
		encoded.append("@R14\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	private String popReturnValue() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@SP // pop return value\n");
		encoded.append("M=M-1\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@ARG\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	private String repositionStackPointer() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@ARG // reposition sp\n");
		encoded.append("D=M\n");
		encoded.append("D=D+1\n");
		encoded.append("@SP\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(5);
		
		return encoded.toString();
	}
	
	private String restoreSegment(String segment, int index) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@R13 // restore segment\n");
		encoded.append("D=M\n");
		encoded.append("@").append(index).append("\n");
		encoded.append("A=D-A\n");
		encoded.append("D=M\n");
		encoded.append("@").append(segment).append("\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	private String gotoReturnAddress() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("@R14 // goto return address\n");
		encoded.append("A=M\n");
		encoded.append("0;JMP\n");
		
		incrementNumberOfCurrentCommands(3);
		
		return encoded.toString();
	}
	
	private void incrementNumberOfCurrentCommands(int inc) {
		MemoryAccessEncoder.numberOfCurrentCommands += inc;
	}
	
	private void incrementFunctionCount() {
		function_count++;
	}
}
