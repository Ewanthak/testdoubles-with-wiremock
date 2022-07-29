package utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileReaderUtil {
	private static final String RESOURCE_DIR = "src/test/resources/requestBodyFiles/";

	public static String readResourceContent(String filename) throws IOException {
		File file = new File(RESOURCE_DIR + filename);
		return FileUtils.readFileToString(file, "UTF-8");
	}

	public static void clearDirectory(File directoryName) throws IOException {
		FileUtils.cleanDirectory(directoryName);
	}

}
