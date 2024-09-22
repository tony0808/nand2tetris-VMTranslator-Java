package main;

import parser.Parser;

import fileio.FileReader;


public class Main {
	
	public static void main(String[] args) {
		FileReader reader = new FileReader("C:\\Users\\Admin\\eclipse-workspace\\nand2tetris-VMTranslator-Java\\src\\foo.vm");
		
		Parser p = new Parser();
		
		p.setCommand(reader.readLine());
		
		while(!p.isCommandNull()) {
			switch(p.getCommandType()) {
				case ARITHMETIC:
					System.out.print("arithm: "); 
					switch(p.getArithmeticType()) {
						case ADD: System.out.println("and");break;
						case SUB: System.out.println("sub");break;
						case OR: System.out.println("or");break;
						case NOT: System.out.println("not");break;
						case AND:System.out.println("and"); break;
						case LT: System.out.println("lt");break;
						case GT: System.out.println("gt");break;
						case NEQ:System.out.println("neg"); break;
						case EQ: System.out.println("eq");break;
					}
					
					break;
				case MEMORY_ACCESS: 
					System.out.println("mem acc: " + p.getMemoryOperation() + " , " + p.getMemorySegment() + " , " + p.getSegmentIndex());
					break;
				case COMMENT: System.out.println("comm"); break;
				case WHITESPACE: System.out.println("whitespace"); break;
				default: System.out.println("uknown command"); System.exit(1);
			}
			p.setCommand(reader.readLine());
		}
		
		reader.close();
	}
}
