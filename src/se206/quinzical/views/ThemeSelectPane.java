package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.QuizModel;
import se206.quinzical.models.Theme;

import javax.swing.text.View;

/**
 * Contains theme selection
 */
public class ThemeSelectPane extends ViewBase {
	private final VBox _container = new VBox();
	private final QuinzicalModel _model;
	private final Label _subtitleLabel = new Label();
	private final Label _titleLabel = new Label();

	public ThemeSelectPane(QuinzicalModel model) {
		_model = model;
		_container.getStyleClass().add("theme-select");
		addStylesheet("theme-select.css");
	}

	public VBox getView() {
		return _container;
	}

	private class ThemeSamplePane extends ViewBase {
		private final VBox _container = new VBox();

		private ThemeSamplePane(QuinzicalModel model, Theme theme) {
			Label title = new Label(theme.getTitle());
			title.getStyleClass().addAll("text-black", "text-bold", "title");

			Label description = new Label(theme.getDescription());
			description.getStyleClass().addAll("text-white", "title");

			Label submit = new Label("Select");
			submit.getStyleClass().addAll("text-black", "text-bold", "submit");

			HBox submitContainer = new HBox(submit);
			submit.getStyleClass().addAll("submit-container");

			// add elements & styles to container
			_container.getChildren().addAll(title, description, submitContainer);
			_container.getStyleClass().add("theme-sample");
		}

		public VBox getView() {
			return _container;
		}
	}
}
