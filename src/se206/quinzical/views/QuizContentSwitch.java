package se206.quinzical.views;

import se206.quinzical.models.QuinzicalModel;

/**
 * This class is switch type.
 * Switches between real game and practice game upon request.
 */
public class QuizContentSwitch extends SwitcherBase {
	private final GameSwitch _gameView;
	private final QuinzicalModel _model;
	private final PracticeSwitch _practiceView;

	public QuizContentSwitch(QuinzicalModel model) {
		_model = model;

		_gameView = new GameSwitch(_model);
		_practiceView = new PracticeSwitch(_model);
		getView().getChildren().addAll(_gameView.getView(), _practiceView.getView());

		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch (_model.getState()) {
			case GAME:
				switchToView(_gameView.getView());
				break;
			case PRACTICE:
				switchToView(_practiceView.getView());
				break;
			case MENU:
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
