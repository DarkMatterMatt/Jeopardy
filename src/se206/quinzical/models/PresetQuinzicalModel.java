package se206.quinzical.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import se206.quinzical.models.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * PresetQuinzicalModel contains categories, questions, score & other game info for the main mode.
 *
 * @author Hajin Kim
 */
public class PresetQuinzicalModel extends QuizModel {
	private static final int CATEGORIES_PER_GAME = 5;
	private static final double INCORRECT_SCORE_MULTIPLIER = 0.5;
	private static final int QUESTIONS_PER_CATEGORY = 5;
	private final List<Category> _categories = new ArrayList<>();
	private final IntegerProperty _score = new SimpleIntegerProperty();

	public PresetQuinzicalModel(QuinzicalModel model) {
		super(model);
		loadQuestions();
	}

	/**
	 * Check if the given answer is correct & change to appropriate state.
	 */
	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		boolean correct = getCurrentQuestion().checkAnswer(answer);
		if (correct) {
			setScore(getScore() + getCurrentQuestion().getValue());
			setState(State.CORRECT_ANSWER);
		}
		else {
			// lose a fraction of score when incorrect, score does not go below zero
			setScore(Math.max(0, getScore() - (int) Math.round(INCORRECT_SCORE_MULTIPLIER * getCurrentQuestion().getValue())));
			setState(State.INCORRECT_ANSWER);
		}
	}

	/**
	 * Called from the "category preview" screen, moves to the answer question
	 */
	public void confirmCategory() {
		setState(State.ANSWER_QUESTION);
	}

	/**
	 * Indicate that the current question is finished being answered,
	 * and the current question is moved to the next question.
	 */
	@Override
	public void finishQuestion() {
		super.finishQuestion();

		// the next time the category is selected, the next question will be chosen
		getCurrentQuestion().getCategory().moveToNextQuestion();

		if (getNumRemaining() > 0) {
			// update "current question" to be the next question in the same category
			selectCategory(getCurrentQuestion().getCategory());
		}
		else {
			setState(State.GAME_OVER);
		}
	}

	/**
	 * Return categories of the real game module
	 */
	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	/**
	 * Returns the number of questions that have been attempted
	 */
	public long getNumAttempted() {
		return _categories.stream().mapToLong(Category::getNumAttempted).sum();
	}

	/**
	 * Returns the number of questions that have not been attempted
	 */
	public long getNumRemaining() {
		return _categories.stream().mapToLong(Category::getNumRemaining).sum();
	}

	/**
	 * Get current score
	 */
	public int getScore() {
		return _score.get();
	}

	/**
	 * Update current score
	 */
	private void setScore(int score) {
		_score.set(score);
	}

	/**
	 * Return score property (e.g. to bind a change listener)
	 */
	public IntegerProperty getScoreProperty() {
		return _score;
	}

	/**
	 * Select 5 categories, each containing random 5 questions.
	 */
	private void loadQuestions() {
		_categories.clear();

		// pick the five categories that will be used for this game
		List<Category> fullCategories = RandomNumberGenerator.getNRandom(_model.getCategories(), CATEGORIES_PER_GAME);

		for (Category fullCategory : fullCategories) {
			// this new category will have a copy of each of the questions, with the value set
			Category newCategory = new Category(fullCategory.getName());
			_categories.add(newCategory);

			// pick the five categories that will be used for this category
			List<Question> questions = RandomNumberGenerator.getNRandom(fullCategory.getQuestions(), QUESTIONS_PER_CATEGORY);

			// go through each question, clone it and set its value, then add it to the category
			for (int i = 0; i < questions.size(); i++) {
				Question newQuestion = new Question(questions.get(i));
				newQuestion.setCategory(newCategory);
				newQuestion.setValue(100 * (i + 1));
				newCategory.addQuestion(newQuestion);
			}
		}
	}

	/**
	 * This is triggered when user resets the game.
	 */
	public void reset() {
		setScore(0);

		// randomly select another set of categories/questions
		loadQuestions();
		beginQuestion(null);

		setState(State.RESET); // trigger any RESET listeners
		setState(State.SELECT_CATEGORY);
	}

	/**
	 * Get active question for the real module, given the category selection
	 */
	@Override
	public void selectCategory(Category item) {
		beginQuestion(item.getActiveQuestion());
		setState(State.CATEGORY_PREVIEW);
	}

	/**
	 * Indicate to the model that question is skipped
	 */
	public void skipQuestion() {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		getCurrentQuestion().skipQuestion();
		setState(State.SKIP_ANSWER);
	}
}
