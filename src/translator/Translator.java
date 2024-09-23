package translator;

import fileio.FileWriter;
import assemblyencoder.AssemblyEncoder;
import exception.InvalidPointerSegmentIndexException;
import exception.InvalidTempSegmentIndexException;
import fileio.FileReader;
import parser.Parser;
import utility.ErrorHandler;

public class Translator {
	
	private Parser parser;
	private FileReader reader;
	private FileWriter writer;
	private AssemblyEncoder encoder;
	
	public Translator(Parser parser, FileReader reader, FileWriter writer, AssemblyEncoder encoder) {
		this.parser = parser;
		this.reader = reader;
		this.writer = writer;
		this.encoder = encoder;
	}
	
	public void translate() {
		parser.setCommand(reader.readLine());
		while(!parser.isCommandNull()) {
			switch(parser.getCommandType()) {
				case ARITHMETIC: 	handleArithmeticCommand(); break;
				case MEMORY_ACCESS: handleMemoryAccessCommand(); break;
				case COMMENT: 		break;
				case WHITESPACE: 	break;
				default: 			ErrorHandler.exitErrorWithLineInfo("Uknown command", parser.getCommandLine());
			}
			parser.setCommand(reader.readLine());
		}
	}
	
	private void handleArithmeticCommand() {
		switch(parser.getArithmeticType()) {
			case ADD: writer.writeLine("and");break;
			case SUB: writer.writeLine("sub");break;
			case OR: writer.writeLine("or");break;
			case NOT: writer.writeLine("not");break;
			case AND:writer.writeLine("and"); break;
			case LT: writer.writeLine("lt");break;
			case GT: writer.writeLine("gt");break;
			case NEQ:writer.writeLine("neg"); break;
			case EQ: writer.writeLine("eq");break;
			default: ErrorHandler.exitErrorWithLineInfo("Uknown arithmetic type", parser.getCommandLine());
		}
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
		
		encoder.setSegmentIndex(index);
		
		switch(parser.getSegmentType()) {
			case LOCAL: 	encoding = encoder.getPushLocalEncoding(); 		break;
			case ARGUMENT:  encoding = encoder.getPushArgumentEncoding();	break;
			case THAT:	 	encoding = encoder.getPushThisEncoding(); 		break;
			case THIS: 		encoding = encoder.getPushThatEncoding(); 		break;
			case CONSTANT:  encoding = encoder.getPushConstantEncoding(); 	break;
			case POINTER: 	encoding = handlePointerSegmentCase();			break;
			case STATIC:	encoding = handleStaticSegmentCase(); 			break;
			case TEMP: 	 	encoding = handleTempSegmentCase(); 			break;
			default:   		ErrorHandler.exitErrorWithLineInfo("Uknown segment type", parser.getCommandLine());
		}
		writer.writeLine(encoding);
	}
	
	private void handleStackPopOperation() {
		switch(parser.getSegmentType()) {
			case ARGUMENT:  break;
			case CONSTANT:  break;
			case LOCAL: 	break;
			case POINTER: 	break;
			case STATIC:	break;
			case TEMP: 	 	break;
			case THAT:	 	break;
			case THIS: 		break;
			default:   		ErrorHandler.exitErrorWithLineInfo("Uknown segment type", parser.getCommandLine());
		}
	}
	
	private String handlePointerSegmentCase() {
		String encoding = null;
		try {
			encoding = encoder.getPushPointerEncoding(); 
		} catch(InvalidPointerSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handleStaticSegmentCase() {
		String encoding = null;
		String fileName = writer.getFileNameWithoutExtension();
		
		encoding = encoder.getPushStaticEncoding(fileName);
		
		return encoding;
	}
	
	private String handleTempSegmentCase() {
		String encoding = null;
		try {
			encoding = encoder.getPushTempEncoding();
		} catch (InvalidTempSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
}
