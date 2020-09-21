package se206.quinzical.models.util;

import java.io.IOException;

public class TextToSpeech {
	private double _speedMultiplier = 1;

	private void executeInBackground(String... cmd) {
		try {
			new ProcessBuilder(cmd).start();
		} catch (IOException e) {
			//
		}
	}

	public double getSpeedMultiplier() {
		return _speedMultiplier;
	}

	public void setSpeedMultiplier(double speedMultiplier) {
		_speedMultiplier = speedMultiplier;
	}

	/**
	 * Speak words using the espeak text to speech engine
	 *
	 * @param words words to speak
	 */
	public void speak(String words) {
		String wordsPerMinute = String.valueOf(Math.round(160 * _speedMultiplier));
		executeInBackground("espeak", "-s", wordsPerMinute, words);
	}
}
