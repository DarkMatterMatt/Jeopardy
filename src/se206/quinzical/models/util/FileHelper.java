package se206.quinzical.models.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * File helper that interacts with the OS file system
 */
public class FileHelper {
	/**
	 * Returns the files in the specified directory.
	 */
	public static File[] filesInDirectory(String dir) {
		File directory = new File(dir);
		return directory.isDirectory() ? directory.listFiles() : new File[]{};
	}

	/**
	 * Return list of strings read from a file
	 * @param file file to read from
	 */
	public static List<String> readFileOutputString(File file) {
		List<String> result = new ArrayList<>();

		try {
			Scanner scn = new Scanner(file);
			while (scn.hasNextLine()) {
				result.add(scn.nextLine());
			}
		}
		catch (FileNotFoundException e) {
			//
		}
		return result;
	}
}
