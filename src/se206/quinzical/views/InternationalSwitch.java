package se206.quinzical.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import se206.quinzical.models.QuinzicalModel;

public class InternationalSwitch extends SwitcherBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final AnswerPane _answerPane;
	private final LivesPane _livesPane;
	private final IncorrectPane _incorrectPane;
	private final CorrectPane _correctPane;

	public InternationalSwitch(QuinzicalModel model) {
		_model = model;

		// initialise screens
		_incorrectPane = new IncorrectPane(model.getPracticeModel());
		_correctPane = new CorrectPane(model.getPracticeModel());

		// make a question box, make lives pane
		_answerPane = new AnswerPane(_model.getPracticeModel());
		_livesPane = new LivesPane(_model);
		HBox.setHgrow(_answerPane.getView(), Priority.ALWAYS);

		// add them to the container
		_container.getChildren().addAll(_answerPane.getView(), _livesPane.getView());
		_container.setSpacing(48);

//		// if the game changes to other modes, make sure to empty out the international question selection
//		_model.getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());

		getView().getChildren().addAll(_container, _incorrectPane.getView(), _correctPane.getView());
		getView().setAlignment(Pos.CENTER);
		getView().setPadding(new Insets(48));
		
		_model.getPracticeModel().getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());
		onModelStateChange();

	}

	private void onModelStateChange() {
		switch (_model.getPracticeModel().getState()) {
		case SELECT_CATEGORY:
		case ANSWER_QUESTION:
			switchToView(_container);
			break;
		case INCORRECT_ANSWER:
			switchToView(_incorrectPane.getView());
			break;
		case CORRECT_ANSWER:
			switchToView(_correctPane.getView());
			break;
		default:
			break;
		}
	}


}
