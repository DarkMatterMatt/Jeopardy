package se206.quinzical.models;

import se206.quinzical.models.util.StringUtils;

public class Question {
	private final String _answer;
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

	public Question(String question, String answer, Category category) {
		_answer = answer;
		_category = category;
		_question = question;
	}

	public Question(String raw, Category category) {
		_category = category;

		int delimiter = raw.indexOf(",");
		if (delimiter == -1) {
			throw new IllegalArgumentException("raw question should have a comma as a delimiter"
					+ "separating question (on the left of comma) and answer (right of comma)");
		}

		String tmpQ = raw.substring(0, delimiter).trim(); // e.g. NZer who led the land march from Te Hapua to Parliament
		String tmpA = raw.substring(delimiter + 1).trim(); // e.g. ( Who is) Dame Whina Cooper.

		// construct question
		String[] tmpAParsed = tmpA.split("\\)");
		if (tmpAParsed.length != 2) {
			throw new IllegalArgumentException("answer part (next to comma) should contain a pair of "
					+ "bracket that encloses either 'who are' 'what is/are' 'where is/are'");
		}

		// Who is / where is/ etc = tmpAParsed[0].substring(1).trim()
		_question = tmpQ.substring(0, 1).toUpperCase() + tmpQ.substring(1).toLowerCase(); // e.g. Who is NZer who led the land march from Te Hapua to Parliament

		// construct answer
		tmpA = tmpAParsed[1].trim();
		_answer = tmpA.substring(0, 1).toUpperCase() + tmpA.substring(1);
	}

	/**
	 * @return true if the answer is correct (case insensitive, normalizes input)
	 */
	public boolean checkAnswer(String rawInput) {
		// remove accents/macrons, remove parentheses, trim trailing/leading whitespace
		String answer = StringUtils.stripTextInParentheses(StringUtils.normalize(_answer)).trim();

		// remove accents/macrons, remove "what is" and "who is" from beginning, trim trailing/leading whitespace
		String input = StringUtils.normalize(rawInput).replaceAll("^\\s*(what|who)\\s*is", "").trim();

		boolean correct = answer.equalsIgnoreCase(input);
		_status = correct ? Status.CORRECT : Status.INCORRECT;
		return correct;
	}

	public String getAnswer() {
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
