package se206.quinzical.models;

import se206.quinzical.models.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a question; has answer (list of), a question.
 */
public class Question {
	private final List<String> _answer;
	private final String _question;
	private transient Category _category;
	private int _numAttempted;
	private Status _status = Status.UNATTEMPTED;
	private int _value = -1;

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

	/**
	 * Create question with answers
	 */
	public Question(Category category, String question, String... answer) {
		_answer = Arrays.asList(answer);
		_category = category;
		_question = question;
	}

	/**
	 * Create a question from raw string
	 */
	public Question(String raw, Category category) {
		_category = category;

		String[] processed = raw.split("\\|");
		if (processed.length <= 1) {
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
		if (answersProcessed.size() == 0) {
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
	 * Return true if the answer is correct (case insensitive, normalizes input)
	 */
	public boolean checkAnswer(String rawInput) {
		// check if input matches any normalized answer
		String input = normalizeAnswer(rawInput);
		boolean correct = _answer.stream().anyMatch(a -> input.equals(normalizeAnswer(a)));

		_status = correct ? Status.CORRECT : Status.INCORRECT;
		return correct;
	}

	/**
	 * Return the answer(s) in a list
	 */
	public List<String> getAnswer() {
		return _answer;
	}

	/**
	 * Return the category
	 */
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

	/**
	 * Return the number of times this question was attempted
	 */
	public int getNumAttempted() {
		return _numAttempted;
	}

	/**
	 * Set the number of attempted to a given number
	 */
	public void setNumAttempted(int i) {
		_numAttempted = i;
	}

	/**
	 * Return the question string
	 */
	public String getQuestion() {
		return _question;
	}

	/**
	 * Return the status of this question (refer to the enum State below)
	 */
	public Status getStatus() {
		return _status;
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
	 * Indicate to itself that this question has been skipped
	 */
	public void skipQuestion() {
		_status = Status.SKIPPED;
	}

	/**
	 * Question status
	 */
	public enum Status {
		UNATTEMPTED,
		SKIPPED,
		CORRECT,
		INCORRECT,
	}
}
