package translator;

import fileio.FileWriter;
import assemblyencoder.ControlFlowEncoder;
import assemblyencoder.MemoryAccessEncoder;
import exception.InvalidPointerSegmentIndexException;
import exception.InvalidTempSegmentIndexException;
import fileio.FileReader;
import parser.Parser;
import utility.ErrorHandler;

public class Translator {
	
	private Parser parser;
	private FileReader reader;
	private FileWriter writer;
	private MemoryAccessEncoder memAccessEncoder;
	private ControlFlowEncoder ctrlFlowEncoder;
	
	public Translator(Parser parser, FileReader reader, FileWriter writer, MemoryAccessEncoder memAccessEncoder, ControlFlowEncoder ctrlFlowEncoder) {
		this.parser = parser;
		this.reader = reader;
		this.writer = writer;
		this.memAccessEncoder = memAccessEncoder;
		this.ctrlFlowEncoder = ctrlFlowEncoder;
	}
	
	public void translate() {
		parser.setCommand(reader.readLine());
		while(!parser.isCommandNull()) {
			switch(parser.getCommandType()) {
				case ARITHMETIC: 	handleArithmeticCommand(); break;
				case MEMORY_ACCESS: handleMemoryAccessCommand(); break;
				case CONTROL_FLOW:	handleControlFlowCommand();	break;
				case COMMENT: 		break;
				case WHITESPACE: 	break;
				default: 			ErrorHandler.exitErrorWithLineInfo("Uknown command", parser.getCommandLine());
			}
			parser.setCommand(reader.readLine());
		}
	}
	
	private void handleArithmeticCommand() {
		String encoding = null;

		switch(parser.getArithmeticType()) {
			case ADD: 	encoding = memAccessEncoder.getAddOperationEncoding(); 		break;
			case SUB: 	encoding = memAccessEncoder.getSubOperationEncoding(); 		break;
			case OR:  	encoding = memAccessEncoder.getOrOperationEncoding(); 		break;
			case NOT: 	encoding = memAccessEncoder.getNotOperationEncoding(); 		break;
			case AND: 	encoding = memAccessEncoder.getAndOperationEncoding(); 		break;
			case LT:  	encoding = memAccessEncoder.getLessThankEncoding();			break;
			case GT:	encoding = memAccessEncoder.getGreatherThanEncoding(); 		break;
			case NEG: 	encoding = memAccessEncoder.getNegativeOperationEncoding();  break;
			case EQ: 	encoding = memAccessEncoder.getEqualsEncoding();				break;
			default: 	ErrorHandler.exitErrorWithLineInfo("Uknown arithmetic type", parser.getCommandLine());
		}
		writer.writeLine(encoding);
	}
	
	private void handleMemoryAccessCommand() {
		switch(parser.getStackOperationType()) {
			case PUSH:	handleStackPushOperation(); break;
			case POP:	handleStackPopOperation(); 	break;
			default: 	ErrorHandler.exitErrorWithLineInfo("Uknown stack operation", parser.getCommandLine());
		}
	}
	
	private void handleStackPushOperation() {
		String encoding = null;
		String index = String.valueOf(parser.getSegmentIndex());
		
		memAccessEncoder.setSegmentIndex(index);
		
		switch(parser.getSegmentType()) {
			case LOCAL: 	encoding = memAccessEncoder.getPushLocalEncoding(); 	break;
			case ARGUMENT:  encoding = memAccessEncoder.getPushArgumentEncoding();	break;
			case THAT:	 	encoding = memAccessEncoder.getPushThatEncoding(); 		break;
			case THIS: 		encoding = memAccessEncoder.getPushThisEncoding(); 		break;
			case CONSTANT:  encoding = memAccessEncoder.getPushConstantEncoding(); 	break;
			case POINTER: 	encoding = handlePushPointerSegmentCase();		break;
			case STATIC:	encoding = handlePushStaticSegmentCase(); 		break;
			case TEMP: 	 	encoding = handlePushTempSegmentCase(); 		break;
			default:   		ErrorHandler.exitErrorWithLineInfo("Uknown segment type", parser.getCommandLine());
		}
		writer.writeLine(encoding);
	}
	
	private void handleStackPopOperation() {
		String encoding = null;
		String index = String.valueOf(parser.getSegmentIndex());
		
		memAccessEncoder.setSegmentIndex(index);
		
		switch(parser.getSegmentType()) {
			case LOCAL: 	encoding = memAccessEncoder.getPopLocalEncoding(); 		break;
			case ARGUMENT:  encoding = memAccessEncoder.getPopArgumentEncoding(); 	break;
			case THAT:	 	encoding = memAccessEncoder.getPopThatEncoding(); 		break;
			case THIS: 		encoding = memAccessEncoder.getPopThisEncoding(); 		break;
			case CONSTANT:  break;
			case POINTER: 	encoding = handlPopPointerSegmentCase(); 		break;
			case STATIC:	encoding = handlePopStaticSegmentCase(); 		break;
			case TEMP: 	 	encoding = handlePopTempSegmentCase();	 		break;
			default:   		ErrorHandler.exitErrorWithLineInfo("Uknown segment type", parser.getCommandLine());
		}
		writer.writeLine(encoding);
	}
	
	private String handlePushPointerSegmentCase() {
		String encoding = null;
		try {
			encoding = memAccessEncoder.getPushPointerEncoding(); 
		} catch(InvalidPointerSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlPopPointerSegmentCase() {
		String encoding = null;
		try {
			encoding = memAccessEncoder.getPopPointerEncoding();
		} catch(InvalidPointerSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlePushStaticSegmentCase() {
		String encoding = null;
		String fileName = writer.getFileNameWithoutExtension();
		
		encoding = memAccessEncoder.getPushStaticEncoding(fileName);
		
		return encoding;
	}
	
	private String handlePopStaticSegmentCase() {
		String encoding = null;
		String fileName = writer.getFileNameWithoutExtension();
		
		encoding = memAccessEncoder.getPopStaticEncoding(fileName);
		
		return encoding;
	}
	
	private String handlePushTempSegmentCase() {
		String encoding = null;
		try {
			encoding = memAccessEncoder.getPushTempEncoding();
		} catch (InvalidTempSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlePopTempSegmentCase() {
		String encoding = null;
		try {
			encoding = memAccessEncoder.getPopTempEncoding();
		} catch (InvalidTempSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private void handleControlFlowCommand() {
		String encoding = null;
		String label = parser.getLabel();
		
		ctrlFlowEncoder.setLabel(label);
		
		switch(parser.getControlFlowType()) {
			case LABEL:  encoding = ctrlFlowEncoder.getLabelEncoding(); 	break;
			case GOTO:   encoding = ctrlFlowEncoder.getGotoEncoding(); 		break;
			case IFGOTO: encoding = ctrlFlowEncoder.getIfGotoEncoding(); 	break;
		}
		
		writer.writeLine(encoding);
	}
}
