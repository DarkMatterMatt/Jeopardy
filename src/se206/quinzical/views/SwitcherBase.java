package se206.quinzical.views;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public abstract class SwitcherBase extends ViewBase {
	private final StackPane _container = new StackPane();

	@Override
	public final StackPane getView() {
		return _container;
	}

	protected void switchToView(Parent show) {
		for (Node n : _container.getChildren()) {
			n.setVisible(n == show);
		}
	}
}
