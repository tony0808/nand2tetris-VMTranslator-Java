package main;

import fileio.FileWriter;
import parser.Parser;
import translator.Translator;
import assemblyencoder.ControlFlowEncoder;
import assemblyencoder.FunctionFlowEncoder;
import assemblyencoder.MemoryAccessEncoder;
import fileio.FileReader;


public class Main {
	
	public static void main(String[] args) {
		FileReader reader = new FileReader("C:\\Users\\Admin\\eclipse-workspace\\nand2tetris-VMTranslator-Java\\src\\foo.vm");
		FileWriter writer = new FileWriter("C:\\\\Users\\\\Admin\\\\eclipse-workspace\\\\nand2tetris-VMTranslator-Java\\\\src\\\\foo.asm");
		
		Parser parser = new Parser();
		
		MemoryAccessEncoder memEncoder = new MemoryAccessEncoder();
		ControlFlowEncoder controlEncoder = new ControlFlowEncoder();
		FunctionFlowEncoder functionEncoder = new FunctionFlowEncoder();
		
		Translator translator = new Translator(parser, reader, writer, memEncoder, controlEncoder, functionEncoder);
		
		translator.translate();
		
		reader.close();
		writer.close();
	}
}
