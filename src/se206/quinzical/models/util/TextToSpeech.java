package se206.quinzical.models.util;

import java.io.*;

/**
 * Class that deals with text to speech stuff using espeak and festival
 *
 */
public class TextToSpeech {
	private double _speedMultiplier = 1;

	private Process executeInBackground(String... cmd) {
		try {
			return new ProcessBuilder(cmd).start();
		}
		catch (IOException e) {
			return null;
		}
	}

	public double getSpeedMultiplier() {
		return _speedMultiplier;
	}

	public void setSpeedMultiplier(double speedMultiplier) {
		_speedMultiplier = speedMultiplier;
	}

	/**
	 * Speak words using the default text to speech engine
	 *
	 * @param words words to speak
	 */
	public void speak(String words) {
		speakFestival(words);
	}

	/**
	 * Speak words using the espeak text to speech engine
	 *
	 * @param words words to speak
	 */
	public void speakEspeak(String words) {
		String wordsPerMinute = String.valueOf(Math.round(160 * _speedMultiplier));
		executeInBackground("espeak", "-s", wordsPerMinute, words);
	}

	/**
	 * Speak words using the festival text to speech engine
	 *
	 * @param words words to speak
	 */
	public void speakFestival(String words) {
		Process p = executeInBackground("festival");
		if (p != null) {
			try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()))) {
				// select custom NZ voice
				bw.write("(voice_akl_nz_jdt_diphone)");
				bw.newLine();

				// set voice speed
				bw.write(String.format("(Parameter.set 'Duration_Stretch %.2f)", 1 / getSpeedMultiplier()));
				bw.newLine();

				// speak!
				bw.write("(SayText \"" + words.replace("\"", "") + "\")");
				bw.newLine();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
