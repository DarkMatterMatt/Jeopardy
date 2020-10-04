package se206.quinzical.models.util;

import java.io.File;

/**
 * File browser that interacts with the OS file system
 */
public class FileBrowser {
	/**
	 * returns the files in the specified directory.
	 */
	public static File[] filesInDirectory(String dir) {
		File directory = new File(dir);
		return directory.isDirectory() ? directory.listFiles() : new File[]{};
	}
}
