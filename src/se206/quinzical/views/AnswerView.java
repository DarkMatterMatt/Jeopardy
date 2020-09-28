package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuizModel;

/**
 * View for answer screen, when a question has been selected
 */
public class AnswerView extends View {
	private final Label _categoryLabel = new Label();
	private final VBox _container = new VBox();
	private final IconView _iconView = new IconView();
	private final QuizModel _model;
	private final Label _questionLabel = new Label();
	private final HBox _hintBox=new HBox();

	public AnswerView(QuizModel model) {
		_model = model;

		AnswerInputView answerInputView = new AnswerInputView(_model);
		HBox categoryContainer = new HBox(_iconView.getView(), _categoryLabel);
		categoryContainer.getStyleClass().add("category-container");
		HBox hintView = new HBox();

		_categoryLabel.getStyleClass().addAll("text-bold", "text-gold", "category");
		_questionLabel.getStyleClass().addAll("text-white", "question");

		_iconView
				.setSize(56, 56)
				.addClass("category-icon");

		// add container styles
		addStylesheet("answer.css");
		_container.getStyleClass().add("answer");
		_container.getChildren().addAll(categoryContainer, _questionLabel, answerInputView.getView());

		// reload screen when we are made visible
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());
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
			return;
		}
		// container was made visible
		questionUpdate(_model.getCurrentQuestion());
	}

	/**
	 * Update display to show question details
	 */
	private void questionUpdate(Question q) {
		if (q == null) return;
		
		if(q.getNumAttempted()>=2) {
//			_hintBox.setVisible(true);
		}else {
//			_hintBox.setVisible(false);
		}
		

		String categoryName = (q!=null)?q.getCategory().getName():"Select other categories";
		String question = (q!=null)?q.getQuestion():"There are no more available questions in this category";
		
		_model.skinCategoryImage(_iconView, categoryName);
		_categoryLabel.setText(categoryName);
		_questionLabel.setText(question);
	}
}
