package assemblyencoder;

import java.util.HashMap;

import exception.InvalidPointerSegmentIndexException;
import exception.InvalidTempSegmentIndexException;

public class MemoryAccessEncoder {
	
	private static final int STATIC_SEGMENT_BASE_ADDRESS = 16;
	private static final int TEMP_SEGMENT_BASE_ADDRESS = 5;
	private static final int PTR_SEGMENT_BASE_ADDRESS = 3;
	
	private String segmentIndex;
	
	private HashMap<String, String> binaryOperationsMap;
	private HashMap<String, String> binaryComparisonMap;
	
	protected static int numberOfCurrentCommands = 0;
	
	public MemoryAccessEncoder() {
		initializeBinaryOperationsMap();
		initializeBinaryComparisonMap();
	}
	
	public void setSegmentIndex(String segmentIndex) {
		this.segmentIndex = segmentIndex;
	}
	
	public String getPushLocalEncoding() {
		return getStandardPushSegmentEncoding("LCL");
	}
	
	public String getPushArgumentEncoding() {
		return getStandardPushSegmentEncoding("ARG");
	}
	
	public String getPushThisEncoding() {
		return getStandardPushSegmentEncoding("THIS");
	}
	
	public String getPushThatEncoding() {
		return getStandardPushSegmentEncoding("THAT");
	}
	
