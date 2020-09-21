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
}
