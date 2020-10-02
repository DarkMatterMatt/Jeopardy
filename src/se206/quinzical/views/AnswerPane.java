package se206.quinzical.views;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.*;

/**
 * This class is a Pane type, and uses AnswerTextField atom.
 * Represents the answer screen for when a question has been selected.
 * <p>
 * This type is 'Pane' type, which means it depends on Atoms and may depend
 * on other Panes.
 */
public class AnswerPane extends ViewBase {
	private final AnswerTextField _answerInputView;
	private final Label _categoryLabel = new Label();
	private final VBox _container = new VBox();
	private final HBox _hintBox;
	private final Label _hintText = new Label();
	private final Icon _iconView = new Icon();
	private final QuizModel _model;
	private final Label _questionLabel = new Label();
	private transient Question _currentQuestion = null;
	private final Icon _repeatIcon;

	public AnswerPane(QuizModel model) {
		_model = model;
		_answerInputView = new AnswerTextField(_model);

		Label hintDisplayedLabel = new Label("Hint Displayed  ");
		hintDisplayedLabel.getStyleClass().addAll("text-small", "text-white");
		Icon hintIcon = new Icon("../assets/light.png");

		_hintBox = new HBox(hintDisplayedLabel, hintIcon.getView());
		_hintBox.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(_hintBox, Priority.ALWAYS);

		_hintText.getStyleClass().addAll("hint-text", "text-white", "text-small");

		HBox categoryContainer = new HBox(_iconView.getView(), _categoryLabel, _hintBox);
		categoryContainer.getStyleClass().add("category-container");

		_categoryLabel.getStyleClass().addAll("text-bold", "text-gold", "category");
		_questionLabel.getStyleClass().addAll("text-white", "question");

		_repeatIcon = new Icon("../assets/volume-up.png")
				.setSize(40, 40);
		_repeatIcon.addClass("repeat-question", "btn");
		_repeatIcon.getView().setOnMouseClicked(ev -> _model.getTextToSpeech().speak(_model.getCurrentQuestion().getQuestion()));
		Tooltip.install(_repeatIcon.getView(), new Tooltip("Repeat question"));

		HBox questionContainer = new HBox(_repeatIcon.getView(), _questionLabel);
		questionContainer.setFillHeight(false);
		questionContainer.getStyleClass().add("question-container");

		_iconView
				.setSize(56, 56)
				.addClass("category-icon");

		// add container styles
		addStylesheet("answer.css");
		_container.getStyleClass().add("answer");
		_container.getChildren().addAll(categoryContainer, questionContainer, _answerInputView.getView(), _hintText);

		// reload screen when we are made visible
		onVisibilityChanged();
		_container.visibleProperty().addListener((observable, oldVal, newVal) -> onVisibilityChanged());
		_model.getModel().getStateProperty().addListener((obs, old, val) -> onVisibilityChanged());

		// check visibility of text
		_model.getTextVisibleProperty().addListener((obs, old, val) -> onVisibilityChanged());
	}

	public void clearInput() {
		_answerInputView.clear();
	}

	public void flashAnswerIncorrect(javafx.event.EventHandler<javafx.event.ActionEvent> onFinished) {
		_answerInputView.flashAnswerIncorrect(onFinished);
	}

	public void focusInput() {
		_answerInputView.focus();
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
		if (_model instanceof PresetQuinzicalModel && _model.getModel().getState() != QuinzicalModel.State.GAME
				|| _model instanceof PracticeModel && _model.getModel().getState() != QuinzicalModel.State.PRACTICE) {
			// this game is not active
			return;
		}
		// container was made visible
		questionUpdate(_model.getCurrentQuestion());
	}

	/**
	 * Update display to show question details
	 */
	private void questionUpdate(Question q) {
		setHintVisible(false);
		clearInput();
		focusInput();

		if (q == null) {
			_categoryLabel.setText("Welp");
			_questionLabel.setText("There is no available question in this category");
			_answerInputView.getView().setVisible(false);
			_repeatIcon.getView().setVisible(false);
			_model.skinCategoryImage(_iconView, "icon-missing.png");
			return;
		}

		if (q != _currentQuestion) {
			// question changed, speak it
			_model.getTextToSpeech().speak(q.getQuestion());
			_currentQuestion = q;
		}

		String categoryName = (q.getCategory() != null) ? q.getCategory().getName() : "";
		String question = _model.getTextVisibility() ? q.getQuestion() : "Text is currently invisible, press the 'T' in the taskbar to toggle text visibility!";

		_model.skinCategoryImage(_iconView, categoryName);
		_categoryLabel.setText(categoryName);
		_answerInputView.getView().setVisible(true);
		_repeatIcon.getView().setVisible(true);
		_questionLabel.setText(question);
		_hintText.setText("Hint: the answer starts with letter " + q.getAnswer().get(0).charAt(0));
	}

	public void setHintVisible(boolean visible) {
		_hintBox.setVisible(visible);
		_hintText.setVisible(visible);
	}
}
