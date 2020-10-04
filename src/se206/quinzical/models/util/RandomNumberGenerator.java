package se206.quinzical.models.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class that returns random numbers
 */
public class RandomNumberGenerator {
	/**
	 * Randomly select elements from a list
	 *
	 * @param list list to select elements from
	 * @param num  number of elements to select
	 * @return a list containing a random subset of elements from the original list
	 */
	public static <T> List<T> getNRandom(List<T> list, int num) {
		List<T> copyOfList = new ArrayList<>(list);
		Collections.shuffle(copyOfList);
		return copyOfList.stream().limit(num).collect(Collectors.toList());
	}
}
