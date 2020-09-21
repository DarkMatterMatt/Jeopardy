package se206.quinzical.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import se206.quinzical.models.util.TextToSpeech;

public abstract class QuizModel {
	private final QuinzicalModel _model;
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.SELECT_CATEGORY);

	public QuizModel(QuinzicalModel model) {
		_model = model;
	}

	public State getState() {
		return _state.get();
	}

	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	public TextToSpeech getTextToSpeech() {
		return _model.getTextToSpeech();
	}

	public void reset() {
		_state.set(State.RESET); // trigger any RESET listeners
		_state.set(State.SELECT_CATEGORY);
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
