package se206.quinzical.views;
import java.util.Arrays;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.View;


public class CategoriesListView extends View {
	private final Pane _container;
	private final ListView<Category> _listView;
	private final HBox _textBox;
	
	public CategoriesListView(QuinzicalModel model) {
		// set up the top header box
		Label topText = new Label("Categories");
		_textBox = new HBox(topText);

		// set up data
		ObservableList<Category> data = FXCollections.observableArrayList();
		data.addAll(model.getCategories());
		
		// set up list view
		_listView = new ListView<Category>(data);
		_container = new VBox(_textBox,_listView);
		_listView.setStyle("-fx-padding: 0px;");
		_listView.setMinWidth(300);
		
		// avoid non-selection thin blue box by preemptively selecting one cell
		_listView.getSelectionModel().select(0);
		_listView.getFocusModel().focus(0);
		
		_listView.setCellFactory((ListView<Category> param) -> {
			ListCell<Category> cell = new ListCell<Category>() {
				@Override
				public void updateItem(Category item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || item == null) {
						setText(null);
						setGraphic(null);
					}else {
						HBox displayedItem = new CategoriesListItemView(item).getView();
						if(item.isSelected()) {
							displayedItem.getStyleClass().addAll("text-bold","category-selected");
						}else {
							displayedItem.getStyleClass().addAll("text-bold","category-not-selected");
						}
						setGraphic(displayedItem);
					}

//                    setPrefWidth(param.getWidth());
//					setWrapText(true);
				}
			};
			cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
		        if (event.getButton()== MouseButton.PRIMARY && (! cell.isEmpty())) {
		        	Category item = cell.getItem();
		            for (Category c: model.getCategories()) {
		            	c.setUnselected();
		            }
		            _listView.refresh();
		            item.setSelected();
		            System.out.println("Left clicked "+item.getName());
		            
		        }
		    });
			return cell;
			
		});
		
		// style
		topText.getStyleClass().addAll("text-large", "text-bold", "text-gold");
		_textBox.getStyleClass().addAll("text-container");
		
		addStylesheet("category-listview.css");
	}

	public Parent getView() {
		return _container;
	}

	
	public class CategoriesListItemView{
		HBox _container = new HBox();
		Category _category;
		
		public CategoriesListItemView(Category item) {
			_category = item;
			Label label = new Label(item.getName());
			label.setWrapText(true);
			_container.getChildren().add(label);
			
			//styling
			if(item.isSelected()) {
				label.setStyle("-fx-text-fill: black;");
			}else {
				label.setStyle("-fx-text-fill: white;");
			}
		}
		
		public Category getCategory() {
			return _category;
		}
		
		public HBox getView() {
			return _container;
		}
		
	}
}
