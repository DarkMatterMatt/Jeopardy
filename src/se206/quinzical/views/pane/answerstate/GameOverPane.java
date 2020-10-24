package se206.quinzical.views.pane.answerstate;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.QuizModel;
import se206.quinzical.views.atom.AnswerTextField;
import se206.quinzical.views.base.ViewBase;

/**
 * This class is Pane type.
 * View for game over screen, after all questions have been attempted
 * <p>
 * Used by GameSwitch
 */
public class GameOverPane extends ViewBase {
	private final VBox _container = new VBox();
	private final PresetQuinzicalModel _model;
	private final Label _subtitleLabel = new Label();
	private final Label _titleLabel = new Label();
	private final AnswerTextField _nameInputView;

	public GameOverPane(PresetQuinzicalModel model) {
		_model = model;
		_nameInputView = new AnswerTextField(_model, name -> {
			_model.getModel().getLeaderboardModel().addScore(_model.getScore(), name);
			_model.getModel().showLeaderboard();
			_model.reset();
		});

		_titleLabel.getStyleClass().addAll("text-bold", "text-gold", "title");
		_subtitleLabel.getStyleClass().addAll("text-white", "subtitle");

		Label submitNameLabel = new Label("Submit your name to the leaderboard");
		submitNameLabel.getStyleClass().addAll("submit-name", "text-small", "text-white");

		_container.getChildren().addAll(_titleLabel, _subtitleLabel, _nameInputView.getView(), submitNameLabel);
		_container.getStyleClass().add("game-over");
		addStylesheet("game-over.css");

		onModelStateChange();
		model.getStateProperty().addListener((observable, oldVal, newVal) -> onModelStateChange());
	}

	public VBox getView() {
		return _container;
	}

	/**
	 * Update text based on score
	 */
	private void onModelStateChange() {
		// only update data when we are the active view
		if (_model.getState() != QuizModel.State.GAME_OVER) return;

		int score = _model.getScore();
		if (score > 0) {
			_titleLabel.setText("Congratulations!");
			_subtitleLabel.setText("You won $" + score + "!");
		}
		else {
			_titleLabel.setText("Welp.");
			_subtitleLabel.setText("I guess there's a first time for everything.\nYou managed to get every question wrong!");
		}
		_nameInputView.focus();
	}
}
