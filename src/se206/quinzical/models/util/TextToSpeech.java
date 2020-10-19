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
	private static final TextToSpeech _instance = new TextToSpeech();
	private transient final AtomicReference<Process> _activeTTS = new AtomicReference<>();
	private double _speedMultiplier = 1;

	private TextToSpeech() {
	}

	/**
	 * Returns the global TextToSpeech instance
	 * <p>
	 * This should only be stored in `transient` fields to avoid being persisted when the parent class is serialized
	 */
	public static TextToSpeech getInstance() {
		return _instance;
	}

	/**
	 * Immediately stop text to speech
	 */
	public void cancel() {
		Process oldP = _activeTTS.get();
		if (oldP != null) {
			oldP.descendants().forEach(ProcessHandle::destroy);
			oldP.destroy();
		}
	}

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

	/**
	 * Speak words using the default text to speech engine
	 *
	 * @param words    words to speak
	 * @param callback consumer to execute when speech is complete
	 */
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

	/**
	 * Speak words using the festival text to speech engine
	 *
	 * @param words    words to speak
	 * @param callback consumer to execute when speech is complete. Process is null when speech execution fails
	 */
	public void speakFestival(String words, Consumer<Process> callback) {
		Process p;
		try {
			p = new ProcessBuilder("festival").start();
		}
		catch (IOException e) {
			callback.accept(null);
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
