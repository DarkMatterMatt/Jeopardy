package se206.quinzical.views;

import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

/**
 * This is Pane type.
 * Structure of the screen for real and practice game.
 * Contains HeaderPane and QuizContentSwitch.
 * 
 * Used by QuinzicalSwitch.
 *
 */
public class QuizPane extends ViewBase {
	private final VBox _container = new VBox();

	public QuizPane(QuinzicalModel model) {
		HeaderPane headerView = new HeaderPane(model);
		QuizContentSwitch quizContentView = new QuizContentSwitch(model);

		_container.getChildren().addAll(headerView.getView(), quizContentView.getView());
		_container.getStyleClass().add("quiz-view");
		addStylesheet("quiz.css");
	}

	@Override
	public VBox getView() {
		return _container;
	}
}
