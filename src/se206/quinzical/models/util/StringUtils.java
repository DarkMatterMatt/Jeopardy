package se206.quinzical.models.util;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtils {
	/**
	 * Pattern used in {@link #stripAccents(String)}.
	 */
	private static final Pattern STRIP_ACCENTS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+"); //$NON-NLS-1$

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

	/**
	 * <p>Removes diacritics (~= accents) from a string. The case will not be altered.</p>
	 * <p>For instance, '&agrave;' will be replaced by 'a'.</p>
	 * <p>Note that ligatures will be left as is.</p>
	 *
	 * <pre>
	 * StringUtils.stripAccents(null)                = null
	 * StringUtils.stripAccents("")                  = ""
	 * StringUtils.stripAccents("control")           = "control"
	 * StringUtils.stripAccents("&eacute;clair")     = "eclair"
	 * </pre>
	 *
	 * @param input String to be stripped
	 * @return input text with diacritics removed
	 * @link https://github.com/apache/commons-lang/blob/c1c4a7535bfaa377eb7a72de7b7e36a04f33a2f8/src/main/java/org/apache/commons/lang3/StringUtils.java#L8221
	 */
	/*
	 * Licensed from the Apache Software Foundation (ASF) under the Apache License 2.0.
	 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.
	 */
	public static String stripAccents(final String input) {
		if (input == null) {
			return null;
		}
		final StringBuilder decomposed = new StringBuilder(Normalizer.normalize(input, Normalizer.Form.NFD));
		for (int i = 0; i < decomposed.length(); i++) {
			if (decomposed.charAt(i) == '\u0141') {
				decomposed.setCharAt(i, 'L');
			}
			else if (decomposed.charAt(i) == '\u0142') {
				decomposed.setCharAt(i, 'l');
			}
		}
		return STRIP_ACCENTS_PATTERN.matcher(decomposed).replaceAll("");
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
