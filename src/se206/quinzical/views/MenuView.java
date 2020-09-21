package se206.quinzical.views;

import javafx.scene.layout.BorderPane;

public class MenuView extends View {
	private final BorderPane _container = new BorderPane();

	@Override
	public BorderPane getView() {
		return _container;
	}
}
