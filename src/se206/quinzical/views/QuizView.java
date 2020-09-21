package se206.quinzical.views;

import javafx.scene.layout.HBox;
import se206.quinzical.models.QuinzicalModel;

public class QuizView extends View {
	private final HBox _container = new HBox();

	public QuizView(QuinzicalModel model) {
		HeaderView headerView = new HeaderView(model);
		QuizContentView quizContentView = new QuizContentView(model);

		_container.getChildren().addAll(headerView.getView(), quizContentView.getView());
		_container.getStyleClass().add("quiz-view");
		addStylesheet("quiz.css");
	}

	@Override
	public HBox getView() {
		return _container;
	}
}
