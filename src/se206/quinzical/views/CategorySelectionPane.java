package se206.quinzical.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;

public class CategorySelectionPane extends ViewBase implements ListChangeListener<Category> {
	private final VBox _container = new VBox();
	private final Label _instruction = new Label();
	private final HBox _topRow = new HBox();
	private final HBox _botRow = new HBox();
	private final QuinzicalModel _model;
	
	public CategorySelectionPane(QuinzicalModel model) {
		_model = model;
//		_model.getPresetModel().getPregameCategorySelection().addListener(this);
//		_model.getPresetModel().getPregameCategorySelection();

		List<String> categories = new ArrayList<>();
		categories.addAll(Arrays.asList("Famous People", "Geography", "History"));
		
		// when the user did not select all the categories, fill the rest with empty
		// circles
		if (categories.size() < 5) {

		}

		_topRow.setAlignment(Pos.CENTER);
		_botRow.setAlignment(Pos.CENTER);
		_container.setAlignment(Pos.CENTER);
		_container.getChildren().addAll(_topRow, _botRow);

		// when the boxes in the list are pressed, they should notify the model
		// when model is notified, it tells category selection pane about it
		// when category selection pane is notified, it updates the icons

		// submit button

	}

	@Override
	public Parent getView() {
		// TODO Auto-generated method stub
		return _container;
	}

	@Override
	public void onChanged(Change<? extends Category> c) {
		for (int i = 0; i < 5; i++) {

			Icon icon = new Icon();
//			try {
//				Image img = new Image(new FileInputStream("./categories/" + categories.get(i) + ".png"));
//				icon.setImage(img);
//			}
//			catch (NullPointerException | FileNotFoundException e) {
//				icon.setImage("/se206/quinzical/assets/icon-missing.png");
//			}
			icon.setSize(100, 100);
			icon.getView().setStyle("-fx-background-color: -c-grey-03;" + "-fx-background-radius: 50%;");
			icon.getView().setPadding(new Insets(20));
			
//			List<Category> categories = _model.getPresetModel().getPregameCategorySelection();
			List<String> categories = new ArrayList<>();
			categories.addAll(Arrays.asList("Famous People", "Geography", "History"));
			if (i < 3) {
				if (categories.size() - 1 < i) {
					// add blank icons
					_model.getPresetModel().skinCategoryImage(icon, "toBeFilledIcon.png");
					_topRow.getChildren().add(icon.getView());
				} else {
					_model.getPresetModel().skinCategoryImage(icon, categories.get(i));
					_topRow.getChildren().add(icon.getView());
				}
			} else {
				if (categories.size() - 1 < i) {
					_model.getPresetModel().skinCategoryImage(icon, "toBeFilledIcon.png");
					_botRow.getChildren().add(icon.getView());
				} else {
					_model.getPresetModel().skinCategoryImage(icon, categories.get(i));
					_botRow.getChildren().add(icon.getView());
				}

			}

		}
	}

}
