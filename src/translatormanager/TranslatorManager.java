package translatormanager;
import java.io.File;

import fileio.FileReader;
import translator.Translator;

public class TranslatorManager {
	
	private File file;
	private Translator translator;
	
	public TranslatorManager(File file, Translator translator) {
		this.file = file;
		this.translator = translator;
	}
		
	public void translate() {
		if(file.isDirectory()) {
			translateDirectory();
		}
		else {
			translateSingleFile();
		}
	}
	
	private void translateDirectory() {
		File[] files;
		String absPath;
		FileReader reader = new FileReader();
		
		files = file.listFiles();
		for(File file : files) {
			absPath = file.getAbsolutePath();
			reader.setReader(absPath);
			translator.setReader(reader);
			translator.translate();
			reader.close();
		}
	}
	
	private void translateSingleFile() {
		FileReader reader = new FileReader();
		
		reader.setReader(file.getAbsolutePath());
		translator.setReader(reader);
		translator.translate();
		reader.close();
	}
}
