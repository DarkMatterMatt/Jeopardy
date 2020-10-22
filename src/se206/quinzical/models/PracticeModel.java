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
		return _model.getState() == QuinzicalModel.State.PRACTICE;
	}

	/**
	 * Check if the given answer by the user is correct & make appropriate state changes.
	 */
	@Override
	public void answerQuestion(String answer) {
		if (_model.getPracticeModel().currentStateIsInternationalSection()) {
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
		Question q = this.getInternationalCategoryFromQuinzicalModel().getActiveQuestionInPracticeModule();
		boolean correct = q.checkAnswer(answer);

		// if correct, leave the number of lives, increase score
		if (!correct)
			_model.reduceLives();
		if (correct)
			_model.increaseInternationalScore();

		// TODO:if the lives are depleted, switch scene to game over
		// TODO:register highscore, reset score
		// if lives = 1 & incorrect
		// switch screen to game over
		// check if the current score exceeds highscore
		// else register score to be higher by 1
		// if the current score is the same as highscore, then increase highscore as
		// well
		// turn on the flag of having exceeded highscore?
		if (_model.getLivesProperty().get() == 1 && !correct) {
			AlertFactory.getCustomWarning("Game over!", "Start over again");
			// switch screen to game over
			// check if the current score exceeds highscore
		} else {
			if (correct) {
				if (_model.getInternationalHighscore() == _model.getCurrentInternationalScore()) {
					int newScore = _model.getCurrentInternationalScore() + 1;
					_model.setInternationalHighScore(newScore);
				}
				_model.increaseInternationalScore();
			}

		}

		// change to correct/incorrect answer screen
		setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);

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
