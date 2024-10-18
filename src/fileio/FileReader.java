package fileio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileReader {
	
	private RandomAccessFile reader;
	private String filePath;
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	public void setReader() {
		try {
			reader = new RandomAccessFile(new File(filePath), "r");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String readLine() {
		String line = null;
		try {
			line = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
	
	public String getFileNameWithoutExtension() {
		String fileName;
		int dotIndex;
		
		fileName = filePath.substring(filePath.lastIndexOf('\\') + 1);
		dotIndex = fileName.lastIndexOf('.');
		
		fileName = fileName.substring(0, dotIndex);
		
		return fileName;
	}
	
	public void close() {
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
