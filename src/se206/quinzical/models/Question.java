package se206.quinzical.models;

public class Question {
	String _question;
	String _answer;

	public Question(String raw) {
		processRaw(raw);
	}

	public void processRaw(String raw) {
		int delimiter = raw.indexOf(",");
		if(delimiter == -1) {
			throw new IllegalArgumentException("raw question should have a comma as a delimiter"
					+ "separating question (on the left of comma) and answer (right of comma)");
		}

		String tmpQ = raw.substring(0,delimiter).trim(); //Eg., NZer who led the land march from Te Hapua to Parliament
		String tmpA = raw.substring(delimiter+1).trim(); //Eg., ( Who is) Dame Whina Cooper.

		// construct question
		String[] tmpAParsed= tmpA.split("\\)");
		if(tmpAParsed.length != 2) {
			throw new IllegalArgumentException("answer part (next to comma) should contain a pair of "
					+ "bracket that encloses either 'who are' "
					+ "'what is/are' 'where is/are'");
		}
		_question = tmpAParsed[0].substring(1).trim()+ " " + tmpQ.toLowerCase(); //Eg., Who is NZer who led the land march from Te Hapua to Parliament

		// construct answer
		tmpA = tmpAParsed[1].trim();
		_answer = tmpA.substring(0, 1).toUpperCase() + tmpA.substring(1);
	}

	public String getAnswer() {
		return _answer;
	}

	public String getQuestion() {
		return _question;
	}
}
