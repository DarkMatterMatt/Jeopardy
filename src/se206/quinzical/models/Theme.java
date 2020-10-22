package se206.quinzical.models;

public enum Theme {
	BUMBLEBEE("Bumblebee", "More orange than yellow", "bumblebee"),
	KIWI("Kiwiana", "Traditionally earthy", "kiwi"),
	OCEAN("Oceanic", "A taste of the deep blue sea", "ocean"),
	PINK("Obnoxiously Pink", "Pink, pink, pink everywhere!", "pink"),
	;

	private final String _description;
	private final String _styleClass;
	private final String _title;

	/**
	 * Add a new theme
	 *
	 * @param title       name of theme
	 * @param description short theme blurb
	 * @param styleClass  CSS style class to apply
	 */
	Theme(String title, String description, String styleClass) {
		_description = description;
		_styleClass = styleClass;
		_title = title;
	}

	/**
	 * Get short theme blurb
	 */
	public String getDescription() {
		return _description;
	}

	/**
	 * Get CSS theme filename to apply
	 */
	public String getThemeClass() {
		return "theme-" + _styleClass;
	}

	/**
	 * Get name of theme
	 */
	public String getTitle() {
		return _title;
	}
}
