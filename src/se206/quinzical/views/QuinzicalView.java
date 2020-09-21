package se206.quinzical.views;

import se206.quinzical.models.QuinzicalModel;

public class QuinzicalView extends SwitcherView {
	private final MenuView _menuView;
	private final QuinzicalModel _model;
	private final QuizView _quizView;

	public QuinzicalView(QuinzicalModel model) {
		_model = model;

		_menuView = new MenuView();
		_quizView = new QuizView(_model);
		getView().getChildren().addAll(_menuView.getView(), _quizView.getView());
		addStylesheet("quinzical.css");

		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch (_model.getState()) {
			case GAME:
			case PRACTICE:
				switchToView(_quizView.getView());
				break;
			case MENU:
				switchToView(_menuView.getView());
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
