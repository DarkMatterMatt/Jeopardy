package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;

/**
 * This class is Pane type, and it does not contain any atoms.
 * It is the main menu which is shown when the game is first loaded.
 * <p>
 * Used by QuinzicalSwitch
 */
public class MenuPane extends ViewBase {
	private final VBox _container = new VBox();
	private final Label _internationalLabel = new Label("International");
	private final QuinzicalModel _model;

	public MenuPane(QuinzicalModel model) {
		_model = model;

		// big main title
		Label titleLabel = new Label("Quinzical");
		titleLabel.getStyleClass().addAll("title", "text-bold", "text-gold");

		// play button
		Label playLabel = new Label("Play");
		playLabel.getStyleClass().addAll("btn", "text-gold");
		playLabel.setOnMouseClicked(e -> model.beginGame());

		// practice button
		Label practiceLabel = new Label("Practice");
		practiceLabel.getStyleClass().addAll("btn", "text-gold");
		practiceLabel.setOnMouseClicked(e -> model.beginPracticeGame());

		// international button
		_internationalLabel.getStyleClass().addAll("btn", "text-gold");
		_internationalLabel.setOnMouseClicked(e -> model.beginInternationalGame());

		// show leaderboard button
		Label leaderboardLabel = new Label("Leaderboard");
		leaderboardLabel.getStyleClass().addAll("btn", "text-gold");
		leaderboardLabel.setOnMouseClicked(e -> model.showLeaderboard());

		// show theme selection button
		Label themesLabel = new Label("Themes");
		themesLabel.getStyleClass().addAll("btn", "text-gold");
		themesLabel.setOnMouseClicked(e -> model.showThemeSelection());

		// quit button
		Label quitLabel = new Label("Quit");
		quitLabel.getStyleClass().addAll("btn", "text-gold");
		quitLabel.setOnMouseClicked(e -> Platform.exit());

		// add styles
		addStylesheet("menu.css");
		_container.getStyleClass().add("menu");
		_container.getChildren().addAll(titleLabel, playLabel, practiceLabel, _internationalLabel, leaderboardLabel, themesLabel, quitLabel);

		// listen to state changes
		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	@Override
	public VBox getView() {
		return _container;
	}

	private void onModelStateChange() {
		// only update when this screen is shown
		if (_model.getState() != QuinzicalModel.State.MENU) return;

		// grey internation button out when disabled
		_internationalLabel.setOpacity(_model.checkInternationalSectionCanStart() ? 1 : 0.4);
	}
}
