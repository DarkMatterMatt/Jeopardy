package se206.quinzical.views;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import se206.quinzical.models.Category;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.View;


public class CategoriesListView extends View {
	QuinzicalModel _model;
	Parent _container;
	
	
	public CategoriesListView(QuinzicalModel model) {
		_model = model;
		ObservableList<Category> data = FXCollections.observableArrayList();
		data.addAll(_model.getCategories());
		ListView<Category> listView = new PrettyListView<Category>(data);
		Text topText = new Text("Categories");		
		_container = new VBox(topText,listView);
		
		
		topText.getStyleClass().addAll("text-gold");

		
		
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
		
		addStylesheet("category-listview.css");
		
	}

	public Parent getView() {
		return _container;
	}


	//to pretty-fy listview's scrollbar cos default one's ugly
	public class PrettyListView<T> extends ListView<T> {
		 
	    private ScrollBar vBar = new ScrollBar();
	    private ScrollBar hBar = new ScrollBar();
	 
	    public PrettyListView(ObservableList<T> h) {
	    	super(h);
	        skinProperty().addListener(it -> {
	            // first bind, then add new scrollbars, otherwise the new bars will be found
	            bindScrollBars();
	            getChildren().addAll(vBar, hBar);
	        });
	 
	        getStyleClass().add("pretty-list-view");
	 
	        vBar.setManaged(false);
	        vBar.setOrientation(Orientation.VERTICAL);
	        vBar.getStyleClass().add("pretty-scroll-bar");
	        vBar.visibleProperty().bind(vBar.visibleAmountProperty().isNotEqualTo(0));
	 
	        hBar.setManaged(false);
	        hBar.setOrientation(Orientation.HORIZONTAL);
	        hBar.getStyleClass().add("pretty-scroll-bar");
	        hBar.visibleProperty().bind(hBar.visibleAmountProperty().isNotEqualTo(0));
	    }
	 
	    private void bindScrollBars() {
	        final Set<Node> nodes = lookupAll("VirtualScrollBar");
	        for (Node node : nodes) {
	            if (node instanceof ScrollBar) {
	                ScrollBar bar = (ScrollBar) node;
	                if (bar.getOrientation().equals(Orientation.VERTICAL)) {
	                    bindScrollBars(vBar, bar);
	                } else if (bar.getOrientation().equals(Orientation.HORIZONTAL)) {
	                    bindScrollBars(hBar, bar);
	                }
	            }
	        }
	    }
	 
	    private void bindScrollBars(ScrollBar scrollBarA, ScrollBar scrollBarB) {
	        scrollBarA.valueProperty().bindBidirectional(scrollBarB.valueProperty());
	        scrollBarA.minProperty().bindBidirectional(scrollBarB.minProperty());
	        scrollBarA.maxProperty().bindBidirectional(scrollBarB.maxProperty());
	        scrollBarA.visibleAmountProperty().bindBidirectional(scrollBarB.visibleAmountProperty());
	        scrollBarA.unitIncrementProperty().bindBidirectional(scrollBarB.unitIncrementProperty());
	        scrollBarA.blockIncrementProperty().bindBidirectional(scrollBarB.blockIncrementProperty());
	    }
	 
	    @Override
	    protected void layoutChildren() {
	        super.layoutChildren();
	 
	        Insets insets = getInsets();
	        double w = getWidth();
	        double h = getHeight();
	        final double prefWidth = vBar.prefWidth(-1);
	        vBar.resizeRelocate(w - prefWidth - insets.getRight(), insets.getTop(), prefWidth, h - insets.getTop() - insets.getBottom());
	 
	        final double prefHeight = hBar.prefHeight(-1);
	        hBar.resizeRelocate(insets.getLeft(), h - prefHeight - insets.getBottom(), w - insets.getLeft() - insets.getRight(), prefHeight);
	    }
	}
	

}
