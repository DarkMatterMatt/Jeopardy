package se206.quinzical.views;

import se206.quinzical.models.QuinzicalModel;

/**
 * This class is Switch type.
 * Top level view that contains everything about GUI.
 * Switches between real game/ practice/ menu.
 * 
 * Uses MenuPane, QuizPane.
 * Used by se206.quinzical.Main
 * @author hajinkim
 *
 */
public class QuinzicalSwitch extends SwitcherBase {
	private final MenuPane _menuView;
	private final QuinzicalModel _model;
	private final QuizPane _quizView;

	public QuinzicalSwitch(QuinzicalModel model) {
		_model = model;

		_menuView = new MenuPane(_model);
		_quizView = new QuizPane(_model);
		getView().getChildren().addAll(_menuView.getView(), _quizView.getView());
		addStylesheet("quinzical.css");

		onModelStateChange();
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
