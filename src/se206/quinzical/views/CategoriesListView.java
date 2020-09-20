package se206.quinzical.views;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.View;

public class CategoriesListView {
	QuinzicalModel _model;
	Parent _container;
	
	
	public CategoriesListView(QuinzicalModel model) {
		_model = model;
		ObservableList<Category> data = FXCollections.observableArrayList();
		data.addAll(_model.getCategories());
		ListView<Category> listView = new ListView<Category>(data);
		_container = listView;
		
		listView.setCellFactory((ListView<Category> param) -> new ListCell<Category>() {
				@Override
				public void updateItem(Category item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || item == null) {
						setText(null);
						setGraphic(null);
					}else {
						HBox content = new HBox(new Text(item.getName()));
						
						content.setSpacing(10);
						
						setGraphic(content);
					}
				}
				private ImageView getImageView() {
					ImageView output = new ImageView();
					
					output.setImage(new Image(getClass().getResourceAsStream("asset/icon1.png")));
					output.setFitWidth(100);
					output.setPreserveRatio(true);
					
					return output;
				}
		});
	}

	public Parent getView() {
		return _container;
	}


	

}
