package se206.quinzical.models;

import java.util.Collections;
import java.util.List;

import se206.quinzical.views.AlertFactory;

/**
 * PracticeModel is the QuizModel for practice module. (used by PracticeSwitch).
 * In addition to the role of QuizModel, it sets the question to be displayed
 * when a category is selected (see selectCategory(Category item)).
 */
public class PracticeModel extends QuizModel {
	private final List<Category> _categories;

	/**
	 * Constructor that takes all the available categories from the Quinzical model.
	 */
	public PracticeModel(QuinzicalModel model) {
		super(model);
		_categories = _model.getCategories();
	}

	/**
	 * Returns true if this model is being modified by the user
	 */
	@Override
	public boolean isActive() {
		return _model.getState() == QuinzicalModel.State.PRACTICE
				|| _model.getState() == QuinzicalModel.State.INTERNATIONAL;
	}

	/**
	 * Return the current question of this model
	 */
	@Override
	public Question getCurrentQuestion() {
		if (_model != null && internationalSectionActive()) {
			return getInternationalCategory().getActiveQuestionInPracticeModule();
		}
		return super.getCurrentQuestion();
	}

	/**
	 * Check if the given answer by the user is correct & make appropriate state changes.
	 */
	@Override
	public void answerQuestion(String answer) {
		if (internationalSectionActive()) {
			handleInternationalQuestion(answer);
			return;
		}
		if (getState() != State.ANSWER_QUESTION && getState() != State.RETRY_INCORRECT_ANSWER) {
			throw new IllegalStateException(
					"Previous state should be ANSWER_QUESTION or RETRY_INCORRECT_ANSWER, found " + getState());
		}
		boolean correct = getCurrentQuestion().checkAnswer(answer);

		// count number of attempts
		Question q = getCurrentQuestion();
		// increase number of attempt for that question
		if (!correct)
			q.setNumAttempted(q.getNumAttempted() + 1);

		// if that question has been answered 3 times, reset that question
		// and change the active question to different random question
		if (correct || q.getNumAttempted() >= 3) {
			q.setNumAttempted(0);

			Category category = q.getCategory();
			Question nextQuestion = category.getRandomQuestion();
			if (category.getQuestions().size() >= 2) {
				// never give the same question twice in a row
				while (nextQuestion == category.getActiveQuestionInPracticeModule()) {
					nextQuestion = category.getRandomQuestion();
				}
			}
			q.getCategory().setActiveQuestionInPracticeModule(nextQuestion);

			setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
			return;
		}
		// incorrect, answered less than 3 times, let the user retry
		setState(State.RETRY_INCORRECT_ANSWER);
		setState(State.ANSWER_QUESTION);
	}

	private void handleInternationalQuestion(String answer) {
		Question q = getInternationalCategory().getActiveQuestionInPracticeModule();
		boolean correct = q.checkAnswer(answer);

		if (correct) {
			setState(State.CORRECT_ANSWER);
			_model.increaseInternationalScore();
			if (_model.getInternationalScore() > _model.getInternationalHighScore()) {
				_model.setInternationalHighScore(_model.getInternationalScore());
			}
		}
		else {
			setState(State.INCORRECT_ANSWER);
			_model.reduceLives();
			if (_model.getLives() == 0) {
				AlertFactory.getCustomWarning(_model, "Game over!", "Start over again");
				_model.resetInternationalScore();
				_model.resetLives();
			}
		}
	}

	/**
	 * Return all categories stored in this model.
	 */
	@Override
	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	/**
	 * When category is selected, give the active question of that category.
	 */
	@Override
	public void selectCategory(Category item) {
		beginQuestion(item.getActiveQuestionInPracticeModule());
		setState(QuizModel.State.SELECT_CATEGORY);
		setState(QuizModel.State.ANSWER_QUESTION);
	}
}
