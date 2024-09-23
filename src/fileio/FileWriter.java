package fileio;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileWriter {
	
	private PrintWriter writer;
	private String filePath;
	
	public FileWriter(String filePath) {
		this.filePath = filePath;
		try {
			writer = new PrintWriter(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getFileNameWithoutExtension() {
		String fileName;
		int dotIndex;
		
		fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);
		dotIndex = fileName.lastIndexOf('.');
		
		fileName = fileName.substring(0, dotIndex);
		
		return fileName;
	}
	
	public void writeLine(String line) {
		writer.println(line);
	}
	
	public void close() {
		writer.close();
	}
}
