package se206.quinzical.views.switches;

import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.base.SwitcherBase;
import se206.quinzical.views.pane.LeaderboardPane;
import se206.quinzical.views.pane.ThemeSelectPane;

/**
 * This class is switch type.
 * Switches between real game and practice game upon request.
 */
public class QuizContentSwitch extends SwitcherBase {
	private final GameSwitch _gameView;
	private final QuinzicalModel _model;
	private final PracticeSwitch _practiceView;
	private final InternationalSwitch _internationalView;
	private final LeaderboardPane _leaderboardPane;
	private final ThemeSelectPane _themeSelectPane;

	public QuizContentSwitch(QuinzicalModel model) {
		_model = model;

		_gameView = new GameSwitch(_model);
		_practiceView = new PracticeSwitch(_model);
		_internationalView = new InternationalSwitch(_model);
		_leaderboardPane = new LeaderboardPane(_model.getLeaderboardModel());
		_themeSelectPane = new ThemeSelectPane(_model);

		getView().getChildren().addAll(_gameView.getView(), _practiceView.getView(), _internationalView.getView(), _leaderboardPane.getView(), _themeSelectPane.getView());

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
			case INTERNATIONAL:
				switchToView(_internationalView.getView());
				break;
			case LEADERBOARD:
				switchToView(_leaderboardPane.getView());
				break;
			case THEME_SELECT:
				switchToView(_themeSelectPane.getView());
				break;
			case MENU:
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
