package se206.quinzical.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import se206.quinzical.models.util.GsonPostProcessable;
import se206.quinzical.models.util.TextToSpeech;
import se206.quinzical.views.IconView;

import java.util.List;

public abstract class QuizModel implements GsonPostProcessable {
	private final ObjectProperty<Question> _currentQuestion = new SimpleObjectProperty<>();
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.SELECT_CATEGORY);
	protected transient QuinzicalModel _model;

	public QuizModel(QuinzicalModel model) {
		_model = model;
	}

	/**
	 * Submit an answer for the current question
	 */
	public abstract void answerQuestion(String answer);

	public void beginQuestion(Question question) {
		_currentQuestion.set(question);
	}

	/**
	 * Return to category selection
	 */
	public void finishQuestion() {
		if (getState() != State.CORRECT_ANSWER && getState() != State.INCORRECT_ANSWER) {
			throw new IllegalStateException("Can only reset when in the CORRECT_ANSWER or INCORRECT_ANSWER state");
		}
		setState(State.SELECT_CATEGORY);
	}

	public abstract List<Category> getCategories();

	public Question getCurrentQuestion() {
		return _currentQuestion.get();
	}

	public ObjectProperty<Question> getCurrentQuestionProperty() {
		return _currentQuestion;
	}

	public State getState() {
		return _state.get();
	}

	@Override
	public void gsonPostProcess() {
		Question currentQuestion = getCurrentQuestion();
		if (currentQuestion != null) {
			Category activeCategory = getCategories()
					.stream()
					.filter(Category::isSelected)
					.findFirst()
					.orElse(null);
			currentQuestion.setCategory(activeCategory);
		}
	}

	protected void setState(State state) {
		_state.set(state);
		save();
	}

	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	public TextToSpeech getTextToSpeech() {
		return _model.getTextToSpeech();
	}

	/**
	 * Save current state to disk
	 */
	public void save() {
		_model.save();
	}

	public abstract void selectCategory(Category item);

	/**
	 * Sets the parent QuinzicalModel model
	 * Used by QuinzicalModel deserializer
	 */
	public void setQuinzicalModel(QuinzicalModel model) {
		_model = model;
	}

	public void skinCategoryImage(IconView icon, String categoryName) {
		try {
			icon.setImage("../assets/categoryicons/" + categoryName + ".png");
		} catch (NullPointerException e) {
			icon.setImage("../assets/icon-missing.png");
		}
	}
	
	public boolean getTextVisibility() {
		return _model.textVisible();
	}

	/**
	 * The current screen being shown
	 */
	public enum State {
		RESET,
		SELECT_CATEGORY,
		CATEGORY_PREVIEW,
		ANSWER_QUESTION,
		CORRECT_ANSWER,
		INCORRECT_ANSWER,
		GAME_OVER,
	}
}
