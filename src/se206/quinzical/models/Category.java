package se206.quinzical.models;

import se206.quinzical.models.util.GsonPostProcessable;
import se206.quinzical.models.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Category implements GsonPostProcessable {
	private final String _name;
	private final List<Question> _questions;
	private boolean _isSelected = false;
	private int _activeQuestion = 0;
	private Question _activeQuestionInPracticeModule;

	public Category(String name) {
		_name = name;
		_questions = new ArrayList<>();
	}

	public Category(List<Question> questions, String name) {
		_questions = questions;
		_name = name;
	}

	public void addQuestion(Question question) {
		_questions.add(question);
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
	 * Returns the number of questions that have been attempted
	 */
	public long getNumAttempted() {
		return _questions.stream().filter(q -> q.getStatus() != Question.Status.UNATTEMPTED).count();
	}

	/**
	 * Returns the number of questions that have not been attempted
	 */
	public long getNumRemaining() {
		return _questions.stream().filter(q -> q.getStatus() == Question.Status.UNATTEMPTED).count();
	}

	public List<Question> getQuestions() {
		return Collections.unmodifiableList(_questions);
	}

	@Override
	public void gsonPostProcess() {
		_questions.forEach(q -> q.setCategory(this));
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

	/**
	 * Returns a random question in the category, or null if there are no questions
	 */
	public Question getRandomQuestion() {
		if (_questions.size() == 0) return null;
		return RandomNumberGenerator.getNRandom(_questions, 1).get(0);
		/*
		List<Integer> list = RandomNumberGenerator.takeFive(_questions.size());
		return _questions.get(list.get(0));
		*/
	}
	
	public Question getActiveQuestionInPracticeModule() {
		if (_activeQuestionInPracticeModule == null) {
			_activeQuestionInPracticeModule = getRandomQuestion();
		}
		return _activeQuestionInPracticeModule;
	}
	public void setActiveQUestionInPracticeModule(Question q) {
		_activeQuestionInPracticeModule = q;
	}
	
	
}
