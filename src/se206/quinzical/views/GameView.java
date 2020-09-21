package se206.quinzical.views;

import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import se206.quinzical.models.QuinzicalModel;

public class GameView extends View {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;

	public GameView(QuinzicalModel model) {
		_model = model;
	}

	@Override
	public HBox getView() {
		return _container;
	}
}
