package se206.quinzical.models.util;

import java.io.File;

public class FileBrowser {
	public static File[] filesInDirectory(String dir) {
		File directory = new File(dir);
		File[] categories = directory.listFiles();
		return categories;
		
	}

}
