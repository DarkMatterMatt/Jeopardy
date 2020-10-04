package se206.quinzical.views;

import javafx.scene.Parent;

/**
 * This class is Base type.
 * Provides the base for Pane type AND Atom type.
 * It provides a method to add stylesheet, and ensures
 * that every class that extends this base will return
 * javaFX parent type (usually javaFX's Pane/Box type),
 * upon getView() call.
 */
public abstract class ViewBase {
	/**
	 * Add style sheet with the given file name, assuming they exist in se206.quinzical.styles package
	 */
	protected void addStylesheet(String filename) {
		String stylesheet = getClass().getResource("/se206/quinzical/styles/" + filename).toExternalForm();
		getView().getStylesheets().add(stylesheet);
	}

	public abstract Parent getView();
}
