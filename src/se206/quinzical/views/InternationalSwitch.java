package se206.quinzical.views;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import se206.quinzical.models.QuinzicalModel;

public class InternationalSwitch extends SwitcherBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final AnswerPane _answerPane;
	private final LivesPane _livesPane;

	public InternationalSwitch(QuinzicalModel model) {
		_model = model;
		// make a question box
		_answerPane = new AnswerPane(_model.getPracticeModel());
		
		// make lives pane
		_livesPane = new LivesPane(_model);

		HBox.setHgrow(_answerPane.getView(), Priority.ALWAYS);

		// add it to container
		_container.getChildren().addAll(_answerPane.getView(), _livesPane.getView());

		// if the game changes to other modes, make sure to empty out the international question selection
		_model.getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());
		
		getView().getChildren().addAll(_container);
		getView().setAlignment(Pos.CENTER);
	}

	private void onModelStateChange() {

	}


}
