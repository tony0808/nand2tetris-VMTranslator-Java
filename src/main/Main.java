package main;

import fileio.FileWriter;
import parser.Parser;
import translator.Translator;
import translatormanager.TranslatorManager;

import java.io.File;

import assemblyencoder.ControlFlowEncoder;
import assemblyencoder.FunctionFlowEncoder;
import assemblyencoder.MemoryAccessEncoder;
import fileio.FileReader;


public class Main {
	
	public static void main(String[] args) {
		
		File file = new File("C:\\Users\\Admin\\eclipse-workspace\\nand2tetris-VMTranslator-Java\\src\\vmfunctions");
		
		FileWriter writer = new FileWriter("C:\\\\Users\\\\Admin\\\\eclipse-workspace\\\\nand2tetris-VMTranslator-Java\\\\src\\\\foo.asm");
		
		Parser parser = new Parser();
		MemoryAccessEncoder memEncoder = new MemoryAccessEncoder();
		ControlFlowEncoder controlEncoder = new ControlFlowEncoder();
		FunctionFlowEncoder functionEncoder = new FunctionFlowEncoder();
		
		Translator translator = new Translator(parser, writer, memEncoder, controlEncoder, functionEncoder);
		TranslatorManager translatorManager = new TranslatorManager(file, translator);
		
		translatorManager.translate();

		writer.close();
	}
}
