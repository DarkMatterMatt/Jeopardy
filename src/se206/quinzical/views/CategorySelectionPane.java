package se206.quinzical.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CategorySelectionPane extends ViewBase {
	private final VBox _container = new VBox();
	private final Label _instruction = new Label();
	private final HBox _topRow = new HBox();
	private final HBox _botRow = new HBox();
	
	public CategorySelectionPane() {
		// vbox
			// one text
			// h box with 3 icons
			// h box with 2 icons
			
		//

		List<String> categories = new ArrayList<>();
		categories.addAll(Arrays.asList("Famous People", "Geography", "History", "Places", "Symbols"));
		
		for (int i = 0; i < categories.size(); i++) {

			Icon icon = new Icon();
			try {
				Image img = new Image(new FileInputStream("./categories/" + categories.get(i) + ".png"));
				icon.setImage(img);
			}
			catch (NullPointerException | FileNotFoundException e) {
				icon.setImage("/se206/quinzical/assets/icon-missing.png");
			}
			icon.setSize(100, 100);
			icon.getView().setStyle("-fx-background-color: -c-grey-03;" + "-fx-background-radius: 50%;");
			icon.getView().setPadding(new Insets(20));
			
			if (i < 3) {
				_topRow.getChildren().add(icon.getView());
			} else {
				_botRow.getChildren().add(icon.getView());
			}

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

}
