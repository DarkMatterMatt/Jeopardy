package se206.quinzical.models.util;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.function.Consumer;

public class KeyboardShortcuts {
	public static final KeyCode[] HOME = {KeyCode.CONTROL, KeyCode.H};
	public static final KeyCode[] LIST_MOVE_DOWN_ONE_ITEM = {KeyCode.DOWN};
	public static final KeyCode[] LIST_MOVE_UP_ONE_ITEM = {KeyCode.UP};
	private static final KeyEventManager _kem = KeyEventManager.getInstance();

	private KeyboardShortcuts() {
	}

	/**
	 * Add keyboard shortcut which calls consumer when keys are pressed
	 *
	 * @param consumer callback function
	 * @param keys     keys that trigger consumer, last key in array must be pressed last
	 */
	public static void addKeyboardShortcut(Consumer<KeyEvent> consumer, KeyCode... keys) {
		if (keys.length == 0) {
			throw new IllegalArgumentException("Keyboard shortcut must use at least one key");
		}
		KeyCode finalKey = keys[keys.length - 1];
		KeyCode[] otherKeys = Arrays.copyOfRange(keys, 0, keys.length - 1);

		_kem.addPressListener(ev -> {
			if (Arrays.stream(otherKeys).allMatch(_kem::isKeyDown)) {
				consumer.accept(ev);
			}
		}, finalKey);
	}
}
