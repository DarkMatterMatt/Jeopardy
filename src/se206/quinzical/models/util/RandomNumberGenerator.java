package se206.quinzical.models.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomNumberGenerator {
	
	public static List<Integer> takeFive(int range){
		Random rand = new Random();
		
		List<Integer> result = new ArrayList<Integer>();
		
		for(int i = 0; i<5; i++) {
			int n =rand.nextInt(range);
			while(result.indexOf(n) != -1) {
				n = rand.nextInt(range);
			}
			result.add(n);
			
		}
		
		return result;
		
	}
	
	public static List<Integer> giveMeDifferentRandomNumber(List<Integer> list, Integer issue, Integer range){
		Random rand = new Random();
		
		int unwanted = issue; //note the unwanted number
		list.remove(issue);
		
		int n =rand.nextInt(range);
		while(n == unwanted) {
			n = rand.nextInt(range);
		}
		
		list.add(n);
		
		return list;
		
	}

}
