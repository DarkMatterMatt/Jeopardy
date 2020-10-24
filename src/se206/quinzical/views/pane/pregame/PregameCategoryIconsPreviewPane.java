package se206.quinzical.views.pane.pregame;

import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.AlertFactory;
import se206.quinzical.views.atom.Icon;
import se206.quinzical.views.base.ViewBase;

/**
 * This class is pane type
 *
 * This pane displays the icons associated with selected categories from
 * PregameCategoriesListPane
 *
 * @author hajinkim
 *
 */
public class PregameCategoryIconsPreviewPane extends ViewBase {
	private final VBox _container = new VBox();
	private final VBox _iconsBox = new VBox();
	private final Label _instruction = new Label();
	private final HBox _submitButton = new HBox();
	private final HBox _topRow = new HBox();
	private final HBox _botRow = new HBox();
	private final QuinzicalModel _model;

	public PregameCategoryIconsPreviewPane(QuinzicalModel model) {
		_model = model;

		_topRow.setAlignment(Pos.CENTER);
		_botRow.setAlignment(Pos.CENTER);
		_topRow.setSpacing(18);
		_botRow.setSpacing(18);
		_iconsBox.setSpacing(18);
		_iconsBox.getChildren().addAll(_topRow, _botRow);

		_instruction.setText("Choose five categories of your taste :3");
		_instruction.getStyleClass().addAll("text-white", "text-bold");


		Icon icon = new Icon("/se206/quinzical/assets/submitButton.png");
		icon.setSize(75, 75);
		_submitButton.getChildren().add(icon.getView());
		icon.getView().setOnMouseClicked(e -> {
			if (model.getPresetModel().pregameCategorySelectionLimitReached()) {
				model.getPresetModel().setHasBeenInitialised();
				onPregameCategoryChange();
			} else {
				AlertFactory.getCustomWarning(_model, "Friendly reminder", "Select 5 categories to proceed!");
			}
		});

		_container.getChildren().addAll(_instruction, _iconsBox, icon.getView());
		_container.setSpacing(30);
		_container.setAlignment(Pos.CENTER_RIGHT);


		onPregameCategoryChange();
		_model.getPresetModel().getPregameUpdateProperty().addListener((o, n, v) -> onPregameCategoryChange());

		// submit button

	}

	@Override
	public VBox getView() {
		// TODO Auto-generated method stub
		return _container;
	}


	private void onPregameCategoryChange() {
		// clear out all the icons
		_topRow.getChildren().clear();
		_botRow.getChildren().clear();
		for (int i = 0; i < 5; i++) {
			Icon icon = new Icon();
			icon.setSize(120, 120);
			icon.getView().setStyle("-fx-background-color: -c-grey-03;" + "-fx-background-radius: 50%;");

			List<Category> categories = _model.getPresetModel().getPregameSelectedCategories();
			if (i < 3) {
				if (categories.size() - 1 < i) {
					// add blank icons
					_model.getPresetModel().skinCategoryImage(icon, "toBeFilledIcon.png");
					_topRow.getChildren().add(icon.getView());
				} else {
					_model.getPresetModel().skinCategoryImage(icon, categories.get(i).getName());
					_topRow.getChildren().add(icon.getView());
				}
			} else {
				if (categories.size() - 1 < i) {
					_model.getPresetModel().skinCategoryImage(icon, "toBeFilledIcon.png");
					_botRow.getChildren().add(icon.getView());
				} else {
					_model.getPresetModel().skinCategoryImage(icon, categories.get(i).getName());
					_botRow.getChildren().add(icon.getView());
				}

			}

		}
	}

}
