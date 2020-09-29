package se206.quinzical.models.util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Tracks key presses, releases and keydown durations
 *
 * @author Matt Moran
 */
public class KeyEventManager {
	// 'null' HashMap key is used to represent a listener for any keyboard key
	public static final KeyCode ANY_KEY = null;
	private static final KeyEventManager _instance = new KeyEventManager();
	private final HashMap<KeyCode, Long> _keyDownSince = new HashMap<>();
	// 'null' HashMap key is used to represent a listener for any keyboard key
	private final HashMap<KeyCode, ArrayList<Consumer<KeyEvent>>> _pressConsumers = new HashMap<>();
	private final HashMap<KeyCode, ArrayList<Consumer<KeyEvent>>> _releaseConsumers = new HashMap<>();

	private KeyEventManager() {
	}

	public static KeyEventManager getInstance() {
		return _instance;
	}

	private void addListener(HashMap<KeyCode, ArrayList<Consumer<KeyEvent>>> map, Consumer<KeyEvent> consumer, KeyCode key) {
		if (!map.containsKey(key)) map.put(key, new ArrayList<>());
		ArrayList<Consumer<KeyEvent>> consumers = map.get(key);
		if (!consumers.contains(consumer)) consumers.add(consumer);
	}

	/**
	 * Add key press listener for one or more keys
	 *
	 * @param consumer the listener function to call
	 * @param keys     one or more keys to listen for
	 */
	public void addPressListener(Consumer<KeyEvent> consumer, KeyCode... keys) {
		Arrays.stream(keys).forEach(k -> addListener(_pressConsumers, consumer, k));
	}

	/**
	 * Add key release listener for one or more keys
	 *
	 * @param consumer the listener function to call
	 * @param keys     one or more keys to listen for
	 */
	public void addReleaseListener(Consumer<KeyEvent> consumer, KeyCode... keys) {
		Arrays.stream(keys).forEach(k -> addListener(_releaseConsumers, consumer, k));
	}

	public boolean isKeyDown(KeyCode key) {
		return _keyDownSince.getOrDefault(key, 0L) != 0;
	}

	public long keyDownDurationMs(KeyCode key) {
		long since = keyDownSince(key);
		return since == 0 ? 0 : System.currentTimeMillis() - since;
	}

	public long keyDownSince(KeyCode key) {
		return _keyDownSince.getOrDefault(key, 0L);
	}

	private void notifyConsumers(HashMap<KeyCode, ArrayList<Consumer<KeyEvent>>> consumersMap, KeyEvent ev) {
		ArrayList<Consumer<KeyEvent>> consumers = consumersMap.get(ev.getCode());
		if (consumers != null) consumers.forEach(c -> c.accept(ev));

		consumers = consumersMap.get(null);
		if (consumers != null) consumers.forEach(c -> c.accept(ev));
	}

	public void onKeyPress(KeyEvent ev) {
		_keyDownSince.put(ev.getCode(), System.currentTimeMillis());
		notifyConsumers(_pressConsumers, ev);
	}

	public void onKeyRelease(KeyEvent ev) {
		_keyDownSince.put(ev.getCode(), 0L);
		notifyConsumers(_releaseConsumers, ev);
	}

	private void removeListener(HashMap<KeyCode, ArrayList<Consumer<KeyEvent>>> map, Consumer<KeyEvent> consumer, KeyCode key) {
		ArrayList<Consumer<KeyEvent>> consumers = map.get(key);
		if (consumers == null) return;
		consumers.remove(consumer);
	}

	/**
	 * Remove key press listener for one or more keys
	 *
	 * @param consumer the listener function to call
	 * @param keys     one or more keys to listen for
	 */
	public void removePressListener(Consumer<KeyEvent> consumer, KeyCode... keys) {
		Arrays.stream(keys).forEach(k -> removeListener(_pressConsumers, consumer, k));
	}

	/**
	 * Remove key release listener for one or more keys
	 *
	 * @param consumer the listener function to call
	 * @param keys     one or more keys to listen for
	 */
	public void removeReleaseListener(Consumer<KeyEvent> consumer, KeyCode... keys) {
		Arrays.stream(keys).forEach(k -> removeListener(_releaseConsumers, consumer, k));
	}
}
