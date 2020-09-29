package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import se206.quinzical.models.QuinzicalModel;

/**
 * View for taskbar. Contains reset & quit buttons
 */
public class TaskbarView extends View {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final ImageView _reset;
	private final StackPane _toggleText;

	public TaskbarView(QuinzicalModel model) {
		_model = model;

		// exit button view
		ImageView exit = createButton("../assets/exit.png");
		exit.setOnMouseClicked(e -> Platform.exit());
		Tooltip.install(exit, new Tooltip("Quit"));

		// reset button view
		_reset = createButton("../assets/reset.png");
		_reset.setOnMouseClicked(e -> model.reset());
		Tooltip.install(_reset, new Tooltip("Reset game"));
		
		// home button view
		ImageView home = createButton("../assets/home.png");
		home.setOnMouseClicked(e->{
			//change state to menu
			model.backToMainMenu();
		});
		Tooltip.install(home, new Tooltip("Main Menu"));
		
		// text and notext icons
		HBox text = new HBox(createButton("../assets/text.png"));
		HBox noText = new HBox(createButton("../assets/notext.png"));
		
		SwitcherView s = new SwitcherView() {};
		s.getView().getChildren().addAll(text,noText);
		
		// enable text
		_toggleText = s.getView();
		_toggleText.setOnMouseClicked(e->{
			System.out.println("toggled to "+ model.textVisible());
			s.switchToView(model.textVisible()?text:noText);;
			model.toggleTextVisibility();
		});
		Tooltip.install(text, new Tooltip("Text Currently Visible"));
		Tooltip.install(noText, new Tooltip("Text Currently Invisible"));

		_container.getChildren().addAll(_toggleText, home,_reset, exit);
		_container.setSpacing(10);
		_container.getStyleClass().add("taskbar");
        addStylesheet("taskbar.css");

		// show reset button ONLY during game state
		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private ImageView createButton(String filename) {
		ImageView v = new ImageView(new Image(getClass().getResourceAsStream(filename)));
		v.setFitHeight(32);
		v.setPreserveRatio(true);
		v.setSmooth(true);
		v.setCache(true);
		v.setPickOnBounds(true);
		v.getStyleClass().add("btn");
		return v;
	}

	private void onModelStateChange() {
		// show reset button ONLY during game state
		switch (_model.getState()) {
			case GAME:
				_reset.setVisible(true);
				_reset.setManaged(true);
				break;
			case MENU:
			case PRACTICE:
				_reset.setVisible(false);
				_reset.setManaged(false);
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
	public Pane getView() {
		return _container;
	}
}
