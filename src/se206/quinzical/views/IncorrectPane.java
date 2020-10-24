package se206.quinzical.views;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuizModel;
import se206.quinzical.models.util.KeyEventManager;
import se206.quinzical.models.util.TextToSpeech;

/**
 * This class is Pane type, and contains AnimatedProgressBar atom.
 * View for incorrect answer screen, after an incorrect answer has been submitted
 * <p>
 * Used by GameSwitch and PracticeSwitch.
 */
public class IncorrectPane extends ViewBase {
	private static final int TIMEOUT_SECS = 4;
	private final TextFlow _answerTextFlow = new TextFlow();
	private final VBox _container = new VBox();
	private final QuizModel _model;
	private final AnimatedProgressBar _progressBarView;

	public IncorrectPane(QuizModel model) {
		_model = model;
		_progressBarView = new AnimatedProgressBar(TIMEOUT_SECS, item -> exitView());

		// show incorrect answer
		Label incorrectLabel = new Label("Incorrect!");

		_answerTextFlow.getStyleClass().add("text-flow");
		incorrectLabel.getStyleClass().addAll("text-bold", "text-main");

		Label interactToSkipLabel = new Label("Click or press any key to skip...");
		interactToSkipLabel.getStyleClass().add("interact-to-skip");

		// add elements and styles to container
		_container.getChildren().addAll(incorrectLabel, _answerTextFlow, interactToSkipLabel, _progressBarView.getView());
		_container.getStyleClass().add("incorrect-view");
		addStylesheet("incorrect.css");

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
		questionUpdate(_model.getCurrentQuestion());
	}

	/**
	 * Update to show correct answer
	 */
	private void questionUpdate(Question q) {
		if (_model.currentStateIsInternationalSection()) {
			q = _model .getInternationalCategoryFromQuinzicalModel().getActiveQuestionInPracticeModule();
		}
		if (q != null) {
			// speak correct answer
			TextToSpeech.getInstance().speak("Incorrect. The correct answer was " + q.getAnswer().get(0));

			List<Node> children = _answerTextFlow.getChildren();
			children.clear();
			children.add(createTextNode("The correct answer was ", "text-white"));

			List<String> answers = q.getAnswer();
			for (String a : answers) {
				children.add(createTextNode(" or ", "text-white"));
				children.add(createTextNode(a, "text-bold", "text-white"));
			}

			// remove first " or " text node, add a trailing full stop
			children.remove(1);
			children.add(createTextNode(".", "text-white"));
		}
	}

	/**
	 * Create a text node with specified content & style classes
	 */
	private Text createTextNode(String text, String ...styleClasses) {
		Text t = new Text(text);
		t.getStyleClass().addAll(styleClasses);
		return t;
	}
}
