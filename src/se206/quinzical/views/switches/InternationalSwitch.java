package se206.quinzical.views.switches;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.atom.Lives;
import se206.quinzical.views.base.SwitcherBase;
import se206.quinzical.views.pane.AnswerPane;
import se206.quinzical.views.pane.answerstate.CorrectPane;
import se206.quinzical.views.pane.answerstate.IncorrectPane;

public class InternationalSwitch extends SwitcherBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final VBox _answerPane = new VBox();
	private final Lives _livesPane;
	private final IncorrectPane _incorrectPane;
	private final CorrectPane _correctPane;

	public InternationalSwitch(QuinzicalModel model) {
		_model = model;

		// initialise screens
		_incorrectPane = new IncorrectPane(model.getPracticeModel());
		_correctPane = new CorrectPane(model.getPracticeModel());

		// make a question box, make lives pane
		updateAnswerPane();
		_livesPane = new Lives(_model);
		HBox.setHgrow(_answerPane, Priority.ALWAYS);
		VBox.setVgrow(_answerPane, Priority.ALWAYS);
		// add them to the container
		_container.getChildren().addAll(_answerPane, _livesPane.getView());
		_container.setSpacing(48);

//		// if the game changes to other modes, make sure to empty out the international question selection
//		_model.getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());

		getView().getChildren().addAll(_container, _incorrectPane.getView(), _correctPane.getView());
		getView().setAlignment(Pos.CENTER);
		getView().setPadding(new Insets(48));
		
		_model.getPracticeModel().getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());
		onModelStateChange();

	}

	private void updateAnswerPane() {
		_answerPane.getChildren().clear();
		VBox answerPane = new AnswerPane(_model.getPracticeModel()).getView();
		VBox.setVgrow(answerPane, Priority.ALWAYS);
		_answerPane.getChildren().add(answerPane);
	}

	private void onModelStateChange() {
		switch (_model.getPracticeModel().getState()) {
		case SELECT_CATEGORY:
		case ANSWER_QUESTION:
			updateAnswerPane();
			switchToView(_container);
			break;
		case INCORRECT_ANSWER:
			switchToView(_incorrectPane.getView());
			// change the question
			_model.getInternationalCategory()
					.setActiveQuestionInPracticeModule(_model.getInternationalCategory().getRandomQuestion());

			break;
		case CORRECT_ANSWER:
			switchToView(_correctPane.getView());
			// change the question
			_model.getInternationalCategory()
					.setActiveQuestionInPracticeModule(_model.getInternationalCategory().getRandomQuestion());

			break;
		default:
			break;
		}
	}


}
