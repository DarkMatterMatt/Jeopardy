package se206.quinzical.views.base;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

/**
 * This class is Base type.
 * Provides the base for 'Switch' type.
 * Every class that extends this class acts as a GUI component
 * that switches to different panes for display, while hiding
 * panes that does ot get displayed.
 */
public abstract class SwitcherBase extends ViewBase {
	private final StackPane _container = new StackPane();

	@Override
	public final StackPane getView() {
		return _container;
	}

	/**
	 * Switch to the provided view
	 */
	public void switchToView(Parent show) {
		for (Node n : _container.getChildren()) {
			n.setVisible(n == show);
		}
	}
}
