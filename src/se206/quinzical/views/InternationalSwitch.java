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

	public InternationalSwitch(QuinzicalModel model) {
		_model = model;
		// make a question box, make lives pane
		_answerPane = new AnswerPane(_model.getPracticeModel());
		_livesPane = new LivesPane(_model);
		HBox.setHgrow(_answerPane.getView(), Priority.ALWAYS);

		// add them to the container
		_container.getChildren().addAll(_answerPane.getView(), _livesPane.getView());
		_container.setSpacing(48);

		// if the game changes to other modes, make sure to empty out the international question selection
		_model.getStateProperty().addListener((obs, newV, oldV) -> onModelStateChange());
		
		getView().getChildren().addAll(_container);
		getView().setAlignment(Pos.CENTER);
		getView().setPadding(new Insets(48));
	}

	private void onModelStateChange() {

	}


}
