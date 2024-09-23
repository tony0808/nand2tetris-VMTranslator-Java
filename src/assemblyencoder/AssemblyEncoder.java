package assemblyencoder;

import exception.InvalidPointerSegmentIndexException;
import exception.InvalidTempSegmentIndexException;

public class AssemblyEncoder {
	
	private static final int STATIC_SEGMENT_BASE_ADDRESS = 16;
	private static final int TEMP_SEGMENT_BASE_ADDRESS = 5;
	
	private String segmentIndex;
	
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
		
		encoded.append("//").append("push ").append("static ").append(segmentIndex).append("\n");
		encoded.append("@").append(STATIC_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(staticSegmentIndex).append("\n");
		encoded.append("A=D+A\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=M+1\n");
		
		return encoded.toString();
	}
	
	public String getPushTempEncoding() throws InvalidTempSegmentIndexException {
		StringBuilder encoded = new StringBuilder("");
		int index = Integer.valueOf(segmentIndex);
		
		if(index < 0 || index > 7) { throw new InvalidTempSegmentIndexException("Invalid temp segment index"); }
		
		encoded.append("//").append("push ").append("temp ").append(segmentIndex).append("\n");
		encoded.append("@").append(TEMP_SEGMENT_BASE_ADDRESS).append("\n");
		encoded.append("D=A\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("A=D+A\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=M+1\n");
		
		return encoded.toString();
	}
	
	public String getPushConstantEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("//").append("push ").append("constant ").append(segmentIndex).append("\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=A\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=M+1\n");
		
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
	
	private String getStandardPushSegmentEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("//").append("push ").append(getSegmentName(segment)).append(" ").append(segmentIndex).append("\n");
		encoded.append("@").append(segment).append("\n");
		encoded.append("D=M\n");
		encoded.append("@").append(segmentIndex).append("\n");
		encoded.append("D=D+A\n");
		encoded.append("@D\n");
		encoded.append("D=M\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=D\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=M+1\n");
		
		return encoded.toString();
	}
	
	private String _getPushPointerEncoding(String segment) {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("//").append("push ").append("pointer ").append(segment.equals("THIS")? "0" : "1").append("\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=").append(segment).append("\n");
		encoded.append("@SP\n");
		encoded.append("A=M\n");
		encoded.append("M=M+1\n");
		
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
}
