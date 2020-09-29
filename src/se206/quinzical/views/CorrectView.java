package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuizModel;
import se206.quinzical.models.util.KeyEventManager;

/**
 * View for correct answer screen, after a correct answer has been submitted
 */
public class CorrectView extends View {
	private static final int TIMEOUT_SECS = 2;
	private final VBox _container = new VBox();
	private final QuizModel _model;
	private final AnimatedProgressBarView _progressBarView;

	public CorrectView(QuizModel model) {
		_model = model;
		_progressBarView = new AnimatedProgressBarView(TIMEOUT_SECS, item -> exitView());

		Label correctLabel = new Label("Correct!");
		correctLabel.getStyleClass().addAll("text-bold", "text-main");

		Label interactToSkipLabel = new Label("Click or press any key to skip...");
		interactToSkipLabel.getStyleClass().add("interact-to-skip");

		// add elements and styles to container
		_container.getChildren().addAll(correctLabel, interactToSkipLabel, _progressBarView.getView());
		_container.getStyleClass().add("correct-view");
		addStylesheet("correct.css");

		// handle starting / stopping the animated progress bar
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());

		// click to exit
		_container.setOnMouseClicked(e -> exitView());

		// press any key to exit
		KeyEventManager.getInstance().addPressListener(ev -> {
			if (_container.isVisible()) {
				exitView();
			}
		}, KeyEventManager.ANY_KEY);
	}

	/**
	 * Called when we are ready to leave this screen
	 */
	private void exitView() {
		_model.finishQuestion();
	}

	public VBox getView() {
		return _container;
	}

	/**
	 * Called when we are ready to leave this screen
	 */
	private void onVisibilityChanged() {
		if (!_container.isVisible()) {
			// container is hidden
			_progressBarView.stop();
			return;
		}
		// container was made visible
		_progressBarView.start();
	}
}
