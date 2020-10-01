package se206.quinzical.views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PracticeModel;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuinzicalModel;

/**
 * This class is Switch type.
 * Main structure of the Practice mode, excluding header view.
 * <p>
 * Used by QuizContentSwitch.
 *
 * @author hajinkim
 */
public class PracticeSwitch extends ViewBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;

	public PracticeSwitch(QuinzicalModel model) {
		_model = model;

		CategoriesList list = new CategoriesList(_model.getPracticeModel());
		SwitcherBase content = new PracticeSwitcher(_model.getPracticeModel());
		//center the answer view
		VBox listContainer = new VBox(list.getView());
		HBox.setHgrow(content.getView(), Priority.ALWAYS);
		listContainer.getStyleClass().add("practice");

		addStylesheet("practice.css");
		_container.getChildren().addAll(listContainer, content.getView());
	}

	@Override
	public HBox getView() {
		return _container;
	}
}

class PracticeSwitcher extends SwitcherBase {
	private final CorrectPane _correctPane;
	private final IncorrectPane _incorrectPane;
	private final HBox _nothingChosen;
	private final PracticeModel _practiceModel;
	// incorrect, correct, question asking
	private final AnswerPane _answerPane;

	public PracticeSwitcher(PracticeModel practiceModel) {
		_practiceModel = practiceModel;

		// initialise possible views
		_answerPane = new AnswerPane(_practiceModel);
		_correctPane = new CorrectPane(_practiceModel);
		_incorrectPane = new IncorrectPane(_practiceModel);
		_nothingChosen = new HBox();

		getView().getChildren().addAll(_answerPane.getView(), _correctPane.getView(), _incorrectPane.getView(), _nothingChosen);

		// add state change listener
		onModelStateChange();
		_practiceModel.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch (_practiceModel.getState()) {
			case SELECT_CATEGORY:
				switchToView(_nothingChosen);
				Question q = _practiceModel.getCurrentQuestion();
				if (q != null) {
					_practiceModel.selectCategory(q.getCategory());
				}
				break;
			case INCORRECT_ANSWER:
				switchToView(_incorrectPane.getView());
				break;
			case CORRECT_ANSWER:
				switchToView(_correctPane.getView());
				break;
			case RETRY_INCORRECT_ANSWER:
				_answerPane.flashAnswerIncorrect(ev -> {
					_answerPane.clearInput();
					_answerPane.setHintVisible(_practiceModel.getCurrentQuestion().getNumAttempted() >= 2);
				});
				break;
			case ANSWER_QUESTION:
				switchToView(_answerPane.getView());
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
