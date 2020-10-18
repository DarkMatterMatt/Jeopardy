package se206.quinzical.models.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Class that deals with text to speech stuff using espeak and festival
 */
public class TextToSpeech {
	private transient final AtomicReference<Process> _activeTTS = new AtomicReference<>();
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
		speak(words, null);
	}

	public void speak(String words, Consumer<Process> callback) {
		speakFestival(words, callback);
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
		speakFestival(words, null);
	}

	public void speakFestival(String words, Consumer<Process> callback) {
		Process p;
		try {
			p = new ProcessBuilder("festival").start();
		}
		catch (IOException e) {
			return;
		}

		// kill any existing speaking
		Process oldP = _activeTTS.getAndSet(p);
		if (oldP != null) {
			oldP.descendants().forEach(ProcessHandle::destroy);
			oldP.destroy();
		}

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

		p.onExit().thenAccept(p2 -> {
			_activeTTS.compareAndSet(p, null);

			// execute callback
			if (callback != null) {
				callback.accept(p);
			}
		});
	}
}
