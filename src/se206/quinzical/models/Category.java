package se206.quinzical.models;
import java.util.ArrayList;
import java.util.List;

public class Category {
	String _name;
	List<Question> _questions = new ArrayList<Question>();
	
	
	public Category(List<String> questions, String name) {
		_name = name;
		for (String question: questions) {
			try {
				Question q = new Question(question);
				_questions.add(q);
			}catch(IllegalArgumentException e) {
				//
			}

		}
	}
	
	
	public Category(List<Question> questions, String name, boolean placeHolder) {
		_questions = questions;
		_name = name;
	}
	
	
}
