package se206.quinzical.views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuizModel;

/**
 * This class is Pane type.
 * It lists possible categories the user can select in a one-column list.
 * Each cell's string value is retrieved from the list of categories from the QuinzicalModel.
 *
 * @author hajinkim
 */
public class CategoriesList extends ViewBase {
	private final Pane _container;
	private final ListView<Category> _listView;
	private final QuizModel _model;
	private final HBox _textBox;

	public CategoriesList(QuizModel model) {
		_model = model;

		// set up the top header box
		Label topText = new Label("Categories");
		_textBox = new HBox(topText);

		// set up data
		ObservableList<Category> data = FXCollections.observableArrayList();
		data.addAll(model.getCategories());

		// set up list view
		_listView = new ListView<>(data);
		_container = new VBox(_textBox, _listView);
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
					if (empty || item == null) {
						setText(null);
						setGraphic(null);
					}
					else {
						HBox displayedItem = new CategoriesListItemView(item).getView();
						if (item.isSelected()) {
							displayedItem.getStyleClass().addAll("text-bold", "category", "selected");
						}
						else {
							displayedItem.getStyleClass().addAll("text-bold", "category", "not-selected");
						}
						setGraphic(displayedItem);
					}

				}
			};
			cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
				if (event.getButton() == MouseButton.PRIMARY && (!cell.isEmpty())) {
					Category item = cell.getItem();
					for (Category c : model.getCategories()) {
						c.setUnselected();
					}
					_listView.refresh();
					item.setSelected();

					_model.selectCategory(item);

				}
			});
			return cell;

		});

		// style
		topText.getStyleClass().addAll("text-large", "text-bold", "text-gold");
		_textBox.getStyleClass().addAll("text-container");

		_container.getStyleClass().add("categories-list");
		addStylesheet("category-listview.css");
	}

	public Parent getView() {
		return _container;
	}

	/**
	 * Objects of this type is a graphic (HBox) that prettifies
	 * each category String for each cell.
	 *
	 * @author hajinkim
	 */
	public static class CategoriesListItemView {
		Category _category;
		HBox _container = new HBox();

		public CategoriesListItemView(Category item) {
			_category = item;
			Label label = new Label(item.getName());
			label.setWrapText(true);
			_container.getChildren().add(label);

			// styling
			if (item.isSelected()) {
				label.getStyleClass().add("text-black");
			}
			else {
				label.getStyleClass().add("text-white");
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
