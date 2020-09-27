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
	private static final int QUESTIONS_PER_CATEGORY = 5;

	private final List<Category> _categories = new ArrayList<>();
	private final IntegerProperty _score = new SimpleIntegerProperty();

	public PresetQuinzicalModel(QuinzicalModel model) {
		super(model);

		// pick the five categories that will be used for this game
		List<Category> fullCategories = RandomNumberGenerator.getNRandom(model.getCategories(), CATEGORIES_PER_GAME);

		for (Category fullCategory : fullCategories) {
			// this new category will have a copy of each of the questions, with the value set
			Category newCategory = new Category(fullCategory.getName());
			_categories.add(newCategory);

			// pick the five categories that will be used for this category
			List<Question> questions = RandomNumberGenerator.getNRandom(fullCategory.getQuestions(), QUESTIONS_PER_CATEGORY);

			// go through each question, clone it and set its value, then add it to the category
			for (int i = 0; i < questions.size(); i++) {
				Question newQuestion = new Question(questions.get(i));
				newQuestion.setValue(100 * (i + 1));
				newCategory.addQuestion(newQuestion);
			}
		}
	}

	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		boolean correct = getCurrentQuestion().checkAnswer(answer);
		if (correct) {
			setState(State.CORRECT_ANSWER);
			_score.set(_score.get() + getCurrentQuestion().getValue());
		}
		else {
			setState(State.INCORRECT_ANSWER);
		}
	}

	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	public int getScore() {
		return _score.get();
	}

	public IntegerProperty getScoreProperty() {
		return _score;
	}

	@Override
	public void selectCategory(Category item) {
		Question q = item.getActiveQuestion();
		beginQuestion(q, q.getValue());
		setState(State.CATEGORY_PREVIEW);
	}

	/**
	 * Called from the "category preview" screen, moves to the answer question
	 */
	public void confirmCategory() {
		setState(State.ANSWER_QUESTION);
	}
}
