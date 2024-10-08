package assemblyencoder;

public class ControlFlowEncoder {
	
	private String label;
	
	public ControlFlowEncoder() {
		
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabelEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("(").append(label).append(")\n");
		
		return encoded.toString();
	}
	
	public String getGotoEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// unconditional jump\n");
		encoded.append("@").append(label).append("\n");
		encoded.append("0;JMP\n");
		
		incrementNumberOfCurrentCommands(2);
		
		return encoded.toString();
	}
	
	public String getIfGotoEncoding() {
		StringBuilder encoded = new StringBuilder("");
		
		encoded.append("// conditional jump\n");
		encoded.append("@SP\n");
		encoded.append("M=M-1\n");
		encoded.append("A=M\n");
		encoded.append("D=M\n");
		encoded.append("@").append(MemoryAccessEncoder.numberOfCurrentCommands + 8).append("\n");
		encoded.append("D;JEQ\n");
		encoded.append("@").append(label).append("\n");
		encoded.append("0;JMP\n");
		
		incrementNumberOfCurrentCommands(8);
		
		return encoded.toString();
	}
	
	private void incrementNumberOfCurrentCommands(int inc) {
		MemoryAccessEncoder.numberOfCurrentCommands += inc;
	} 
}
