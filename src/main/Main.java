package main;

import fileio.FileWriter;
import parser.Parser;
import translator.Translator;
import assemblyencoder.AssemblyEncoder;
import fileio.FileReader;


public class Main {
	
	public static void main(String[] args) {
		FileReader reader = new FileReader("C:\\Users\\Admin\\eclipse-workspace\\nand2tetris-VMTranslator-Java\\src\\foo.vm");
		FileWriter writer = new FileWriter("C:\\\\Users\\\\Admin\\\\eclipse-workspace\\\\nand2tetris-VMTranslator-Java\\\\src\\\\foo.asm");
		
		Translator translator = new Translator(new Parser(), reader, writer, new AssemblyEncoder());
		
		translator.translate();
		
		reader.close();
		writer.close();
	}
}
