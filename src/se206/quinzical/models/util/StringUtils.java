package se206.quinzical.models.util;

import java.text.Normalizer;

public class StringUtils {
	private StringUtils() {
	}

	public static int countOccurrences(String haystack, char needle) {
		int count = 0;
		for (int i = 0; i < haystack.length(); i++) {
			if (haystack.charAt(i) == needle) {
				count++;
			}
		}
		return count;
	}

	public static String normalize(String text) {
		return Normalizer.normalize(text, Normalizer.Form.NFC).trim();
	}

	public static String stripTextInParentheses(String text) {
		String newText = text.replaceAll("\\([^()]*\\)", "");
		while (newText.length() != text.length()) {
			text = newText;
			newText = text.replace("\\([^()]*\\)", "");
		}
		return newText;
	}
}
