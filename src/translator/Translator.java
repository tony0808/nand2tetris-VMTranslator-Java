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
		
		encoder.setSegmentIndex(index);
		
		switch(parser.getSegmentType()) {
			case LOCAL: 	encoding = encoder.getPopLocalEncoding(); 		break;
			case ARGUMENT:  encoding = encoder.getPopArgumentEncoding(); 	break;
			case THAT:	 	encoding = encoder.getPopThatEncoding(); 		break;
			case THIS: 		encoding = encoder.getPopThisEncoding(); 		break;
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
			encoding = encoder.getPushPointerEncoding(); 
		} catch(InvalidPointerSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlPopPointerSegmentCase() {
		String encoding = null;
		try {
			encoding = encoder.getPopPointerEncoding();
		} catch(InvalidPointerSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlePushStaticSegmentCase() {
		String encoding = null;
		String fileName = writer.getFileNameWithoutExtension();
		
		encoding = encoder.getPushStaticEncoding(fileName);
		
		return encoding;
	}
	
	private String handlePopStaticSegmentCase() {
		String encoding = null;
		String fileName = writer.getFileNameWithoutExtension();
		
		encoding = encoder.getPopStaticEncoding(fileName);
		
		return encoding;
	}
	
	private String handlePushTempSegmentCase() {
		String encoding = null;
		try {
			encoding = encoder.getPushTempEncoding();
		} catch (InvalidTempSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
	
	private String handlePopTempSegmentCase() {
		String encoding = null;
		try {
			encoding = encoder.getPopTempEncoding();
		} catch (InvalidTempSegmentIndexException e) {
			ErrorHandler.exitErrorWithLineInfo(e.getMessage(), parser.getCommandLine());
		}
		return encoding;
	}
}
