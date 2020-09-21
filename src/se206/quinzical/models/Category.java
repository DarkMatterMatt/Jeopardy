package se206.quinzical.models;

import java.util.ArrayList;
import java.util.List;

public class Category {
	boolean _isSelected = false;
	String _name;
	List<Question> _questions = new ArrayList<>();
	private int _activeQuestion = 0;

	public Category(List<String> questions, String name) {
		_name = name;
		for (String question : questions) {
			try {
				Question q = new Question(question);
				_questions.add(q);
			} catch (IllegalArgumentException e) {
				//
			}
		}
	}

	public Category(List<Question> questions, String name, boolean placeHolder) {
		_questions = questions;
		_name = name;
	}

	public Question getActiveQuestion() {
		if (_questions.size() > _activeQuestion) {
			return _questions.get(_activeQuestion);
		}
		return null;
	}

	public int getActiveValue() {
		return 100 * (_activeQuestion + 1);
	}

	public String getName() {
		return _name;
	}

	/**
	 * Returns the number of questions that have not been attempted
	 */
	public long getNumRemaining() {
		return _questions.stream().filter(q -> q.getStatus() == Question.Status.UNATTEMPTED).count();
	}

	/**
	 * Returns the number of questions that have been attempted
	 */
	public long getNumAttempted() {
		return _questions.stream().filter(q -> q.getStatus() != Question.Status.UNATTEMPTED).count();
	}

	public boolean isSelected() {
		return _isSelected;
	}

	public void moveToNextQuestion() {
		_activeQuestion++;
	}

	public void reset() {
		_activeQuestion = 0;
	}

	public void setSelected() {
		_isSelected = true;
	}

	public void setUnselected() {
		_isSelected = false;
	}
}
