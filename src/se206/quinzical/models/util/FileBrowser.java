package se206.quinzical.models.util;

import java.io.File;

public class FileBrowser {
	public static File[] filesInDirectory(String dir) {
		File directory = new File(dir);
		return directory.isDirectory() ? directory.listFiles() : new File[]{};
	}
}
