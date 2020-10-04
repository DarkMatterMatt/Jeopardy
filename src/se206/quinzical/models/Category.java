package se206.quinzical.models;

import se206.quinzical.models.util.GsonPostProcessable;
import se206.quinzical.models.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Category class stores information about one category.
 */
public class Category implements GsonPostProcessable {
	private final String _name;
	private final List<Question> _questions;
	private int _activeQuestion = 0;
	private int _activeQuestionInPracticeModule = -1;
	private boolean _isSelected = false;

	/**
	 * Constructor that makes an empty Category
	 */
	public Category(String name) {
		_name = name;
		_questions = new ArrayList<>();
	}

	/**
	 * Constructor that makes a Category with a list of questions
	 */
	public Category(List<Question> questions, String name) {
		_questions = questions;
		_name = name;
	}

	/**
	 * Add question to the existing list of questions
	 */
	public void addQuestion(Question question) {
		_questions.add(question);
	}

	/**
	 * Active Question refers to the current question to be answered in the real quiz module (GameSwitch)
	 * Returns the question that would have to be answered if the category is selected in CategoriesList
	 */
	public Question getActiveQuestion() {
		if (_questions.size() > _activeQuestion) {
			return _questions.get(_activeQuestion);
		}
		return null;
	}

	/**
	 * Active question in the practice module is the question that will be displayed in the practice module
	 * when this category is selected.
	 * Active question in the practice module is uninitialised upon category object construction, but
	 * is set to certain question when this getter method is called.
	 */
	public Question getActiveQuestionInPracticeModule() {
		if (_questions.size() == 0) return null;
		if (_activeQuestionInPracticeModule == -1) {
			_activeQuestionInPracticeModule = ThreadLocalRandom.current().nextInt(0, _questions.size());
		}
		return _questions.get(_activeQuestionInPracticeModule);
	}

	/**
	 * Returns the value of the active question
	 * Value of the question is how much money the user earns by answering the question
	 */
	public int getActiveValue() {
		return 100 * (_activeQuestion + 1);
	}

	/**
	 * Returns the name of the category
	 */
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

	/**
	 * Returns the list of questions that a category contains
	 */
	public List<Question> getQuestions() {
		return Collections.unmodifiableList(_questions);
	}

	/**
	 * Returns a random question in the category, or null if there are no questions
	 */
	public Question getRandomQuestion() {
		if (_questions.size() == 0) return null;
		return RandomNumberGenerator.getNRandom(_questions, 1).get(0);
	}

	/**
	 * After deserializing, we need to give each question a reference to its parent category (removed during serialization)
	 */
	@Override
	public void gsonPostProcess() {
		_questions.forEach(q -> q.setCategory(this));
	}

	/**
	 * Check if this category is selected by the user
	 */
	public boolean isSelected() {
		return _isSelected;
	}

	/**
	 * Set active question to the next question in the list
	 */
	public void moveToNextQuestion() {
		_activeQuestion++;
	}

	/**
	 * This is triggered when user resets the game.
	 * For the real quiz module, it will make user start from the first question as the active question
	 */
	public void reset() {
		_activeQuestion = 0;
	}

	/**
	 * Set the active question of practice module to the given question.
	 */
	public void setActiveQUestionInPracticeModule(Question q) {
		_activeQuestionInPracticeModule = _questions.indexOf(q);
	}

	/**
	 * Set the selected state to be true (when clicked in category selection menu)
	 */
	public void setSelected() {
		_isSelected = true;
	}

	/**
	 * Set the selected state to be false (when another category is clicked in category selection menu)
	 */
	public void setUnselected() {
		_isSelected = false;
	}
}
