package se206.quinzical.views;

import se206.quinzical.models.QuinzicalModel;

public class QuizContentView extends SwitcherView {
	private final GameView _gameView;
	private final QuinzicalModel _model;
	private final PracticeView _practiceView;

	public QuizContentView(QuinzicalModel model) {
		_model = model;

		_gameView = new GameView(_model);
		_practiceView = new PracticeView(_model);
		getView().getChildren().addAll(_gameView.getView(), _practiceView.getView());

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
