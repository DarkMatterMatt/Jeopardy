package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

/**
 * This class is Pane type.
 * View for the header, and contains TaskBar atom, title, and score
 * <p>
 * This Pane is used by QuizPane
 */
public class HeaderPane extends ViewBase {
	private final VBox _container = new VBox();
	private final QuinzicalModel _model;
	private final Label _subtitleLabel = new Label();

	public HeaderPane(QuinzicalModel model) {
		_model = model;

		// title
		Label titleLabel = new Label("Quinzical");
		titleLabel.getStyleClass().add("title");

		// show score / "Practice Mode"
		_subtitleLabel.getStyleClass().add("subtitle");

		// taskbar
		Taskbar taskbar = new Taskbar(_model);

		_container.getChildren().addAll(taskbar.getView(), titleLabel, _subtitleLabel);
		_container.getStyleClass().add("header");
		addStylesheet("header.css");

		// update subtitle automatically
		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
		_model.getPresetModel().getScoreProperty().addListener((observable, oldVal, newVal) -> onModelStateChange());
	}

	public Pane getView() {
		return _container;
	}

	private void onModelStateChange() {
		switch (_model.getState()) {
			case GAME:
				_subtitleLabel.setText("Winnings: $" + _model.getPresetModel().getScore());
				break;
			case PRACTICE:
				_subtitleLabel.setText("Practice Mode");
				break;
			case INTERNATIONAL:
				_subtitleLabel.setText("International section");
				break;
			case LEADERBOARD:
				_subtitleLabel.setText("Leaderboard");
				break;
			case MENU:
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
