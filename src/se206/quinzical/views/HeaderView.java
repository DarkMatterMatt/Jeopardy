package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

/**
 * View for the header. Contains taskbar, title, score
 */
public class HeaderView extends View {
	private final VBox _container = new VBox();
	private final QuinzicalModel _model;
	private final Label _subtitleLabel = new Label();

	public HeaderView(QuinzicalModel model) {
		_model = model;

		// title
		Label titleLabel = new Label("Jeopardy!");
		titleLabel.getStyleClass().add("title");

		// show score / "Practice Mode"
		_subtitleLabel.getStyleClass().add("subtitle");

		// taskbar
		TaskbarView taskbar = new TaskbarView(_model);

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
			case PRACTICE:
				_subtitleLabel.setText("Practice Mode");
				break;
			case MENU:
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
