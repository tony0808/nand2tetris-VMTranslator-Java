package specs;

import java.util.Set;

public class CommandSpecManager {
	
	public enum CommandType {
		ARITHMETIC,
		MEMORY_ACCESS,
		COMMENT,
		WHITESPACE,
		UKNOWN
	}
	
	public enum ArithmeticType {
		ADD,
		SUB,
		GT,
		LT,
		NEQ,
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
	
	private static final Set<String> ARITHM_OPERATIONS = Set.of("add", "sub", "gt", "lt", "neg", "eq", "and", "not", "or");
	private static final Set<String> MEMACC_OPERATIONS = Set.of("push", "pop");
	private static final Set<String> MEMORY_SEGMENTS = Set.of("local", "argument", "this", "that", "temp", "pointer", "constant", "static");
	
	public static ArithmeticType getArithmeticType(String op) {
	    try {
	        return ArithmeticType.valueOf(op.toUpperCase());
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
	
	public static boolean isMemorySegmentValid(String seg) {
		return MEMORY_SEGMENTS.contains(seg);
	}
}