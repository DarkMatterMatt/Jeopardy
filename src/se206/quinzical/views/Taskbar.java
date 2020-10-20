package se206.quinzical.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.util.TextToSpeech;

/**
 * This is Atom type.
 * View for taskbar. Contains reset & quit button, and other various functions
 * <p>
 * Used by HeaderView.
 */
public class Taskbar extends ViewBase {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;
	private final ImageView _reset;
	private final Timeline _speedSliderHideTimer = new Timeline();
	private final StackPane _toggleText;

	public Taskbar(QuinzicalModel model) {
		_model = model;

		// exit button view
		ImageView exit = createButton("/se206/quinzical/assets/exit.png");
		exit.setOnMouseClicked(e -> AlertFactory.getExitAlert(this));
		Tooltip.install(exit, new Tooltip("Quit"));

		// reset button view
		_reset = createButton("/se206/quinzical/assets/reset.png");
		_reset.setOnMouseClicked(e -> AlertFactory.getResetAlert(this, model));
		Tooltip.install(_reset, new Tooltip("Reset game"));

		// home button view
		ImageView home = createButton("/se206/quinzical/assets/home.png");
		home.setOnMouseClicked(e -> {
			TextToSpeech.getInstance().cancel();
			model.backToMainMenu();
		});
		Tooltip.install(home, new Tooltip("Main Menu"));

		// text and notext icons
		HBox text = new HBox(createButton("/se206/quinzical/assets/text.png"));
		HBox noText = new HBox(createButton("/se206/quinzical/assets/notext.png"));

		SwitcherBase s = new SwitcherBase() {};
		s.getView().getChildren().addAll(text, noText);

		// enable text
		_toggleText = s.getView();
		s.switchToView(model.textVisible() ? text : noText);
		_toggleText.setOnMouseClicked(e -> {
			model.toggleTextVisibility();
			s.switchToView(model.textVisible() ? text : noText);
		});
		Tooltip.install(text, new Tooltip("Text Currently Visible"));
		Tooltip.install(noText, new Tooltip("Text Currently Invisible"));

		// text speed slider
		Slider speedSlider = createSpeedSlider();

		_container.getChildren().addAll(speedSlider, _toggleText, home, _reset, exit);
		_container.setSpacing(10);
		_container.getStyleClass().add("taskbar");
		addStylesheet("taskbar.css");

		// show reset button ONLY during game state
		onModelStateChange();
		_model.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	/**
	 * Create an icon button given an image file name.
	 */
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

	/**
	 * Slider to change speed of text speech
	 */
	private Slider createSpeedSlider() {
		Slider slider = new Slider(-1, 1, 0);
		Tooltip tooltip = new Tooltip();
		Tooltip.install(slider, tooltip);

		_speedSliderHideTimer.setOnFinished(ev -> {
			tooltip.hide();
			_model.save();
		});

		slider.valueProperty().addListener((obs, old, num) -> {
			double val = num.doubleValue();
			if (val != 0 && Math.abs(val) < 0.1) {
				// snap to zero
				slider.setValue(0);
				return;
			}
			// change value so it is 0.5x speed on the left, 1x speed in the middle, 2x speed on the right
			double speed = 0.25 * val * val + 0.75 * val + 1;
			_model.getTextToSpeech().setSpeedMultiplier(speed);

			// update tooltip, 600ms display duration
			tooltip.setText("Speech speed: " + Math.round(speed * 100) / 100.0 + "x");
			if (tooltip.getOwnerWindow() != null) {
				if (!tooltip.isShowing()) {
					tooltip.show(tooltip.getOwnerWindow());
				}
				_speedSliderHideTimer.getKeyFrames().setAll(new KeyFrame(Duration.millis(600)));
				_speedSliderHideTimer.playFromStart();
			}
		});

		// not-excellent way of reversing our quadratic transformation
		double savedSpeedMultiplier = _model.getTextToSpeech().getSpeedMultiplier();
		double savedSpeed = -1;
		while (0.25 * savedSpeed * savedSpeed + 0.75 * savedSpeed + 1 < savedSpeedMultiplier) {
			savedSpeed += 0.01;
		}
		slider.setValue(savedSpeed);

		return slider;
	}

	public Pane getView() {
		return _container;
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
			case INTERNATIONAL:
			case LEADERBOARD:
				_reset.setVisible(false);
				_reset.setManaged(false);
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
