package specs;

import java.util.Set;

public class CommandSpecManager {
	
	public enum CommandType {
		ARITHMETIC,
		MEMORY_ACCESS,
		CONTROL_FLOW,
		FUNCTION_FLOW,
		COMMENT,
		WHITESPACE,
		UKNOWN
	}
	
	public enum ArithmeticType {
		ADD,
		SUB,
		GT,
		LT,
		NEG,
		AND,
		NOT,
		OR,
		EQ
	}
	
	public enum SegmentType {
		LOCAL,
		ARGUMENT,
		STATIC,
		TEMP,
		POINTER,
		CONSTANT,
		THIS,
		THAT
	}
	
	public enum StackOperationType {
		PUSH,
		POP
	}
	
	public enum ControlFlowType {
		LABEL,
		GOTO,
		IFGOTO,
	}
	
	public enum FunctionFlowType {
		CALL,
		RETURN,
		FUNCTION,
	}
	
	private static final Set<String> ARITHM_OPERATIONS 			= Set.of("add", "sub", "gt", "lt", "neg", "eq", "and", "not", "or");
	private static final Set<String> MEMACC_OPERATIONS 			= Set.of("push", "pop");
	private static final Set<String> CONTROL_FLOW_OPERATIONS 	= Set.of("label", "goto", "if-goto");
	private static final Set<String> FUNCTION_FLOW_OPERATIONS 	= Set.of("function", "call", "return");
	private static final Set<String> MEMORY_SEGMENTS 			= Set.of("local", "argument", "this", "that", "temp", "pointer", "constant", "static");
	
	public static ArithmeticType getArithmeticType(String op) {
	    try {
	        return ArithmeticType.valueOf(op.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        return null;
	    }
	}
	
	public static ControlFlowType getControlFlowType(String op) {
		try {
			if(op.equals("if-goto")) { op = "ifgoto"; }
			return ControlFlowType.valueOf(op.toUpperCase());
		} catch(IllegalArgumentException e) {
			return null;
		}
	}
	
	public static FunctionFlowType getFunctionFlowType(String op) {
		try {
	        return FunctionFlowType.valueOf(op.toUpperCase());
	    } catch (IllegalArgumentException e) {
	        return null;
	    }
	}
	
	public static StackOperationType getStackOperationType(String op) {
		try {
			return StackOperationType.valueOf(op.toUpperCase());
		} catch (IllegalArgumentException e) {
	        return null;
	    }
	}
	
	public static SegmentType getSegmentType(String segment) {
		try {
			return SegmentType.valueOf(segment.toUpperCase());
		} catch (IllegalArgumentException e) {
	        return null;
	    }
	}
	
	public static String getSegmentAsString(SegmentType segment) {
		return segment.name().toLowerCase();
	}
	
	
	public static boolean isArithmeticOperationValid(String op) {
		return ARITHM_OPERATIONS.contains(op);
	}
	
	public static boolean isMemoryAccessOperationValid(String op) {
		return MEMACC_OPERATIONS.contains(op);
	}
	
	public static boolean isControlFlowOperationValid(String op) {
		return CONTROL_FLOW_OPERATIONS.contains(op);
	}
	
	public static boolean isFunctionFlowOperationValid(String op) {
		return FUNCTION_FLOW_OPERATIONS.contains(op);
	}
	
	public static boolean isMemorySegmentValid(String seg) {
		return MEMORY_SEGMENTS.contains(seg);
	}
}