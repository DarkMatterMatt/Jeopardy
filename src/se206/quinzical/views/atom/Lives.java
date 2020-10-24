package se206.quinzical.views.atom;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.base.ViewBase;

/**
 * Atom that represents lives for international section, that shows remaining
 * lives as heart icons and also shows high score & current score
 */
public class Lives extends ViewBase {
	private final VBox _container = new VBox();
	private final VBox _highscoreBox = new VBox();
	private final VBox _livesBox = new VBox();

	private QuinzicalModel _model;

	public Lives(QuinzicalModel model) {
		_model = model;

		// add the box containing lives into the container
		_container.getChildren().addAll(_highscoreBox, _livesBox);
		_highscoreBox.setSpacing(10);
		_container.setSpacing(40);

		// styles
		this.addStylesheet("lives.css");
		_container.getStyleClass().addAll("lives");
		_highscoreBox.getStyleClass().addAll("highscore-container");
		_livesBox.getStyleClass().addAll("heart-container");

		updateScore();
		updateLives();
		// add score property
		_model.getLivesProperty().addListener((obs, newVal, oldVal) -> updateLives());
		_model.getScoreProperty().addListener((obs, n, o) -> updateScore());
	}

	public void updateScore() {
		_highscoreBox.getChildren().clear();

		Label highscore = new Label("High:");
		Label hscore = new Label("" + _model.getInternationalHighScore());
		Label yourscore = new Label("You:");
		Label yscore = new Label("" + _model.getInternationalScore());

		_highscoreBox.setAlignment(Pos.CENTER);
		highscore.getStyleClass().addAll("text-white", "text-medium", "text-bold");
		yourscore.getStyleClass().addAll("text-white", "text-medium", "text-bold");
		hscore.getStyleClass().addAll("text-white", "text-medium");
		yscore.getStyleClass().addAll("text-white", "text-medium");
		_highscoreBox.getChildren().addAll(highscore, hscore, yourscore, yscore);


	}

	public void updateLives() {
		// make as many icons as the maximum number of lives
		// how many of them are filled depends on the current number of lives
		int currentLives = _model.getLivesProperty().get();
		int lostLives = _model.getMaxLives()-currentLives;

		// make array for icons
		List<Icon> hearts = new ArrayList<Icon>();


		for(int i = 0; i<currentLives; i++) {
			// make filled hearts icons
			hearts.add(new Icon("/se206/quinzical/assets/filledHeart.png"));
			// can you use the same image multiple times?
		}
		for(int i = 0; i<lostLives; i++) {
			// make empty hearts icons
			hearts.add(new Icon("/se206/quinzical/assets/emptyHeart.png"));
		}

		_livesBox.getChildren().clear();
		for(Icon icon: hearts) {
			Parent heart = icon.getView();
			heart.getStyleClass().addAll("heart");
			_livesBox.getChildren().addAll(heart);

		}
	}

	@Override
	public VBox getView() {
		return _container;
	}

}
