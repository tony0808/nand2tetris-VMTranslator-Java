package parser;

import specs.CommandSpecManager;
import specs.CommandSpecManager.ArithmeticType;
import specs.CommandSpecManager.CommandType;
import specs.CommandSpecManager.ControlFlowType;
import specs.CommandSpecManager.SegmentType;
import specs.CommandSpecManager.StackOperationType;
import utility.Utility;

public class Parser {
	
	private String command;
	private long commandLine;
	
	private String memOperation;
	private String memSegment;
	private int segmentIndex;
	 
	private String ctrlFlowOperation;
	private String label;
	
	public void setCommand(String command) {
		commandLine++;
		if(command != null) {
			this.command = preprocessCommand(command);
		}
		else {
			this.command = null;
		}
	}
	
	public boolean isCommandNull() {
		return command == null;
	}
	
	public CommandType getCommandType() {
		
		if(isArithmetic()) { return CommandType.ARITHMETIC; }
		if(isMemoryAccess()) { return CommandType.MEMORY_ACCESS; }
		if(isControlFlow()) { return CommandType.CONTROL_FLOW; }
		if(isComment()) { return CommandType.COMMENT; }
		if(isWhiteSpace()) { return CommandType.WHITESPACE; }
		
		return CommandType.UKNOWN;
	}
	
	public String getMemoryOperation() {
		return memOperation;
	}
	
	public String getMemorySegment() {
		return memSegment;
	}
	
	public int getSegmentIndex() {
		return segmentIndex;
	}
	
	public String getControlFlowOperation() {
		return ctrlFlowOperation;
	}
	
	public String getLabel() {
		return label;
	}
	
	public ArithmeticType getArithmeticType() {
		return CommandSpecManager.getArithmeticType(command);
	}
	
	public SegmentType getSegmentType() {
		return CommandSpecManager.getSegmentType(memSegment);
	}
	
	public StackOperationType getStackOperationType() {
		return CommandSpecManager.getStackOperationType(memOperation);
	}
	
	public ControlFlowType getControlFlowType() {
		return CommandSpecManager.getControlFlowType(ctrlFlowOperation);
	}
	
	public long getCommandLine() {
		return commandLine;
	}
	
	private boolean isArithmetic() {
		return CommandSpecManager.isArithmeticOperationValid(command);
	}
	
	private boolean isMemoryAccess() {
		boolean isMemoryAccess = false;
	    String[] parts = command.split(" ");

	    if (parts.length != 3) {
	        return false; 
	    }

	    String operation = parts[0];
	    String segment = parts[1];
	    String index = parts[2];
	    
	    isMemoryAccess = CommandSpecManager.isMemoryAccessOperationValid(operation) &&
	    		CommandSpecManager.isMemorySegmentValid(segment) &&
	           Utility.isNumeric(index);
	   
	    if(isMemoryAccess) {
	    	memOperation = operation;
		    memSegment = segment;
		    segmentIndex = Integer.valueOf(index);
	    }
	    
	    return isMemoryAccess;
	}
	
	private boolean isControlFlow() {
		boolean isControlFlow = false;
		String[] parts;
		
		parts = command.split(" ");
		
		if(parts.length != 2) {
			return false;
		}
		
		ctrlFlowOperation = parts[0];
		label = parts[1];
		
		isControlFlow = CommandSpecManager.isControlFlowOperationValid(ctrlFlowOperation);
		
		return isControlFlow;
	}
	
	private boolean isComment() {
		return (command.length() >= 2 && command.substring(0, 2).equals("//"));
	}
	
	private boolean isWhiteSpace() {
		return command.length() == 0;
	}
	
	private String preprocessCommand(String command) {
		String preprocessed;
		
		preprocessed = command.trim();
		preprocessed = preprocessed.split("//")[0].trim();
		
		return preprocessed;
	}
}


















