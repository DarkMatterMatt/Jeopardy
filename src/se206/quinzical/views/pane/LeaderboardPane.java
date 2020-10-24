package se206.quinzical.views.pane;

import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import se206.quinzical.models.LeaderboardModel;
import se206.quinzical.views.base.ViewBase;

import java.util.List;

/**
 * This class is Pane type.
 * View for current leaderboard
 * <p>
 * Used by GameSwitch and PracticeSwitch.
 */
public class LeaderboardPane extends ViewBase {
	private static final int MAX_ROWS = 7;
	private static final Image TROPHY_BRONZE = new Image(LeaderboardPane.class.getResourceAsStream("/se206/quinzical/assets/trophy-bronze.png"));
	private static final Image TROPHY_GOLD = new Image(LeaderboardPane.class.getResourceAsStream("/se206/quinzical/assets/trophy-gold.png"));
	private static final Image TROPHY_SILVER = new Image(LeaderboardPane.class.getResourceAsStream("/se206/quinzical/assets/trophy-silver.png"));
	private final GridPane _grid = new GridPane();
	private final VBox _container = new VBox(_grid);
	private final LeaderboardModel _model;

	public LeaderboardPane(LeaderboardModel model) {
		_model = model;

		// add column withs in a 80/80/fill split
		ColumnConstraints rankConstraints = new ColumnConstraints();
		rankConstraints.setPrefWidth(80);
		ColumnConstraints scoreConstraints = new ColumnConstraints();
		scoreConstraints.setPrefWidth(80);
		ColumnConstraints nameConstraints = new ColumnConstraints();
		nameConstraints.setHgrow(Priority.ALWAYS);
		nameConstraints.setMaxWidth(240);
		_grid.getColumnConstraints().addAll(rankConstraints, scoreConstraints, nameConstraints);

		// add event listeners
		update();
		_model.getSortedScores().addListener((ListChangeListener<LeaderboardModel.Entry>) ev -> update());

		// add styles
		addStylesheet("leaderboard.css");
		_container.getStyleClass().add("leaderboard");
		_grid.getStyleClass().add("grid");
		_grid.setAlignment(Pos.CENTER);
	}

	private ImageView createImageView(Image image) {
		ImageView iv = new ImageView(image);
		iv.setFitWidth(32);
		iv.setPreserveRatio(true);
		iv.setSmooth(true);
		iv.setCache(true);
		GridPane.setHalignment(iv, HPos.CENTER);
		GridPane.setValignment(iv, VPos.CENTER);
		return iv;
	}

	private Text createText(String text, String... classes) {
		Text t = new Text(text);
		t.getStyleClass().addAll(classes);
		GridPane.setHalignment(t, HPos.CENTER);
		GridPane.setValignment(t, VPos.CENTER);
		return t;
	}

	private Text createText(String text) {
		return createText(text, "text-white");
	}

	public VBox getView() {
		return _container;
	}

	private void update() {
		// clear grid & reinitialize - very inefficient, but easy
		_grid.getChildren().clear();
		_grid.addRow(0,
				createText("rank", "text-white", "text-bold"),
				createText("score", "text-white", "text-bold"),
				createText("name", "text-white", "text-bold"));

		// add scores
		List<LeaderboardModel.Entry> scores = _model.getSortedScores();
		for (int i = 1; i <= MAX_ROWS; i++) {
			// create rank image/text (trophies for top three)
			Node rank = createText(Integer.toString(i));
			if (i == 1) rank = createImageView(TROPHY_GOLD);
			else if (i == 2) rank = createImageView(TROPHY_SILVER);
			else if (i == 3) rank = createImageView(TROPHY_BRONZE);

			// default to blank name & score
			if (i > scores.size()) {
				_grid.addRow(i, rank, createText(""), createText(""));
				continue;
			}

			// add name & score
			LeaderboardModel.Entry entry = scores.get(i - 1);
			Text score = createText("$" + entry.score, i == 1 ? "text-gold" : "text-white");
			Text name = createText(entry.name, i == 1 ? "text-gold" : "text-white");
			_grid.addRow(i, rank, score, name);
		}
	}
}
