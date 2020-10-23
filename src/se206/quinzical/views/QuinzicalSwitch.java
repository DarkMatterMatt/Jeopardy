package se206.quinzical.views;

import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.util.KeyboardShortcuts;

import java.util.Arrays;

/**
 * This class is Switch type.
 * Top level view that contains everything about GUI.
 * Switches between real game/ practice/ menu.
 * <p>
 * Uses MenuPane, QuizPane.
 * Used by se206.quinzical.Main
 *
 * @author hajinkim
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

		// listen for theme changes
		getView().getStyleClass().add(_model.getTheme().getThemeClass());
		_model.getThemeProperty().addListener((obs, old, val) -> {
			if (old != null) getView().getStyleClass().remove(old.getThemeClass());
			if (val != null) getView().getStyleClass().add(val.getThemeClass());
		});

		// add home/menu shortcut
		KeyboardShortcuts.addKeyboardShortcut(ev -> {
			_model.backToMainMenu();
		}, KeyboardShortcuts.HOME);
	}

	private void onModelStateChange() {
		switch (_model.getState()) {
			case GAME:
			case PRACTICE:
			case INTERNATIONAL:
			case LEADERBOARD:
			case THEME_SELECT:
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
