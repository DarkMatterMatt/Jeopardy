package se206.quinzical.models.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * helper class that returns random numbers
 *
 */
public class RandomNumberGenerator {
	/**
	 * Randomly select elements from a list
	 * @param list list to select elements from
	 * @param num number of elements to select
	 * @return a list containing a random subset of elements from the original list
	 */
	public static <T> List<T> getNRandom(List<T> list, int num) {
		List<T> copyOfList = new ArrayList<>(list);
		Collections.shuffle(copyOfList);
		return copyOfList.stream().limit(num).collect(Collectors.toList());
	}

	/*
	 * return five random numbers (from 0) from the given limit.
	 */
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

	/*
	 * if there is a duplicate in the list of integers, we want to remove the duplicate by using this function
	 */
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
