package se206.quinzical.models;

public class Question {
	String _question;
	String _answer;
	
	public Question(String raw) {
		String[] processed = raw.split(",");
		int l = processed.length;
		
		if(l == 2) {
			_question = processed[0];
			_answer = processed[1];
		}else {
			throw new IllegalArgumentException("question format should have only one comma separating question part and answer part");
		}
	}
}
