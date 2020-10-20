package se206.quinzical.views;

import javafx.scene.layout.GridPane;
import se206.quinzical.models.LeaderboardModel;

/**
 * This class is Pane type.
 * View for current leaderboard
 * <p>
 * Used by GameSwitch and PracticeSwitch.
 */
public class LeaderboardPane extends ViewBase {
	private static final int MAX_ROWS = 7;
	private final GridPane _container = new GridPane();
	private final LeaderboardModel _model;

	public LeaderboardPane(LeaderboardModel model) {
		_model = model;
	}

	public GridPane getView() {
		return _container;
	}
}
