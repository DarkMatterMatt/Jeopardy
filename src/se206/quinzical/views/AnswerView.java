package se206.quinzical.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
	private final AnswerInputView _answerInputView;
	private final HBox _hintBox;

	public AnswerView(QuizModel model) {
		_model = model;
		_answerInputView = new AnswerInputView(_model);
		
		Label label = new Label("Hint Displayed  ");
		label.getStyleClass().addAll("text-small", "text-white");
		IconView hintIcon = new IconView();
		hintIcon.setImage("../assets/light.png");

		_hintBox = new HBox(label, hintIcon.getView());

		HBox categoryContainer = new HBox(_iconView.getView(), _categoryLabel, _hintBox);
		_hintBox.setPrefWidth(200);
		_hintBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(_hintBox, Priority.ALWAYS);
		categoryContainer.getStyleClass().add("category-container");

		_categoryLabel.getStyleClass().addAll("text-bold", "text-gold", "category");
		_questionLabel.getStyleClass().addAll("text-white", "question");

		_iconView
				.setSize(56, 56)
				.addClass("category-icon");

		// add container styles
		addStylesheet("answer.css");
		_container.getStyleClass().add("answer");
		_container.getChildren().addAll(categoryContainer, _questionLabel, _answerInputView.getView());

		// reload screen when we are made visible
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());

		// check visibility of text
		_model.getTextVisibleProperty().addListener((obs, old, val) -> onVisibilityChanged());
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
		if (q == null) {
			_hintBox.setVisible(false);
			_categoryLabel.setText("Welp");
			_questionLabel.setText("There is no available question in this category");
			_answerInputView.getView().setVisible(false);
			return;
		}

		String categoryName = q.getCategory().getName();
		String question = _model.getTextVisibility()?q.getQuestion():"====Text is currently set to invisible====\nIf you hate listening test, consider pressing the 'T' button above";

		_model.skinCategoryImage(_iconView, categoryName);
		_categoryLabel.setText(categoryName);
		_answerInputView.getView().setVisible(true);

		if(q.getNumAttempted()==2) {
			_hintBox.setVisible(true);
			_questionLabel.setText(question+"\n\n"+"Hint: the answer starts with letter "+q.getAnswer().charAt(0));
		}else {
			_hintBox.setVisible(false);
			_questionLabel.setText(question);
		}
	}
}
