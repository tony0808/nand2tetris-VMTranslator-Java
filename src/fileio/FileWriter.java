package fileio;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {
	
	private PrintWriter writer;
	
	public FileWriter(String filePath) {
		try {
			writer = new PrintWriter(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writeLine(String line) {
		writer.println(line);
	}
	
	public void close() {
		writer.close();
	}
}
