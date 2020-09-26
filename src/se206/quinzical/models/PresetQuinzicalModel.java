package se206.quinzical.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import se206.quinzical.models.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PresetQuinzicalModel contains categories, questions, score & other game info for the main mode.
 *
 * @author Hajin Kim
 */
public class PresetQuinzicalModel extends QuizModel {
	private final List<Category> _fiveCategoriesWithFiveQuestions;
	private final IntegerProperty _score = new SimpleIntegerProperty();

	public PresetQuinzicalModel(QuinzicalModel qdb) {
		super(qdb);

		_fiveCategoriesWithFiveQuestions = new ArrayList<>();

		List<Category> categories = selectFiveCategories(qdb);
		for (Category category : categories) {
			Category newCategory = selectFiveQuestions(category);
			_fiveCategoriesWithFiveQuestions.add(newCategory);
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
		return Collections.unmodifiableList(_fiveCategoriesWithFiveQuestions);
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

	/*
	 * Below are utility methods to select 5 categories/questions and return List<Category> / Category
	 */

	public List<Category> selectFiveCategories(QuinzicalModel qdb) {
		if (qdb._categories.size() == 0) {
			return new ArrayList<>();
		}

		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(qdb._categories.size());
		List<Category> result = new ArrayList<>();

		for (Integer n : fiveNumbers) {
			// get nth category from the categories set, and add to the preset categories
			result.add(qdb._categories.get(n));
		}
		return result;
	}

	public Category selectFiveQuestions(Category c) {
		List<Question> copyOfQuestions = new ArrayList<>(c.getQuestions());
		Collections.shuffle(copyOfQuestions);
		List<Question> fiveQuestions = copyOfQuestions.stream().limit(5).collect(Collectors.toList());

		/*
		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(c.getQuestions().size());
		List<Question> fiveQuestions = new ArrayList<>();
		int value = 100;
		for (Integer n : fiveNumbers) {
			// get nth question from the questions of the category
			Question q = c.getQuestions().get(n);
			// assign value
			fiveQuestions.add(q);
			value = value + 100;
		}
		*/
		return new Category(fiveQuestions, c.getName());
	}
}
