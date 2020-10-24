package se206.quinzical.views;

import javafx.animation.AnimationTimer;
import javafx.scene.control.ProgressBar;

import java.util.function.Consumer;

/**
 * This class is Atom type.
 * Visually shows user the time left for transition screen
 * Used by IncorrectPane, CorrectPane
 */
public class AnimatedProgressBar extends ViewBase {
	private final Consumer<AnimatedProgressBar> _onFinishedListener;
	private final ProgressBar _progressBar = new ProgressBar(0);
	private final AnimationTimer _progressBarTimer;
	private final double _timeoutSecs;

	/**
	 * Create a new animated progress bar
	 * @param timeoutSecs total number of seconds to fill the progress bar
	 * @param onFinishedListener callback function to execute on completion
	 */
	public AnimatedProgressBar(double timeoutSecs, Consumer<AnimatedProgressBar> onFinishedListener) {
		_timeoutSecs = timeoutSecs;
		_onFinishedListener = onFinishedListener;
		_progressBarTimer = new AnimationTimer() {
			private long startTime;

			@Override
			public void handle(long now) {
				double elapsedSecs = (now - startTime) / 1e9;
				double progress = elapsedSecs / _timeoutSecs;
				_progressBar.setProgress(progress);

				if (progress >= 1) {
					// progress bar is full
					stop();
					onAnimatedFinished();
				}
			}

			@Override
			public void stop() {
				super.stop();
				_progressBar.setProgress(0);
			}

			@Override
			public void start() {
				super.start();
				_progressBar.setProgress(0);
				startTime = System.nanoTime();
			}
		};

		// add styles
		_progressBar.getStyleClass().add("animated-progress-bar");
		addStylesheet("animated-progress-bar.css");
	}

	@Override
	public ProgressBar getView() {
		return _progressBar;
	}

	/**
	 * Called when progress bar is full
	 */
	private void onAnimatedFinished() {
		_onFinishedListener.accept(this);
	}

	/**
	 * Start animation
	 */
	public void start() {
		_progressBarTimer.stop();
		_progressBarTimer.start();
	}

	/**
	 * Stop and reset animation
	 */
	public void stop() {
		_progressBarTimer.stop();
	}
}
