package se206.quinzical.models.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * The only method in this class returns the list of strings read from a file
 */
public class MyScanner {
	public static List<String> readFileOutputString(File f){
		List<String> result = new ArrayList<String>();
		Scanner scn;
		
		try {
			scn = new Scanner(f);
			
			while(scn.hasNextLine()) {
				result.add(scn.nextLine());
			}			
		} catch (FileNotFoundException e) {}
		
		return result;
	}

}
