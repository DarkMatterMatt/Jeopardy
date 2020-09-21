package se206.quinzical.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class GameModel extends QuizModel {
	private final IntegerProperty _score = new SimpleIntegerProperty();

	public GameModel(QuinzicalModel model) {
		super(model);
	}

	public int getScore() {
		return _score.get();
	}

	public IntegerProperty getScoreProperty() {
		return _score;
	}

	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		// boolean correct = getCurrentQuestion().checkAnswer(answer);
		boolean correct = true;
		setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
	}
}