	public String getPushStaticEncoding(String fileName) {
		StringBuilder encoded = new StringBuilder("");
		String staticSegmentIndex = fileName + "." + segmentIndex;	
		
		encoded.append("// ").append("push ").append("static ").append(segmentIndex).append("\n");
		encoded.append("@").append(STATIC_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(staticSegmentIndex).append("\n");
		encoded.append("A=D+A\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(10);
		
		return encoded.toString();
	}
	
	public String getPushTempEncoding() throws InvalidTempSegmentIndexException {
		StringBuilder encoded = new StringBuilder("");
		int index = Integer.valueOf(segmentIndex);
		
		if(index < 0 || index > 7) { throw new InvalidTempSegmentIndexException("Invalid temp segment index"); }
		
		encoded.append("// ").append("push ").append("temp ").append(segmentIndex).append("\n");
		encoded.append("@").append(TEMP_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("A=D+A\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(10);
		
		return encoded.toString();
	}
	
	public String getPushConstantEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("push ").append("constant ").append(segmentIndex).append("\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=A\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	public String getPushPointerEncoding() throws InvalidPointerSegmentIndexException {
		String encoded = null;
		switch(Integer.valueOf(segmentIndex)) {
			case 0: encoded = _getPushPointerEncoding("THIS"); break;
			case 1: encoded = _getPushPointerEncoding("THAT"); break;
			default: throw new InvalidPointerSegmentIndexException("Invalid pointer segment index");
		}
		return encoded;
	}
	
	public String getPopLocalEncoding() {
		return getStandardPopSegmentEncoding("LCL");
	}
	
	public String getPopArgumentEncoding() {
		return getStandardPopSegmentEncoding("ARG");
	}
	
	public String getPopThisEncoding() {
		return getStandardPopSegmentEncoding("THIS");
	}
	
	public String getPopThatEncoding() {
		return getStandardPopSegmentEncoding("THAT");
	}
	
	public String getPopStaticEncoding(String fileName) {
		StringBuilder encoded = new StringBuilder("");
		String staticSegmentIndex = fileName + "." + segmentIndex;
		
		encoded.append("// ").append("pop ").append("static ").append(segmentIndex).append("\n");
		encoded.append("@").append(STATIC_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(staticSegmentIndex).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("@R13\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@R13\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(14);
		
		return encoded.toString();
	}
	
	public String getPopPointerEncoding() throws InvalidPointerSegmentIndexException {
		String encoded = null;
		switch(Integer.valueOf(segmentIndex)) {
			case 0: encoded = _getPopPointerEncoding("THIS"); break;
			case 1: encoded = _getPopPointerEncoding("THAT"); break;
			default: throw new InvalidPointerSegmentIndexException("Invalid pointer segment index");
		}
		return encoded;
	}
	
	public String getPopTempEncoding() throws InvalidTempSegmentIndexException {
		StringBuilder encoded = new StringBuilder("");
		int index = Integer.valueOf(segmentIndex);
		
		if(index < 0 || index > 7) { throw new InvalidTempSegmentIndexException("Invalid temp segment index"); }
		
		encoded.append("// ").append("pop ").append("temp ").append(segmentIndex).append("\n");
		encoded.append("@").append(TEMP_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("@R13\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@R13\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(14);
		
		return encoded.toString();
	}
	
	public String getAddOperationEncoding() {
		return getBinaryOperationEncoding("ADD");
	}
	
	public String getSubOperationEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("sub\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("A=M\n");
		encoded.append("D=A-D\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(12);
		
		return encoded.toString();
	}
	
	public String getAndOperationEncoding() {
		return getBinaryOperationEncoding("AND");
	}
	
	public String getOrOperationEncoding() {
		return getBinaryOperationEncoding("OR");
	}
	
	public String getNotOperationEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("not").append("\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("D=M\n");
		encoded.append("M=!D\n");
		
		incrementNumberOfCurrentCommands(4);
		
		return encoded.toString();
	}
	
	public String getNegativeOperationEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("not").append("\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("D=M\n");
		encoded.append("M=-D\n");
		
		incrementNumberOfCurrentCommands(4);
		
		return encoded.toString();
	}
	
	public String getEqualsEncoding() {
		return getComparisonEncoding("eq");
	}
	
	public String getGreatherThanEncoding() {
		return getComparisonEncoding("gt");
	}
	
	public String getLessThankEncoding() {
		return getComparisonEncoding("lt");
	}
	
	private String getComparisonEncoding(String cond) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append(cond).append("\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("A=M\n");
		
		if(binaryComparisonMap.get(cond).equals("D;JGT") || binaryComparisonMap.get(cond).equals("D;JLT")) {
			encoded.append("A=-A\n");
			encoded.append("D=-D\n");

			incrementNumberOfCurrentCommands(2);
		}
		
		encoded.append("D=D-A\n");
		encoded.append("@").append(numberOfCurrentCommands + 16).append("\n");
		encoded.append(binaryComparisonMap.get(cond)).append("\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("M=0\n");
		encoded.append("@").append(numberOfCurrentCommands + 19).append("\n");
		encoded.append("0;JMP\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append("M=-1\n");
		
		incrementNumberOfCurrentCommands(19);
		
		return encoded.toString();
	}
	
	private String getBinaryOperationEncoding(String op) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append(op.toLowerCase()).append("\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M-1\n");
		encoded.append(binaryOperationsMap.get(op)).append("\n");
		
		incrementNumberOfCurrentCommands(8);
		
		return encoded.toString();
	}
	
	private String getStandardPushSegmentEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("push ").append(getSegmentName(segment)).append(" ").append(segmentIndex).append("\n");
		encoded.append("@").append(segment).append("\n");
		encoded.append("D=M\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("A=D\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(11);
		
		return encoded.toString();
	}
	
	private String _getPushPointerEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		int index = segment.equals("THIS") ? 0 : 1;
		
		encoded.append("// ").append("push ").append("pointer ").append(segment.equals("THIS")? "0" : "1").append("\n");
		encoded.append("@").append(PTR_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(index).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("A=D\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M+1\n");
		
		incrementNumberOfCurrentCommands(11);
		
		return encoded.toString();
	}
	
	private String _getPopPointerEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("pop ").append("pointer ").append(segment.equals("THIS")? "0" : "1").append("\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@").append(segment).append("\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(7);
		
		return encoded.toString();
	}
	
	private String getStandardPopSegmentEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// ").append("pop ").append(getSegmentName(segment)).append(" ").append(segmentIndex).append("\n");
		encoded.append("@").append(segment).append("\n");
		encoded.append("D=M\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("@R13\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@R13\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		
		incrementNumberOfCurrentCommands(14);
		
		return encoded.toString();
	}
	
	private String getSegmentName(String segment) {
		
		if(segment.equals("LCL")) {
			segment = "LOCAL";
		}
		else
		if(segment.equals("ARG")) {
			segment = "ARGUMENT";
		}
		
		return segment.toLowerCase();
	}
	
	private void initializeBinaryOperationsMap() {
		binaryOperationsMap = new HashMap<>();
		
		binaryOperationsMap.put("ADD", "M=D+M");
		binaryOperationsMap.put("AND", "M=D&M");
		binaryOperationsMap.put("OR",  "M=D|M");
	}

	private void initializeBinaryComparisonMap() {
		binaryComparisonMap = new HashMap<>();
		
		binaryComparisonMap.put("eq", "D;JEQ");
		binaryComparisonMap.put("gt", "D;JGT");
		binaryComparisonMap.put("lt", "D;JLT");
	}
	
	private void incrementNumberOfCurrentCommands(int inc) {
		numberOfCurrentCommands += inc;
	}
}
