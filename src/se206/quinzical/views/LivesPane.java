package se206.quinzical.views;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.QuizModel;

/**
 * Pane for international section, that shows remaining lives as heart icons and 
 * also shows high score & current score
 */
public class LivesPane extends ViewBase {
	private final VBox _container = new VBox();
	private final VBox _highscores = new VBox();
	private final VBox _lives = new VBox();
	private QuinzicalModel _model;
	
	public LivesPane(QuinzicalModel model) {
		_model = model;

		// add the box containing lives into the container
		_container.getChildren().addAll(_lives);
		
		
		// styles
		this.addStylesheet("lives.css");
		_container.getStyleClass().addAll("lives");
		_highscores.getStyleClass().addAll("highscore-container");
		_lives.getStyleClass().addAll("heart-container");
		
		
		updateLives();
		_model.getLivesProperty().addListener((obs,newVal,oldVal)->updateLives());
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
		
		_lives.getChildren().clear();
		for(Icon icon: hearts) {
			Parent heart = icon.getView();
			heart.getStyleClass().addAll("heart");
			_lives.getChildren().addAll(heart);
			
		}
	}
	
	@Override
	public Parent getView() {
		return _container;
	}

}
