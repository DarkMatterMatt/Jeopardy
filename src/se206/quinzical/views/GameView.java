package se206.quinzical.views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.QuinzicalModel;

/**
 * Main content layout for the 'real' game
 */
public class GameView extends SwitcherView {
	private final AnswerView _answerQuestion;
	private final CorrectView _correctPane;
	private final IncorrectView _incorrectPane;
	private final QuinzicalModel _model;
	private final PresetQuinzicalModel _presetModel;
	private final HBox _questionSelectContainer = new HBox();

	public GameView(QuinzicalModel model) {
		_model = model;
		_presetModel = _model.getPresetModel();

		// create primary views
		_answerQuestion = new AnswerView(_presetModel);
		_correctPane = new CorrectView(_presetModel);
		_incorrectPane = new IncorrectView(_presetModel);

		CategoriesListView categoriesListPane = new CategoriesListView(_presetModel);
		CategoryPreview categoryPreviewPane = new CategoryPreview(_presetModel);

		// categoryPreviewPane is centered inside its container
		VBox categoryPreviewContainer = new VBox(categoryPreviewPane.getView());
		HBox.setHgrow(categoryPreviewContainer, Priority.ALWAYS);
		categoryPreviewContainer.getStyleClass().add("category-preview-container");

		// question selection includes the list of categories & current category preview
		_questionSelectContainer.getChildren().addAll(categoriesListPane.getView(), categoryPreviewContainer);

		// add styles
		addStylesheet("game.css");
		getView().getStyleClass().add("game");
		getView().getChildren().addAll(_questionSelectContainer, _answerQuestion.getView(), _correctPane.getView(), _incorrectPane.getView());

		// listen for state changes
		onModelStateChange();
		_model.getPracticeModel().getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch (_presetModel.getState()) {
			case SELECT_CATEGORY:
				switchToView(_questionSelectContainer);
				break;
			case INCORRECT_ANSWER:
				switchToView(_incorrectPane.getView());
				break;
			case CORRECT_ANSWER:
				switchToView(_correctPane.getView());
				break;
			case ANSWER_QUESTION:
				switchToView(_answerQuestion.getView());
				break;
			case GAME_OVER:
				// tell model that we're finished
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
