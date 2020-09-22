package se206.quinzical.models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import se206.quinzical.models.util.TextToSpeech;

public abstract class QuizModel {
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.SELECT_CATEGORY);
	public transient QuinzicalModel _model;
	private Question _currentQuestion = null;
	private int _currentQuestionValue;

	public QuizModel(QuinzicalModel model) {
		_model = model;
	}

	/**
	 * Submit an answer for the current question
	 */
	public abstract void answerQuestion(String answer);

	public void beginQuestion(Question question, int value) {
		_currentQuestion = question;
		_currentQuestionValue = value;
	}

	/**
	 * Return to category selection
	 */
	public void finishQuestion() {
		if (_state.get() != State.CORRECT_ANSWER && _state.get() != State.INCORRECT_ANSWER) {
			throw new IllegalStateException("Can only reset when in the CORRECT_ANSWER or INCORRECT_ANSWER state");
		}
		_state.set(State.SELECT_CATEGORY);
	}

	public Question getCurrentQuestion() {
		return _currentQuestion;
	}

	public int getCurrentQuestionValue() {
		return _currentQuestionValue;
	}

	public State getState() {
		return _state.get();
	}

	protected void setState(State state) {
		_state.set(state);
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
	 * Sets the parent QuinzicalModel model
	 * Used by QuinzicalModel deserializer
	 */
	public void setQuinzicalModel(QuinzicalModel model) {
		_model = model;
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
