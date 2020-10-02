package se206.quinzical.models;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import se206.quinzical.models.util.StringUtils;

public class Question {
	private final List<String> _answer;
	private final String _question;
	private transient Category _category;
	private Status _status = Status.UNATTEMPTED;
	private int _value = -1;
	private int _numAttempted;

	/**
	 * Copy constructor, create a copy of a Question
	 */
	public Question(Question origQuestion) {
		_answer = origQuestion._answer;
		_question = origQuestion._question;
		_category = origQuestion._category;
		_status = origQuestion._status;
		_value = origQuestion._value;
		_numAttempted = origQuestion._numAttempted;
	}

	public Question(Category category, String question, String ...answer) {
		_answer = Arrays.asList(answer);
		_category = category;
		_question = question;
	}

	public Question(String raw, Category category) {
		_category = category;

		String[] processed = raw.split("\\|");
		if(processed.length <= 1) {
			throw new IllegalArgumentException("format of a question is: <Question>|<Answer1>|<Answer2>...");
		}

		_question = processed[0].trim();
		String[] answers = Arrays.copyOfRange(processed, 1, processed.length);
		List<String> answersProcessed = new ArrayList<String>();
		for (String ans : answers) {
			String tmp = ans.trim();
			if (!tmp.isEmpty()) {
				answersProcessed.add(ans);
			}
		}
		if(answersProcessed.size()==0) {
			throw new IllegalArgumentException("provide at least one answer that is not empty");
		}
		_answer = answersProcessed;
	}

	/**
	 * Normalize answer to make 'correct answer' matching more flexible
	 * <p>
	 * Remove macrons & accents, convert to lowercase, trim & deduplicate whitespace, remove 'a' or 'the' prefixes,
	 * remove trailing 's' (poor method of removing plurals), convert 'mount' to 'mt' & 'new zealand' to 'nz'
	 */
	public static String normalizeAnswer(String s) {
		return StringUtils.stripAccents(s)
				.trim()
				.toLowerCase()
				.replaceAll("\\s+", " ")
				.replaceAll("^(a|the)\\s+", "")
				.replaceAll("s$", "")
				.replace("mount", "mt")
				.replace("new zealand", "nz");
	}

	/**
	 * @return true if the answer is correct (case insensitive, normalizes input)
	 */
	public boolean checkAnswer(String rawInput) {
		// check if input matches any normalized answer
		String input = normalizeAnswer(rawInput);
		boolean correct = _answer.stream().anyMatch(a -> input.equals(normalizeAnswer(a)));

		_status = correct ? Status.CORRECT : Status.INCORRECT;
		return correct;
	}

	public List<String> getAnswer() {
		return _answer;
	}

	public Category getCategory() {
		return _category;
	}

	/**
	 * Sets the parent category that contains this question
	 * Used by Category deserializer
	 */
	public void setCategory(Category category) {
		_category = category;
	}

	public String getQuestion() {
		return _question;
	}

	public Status getStatus() {
		return _status;
	}

	public int getNumAttempted() {
		return _numAttempted;
	}

	public void setNumAttempted(int i) {
		_numAttempted = i;
	}

	/**
	 * Get the value of the question
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * Set the value of the question
	 */
	public void setValue(int value) {
		_value = value;
	}

	/**
	 * Question status
	 */
	public enum Status {
		UNATTEMPTED,
		CORRECT,
		INCORRECT,
	}
}
