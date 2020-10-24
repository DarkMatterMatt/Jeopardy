package se206.quinzical.views.pane;

import javafx.scene.control.Label;
import javafx.scene.layout.*;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.Theme;
import se206.quinzical.views.base.ViewBase;

/**
 * Contains theme selection
 */
public class ThemeSelectPane extends ViewBase {
	private final GridPane _grid = new GridPane();
	private final VBox _container = new VBox(_grid);

	public ThemeSelectPane(QuinzicalModel model) {
		ThemeSamplePane pink = new ThemeSamplePane(model, Theme.PINK);
		_grid.add(pink.getView(), 0, 0);
		_grid.add(new ThemeSamplePane(model, Theme.BUMBLEBEE).getView(), 0, 1);
		_grid.add(new ThemeSamplePane(model, Theme.KIWI).getView(), 1, 0);
		_grid.add(new ThemeSamplePane(model, Theme.OCEAN).getView(), 1, 1);


		// make 2x2 grid
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setPercentWidth(50);
		_grid.getColumnConstraints().addAll(columnConstraints, columnConstraints);

		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setPercentHeight(50);
		_grid.getRowConstraints().addAll(rowConstraints, rowConstraints);

		_grid.getStyleClass().add("grid");
		_container.getStyleClass().add("theme-select");
		addStylesheet("theme-select.css");
	}

	public VBox getView() {
		return _container;
	}

	/**
	 * A theme preview pane, to show what the theme will look like
	 */
	private static class ThemeSamplePane extends ViewBase {
		private final VBox _container = new VBox();

		private ThemeSamplePane(QuinzicalModel model, Theme theme) {
			// theme name
			Label title = new Label(theme.getTitle());
			title.getStyleClass().addAll("text-medium-large", "text-black", "text-bold", "title");

			HBox titleContainer = new HBox(title);
			titleContainer.getStyleClass().addAll("title-container");

			// short blurb
			Label description = new Label(theme.getDescription());
			description.getStyleClass().addAll("text-white", "description");

			// "select" button
			Label select = new Label("Select");
			select.getStyleClass().addAll("text-black", "text-bold", "btn", "select");
			select.setOnMouseClicked(ev -> model.setTheme(theme));

			HBox selectContainer = new HBox(select);
			selectContainer.getStyleClass().addAll("select-container");

			// add elements & styles to container
			_container.getChildren().addAll(titleContainer, description, selectContainer);
			_container.getStyleClass().addAll("theme-sample", theme.getThemeClass());
		}

		public VBox getView() {
			return _container;
		}
	}
}
