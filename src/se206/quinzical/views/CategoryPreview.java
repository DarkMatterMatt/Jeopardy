package se206.quinzical.views;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.QuinzicalModel;

public class CategoryPreview extends View {
	private final Pane _container = new Pane();
	private final IconView _confirm = new IconView();
	private final Label _category = new Label();
	private final Label _value = new Label();
	private final HBox _content = new HBox();
	private final IconView _icon = new IconView();

	public CategoryPreview(QuinzicalModel model) {
		// category icon
		_icon.setSize(84, 84)
				.addClass("category-icon");

		// category name & description
		Label playFor = new Label("Play for ");
		TextFlow description = new TextFlow(playFor, _value);
		VBox text = new VBox(_category, description);

		// add text styles
		description.getStyleClass().addAll("category-description");
		text.getStyleClass().addAll("text-container");
		_category.getStyleClass().addAll("text-gold", "text-bold", "text-large");
		playFor.getStyleClass().addAll("text-white");
		_value.getStyleClass().addAll("text-white", "text-bold");

		// category icon and text are in a HBox
		_content.getChildren().addAll(_icon.getView(), text);

		// confirm icon
		_confirm.setImage("../assets/submit.png")
				.setSize(48, 48)
				.addClass("confirm-icon");

		_container.getChildren().addAll(_content, _confirm.getView());
		_container.getStyleClass().add("category-preview");
		addStylesheet("category-preview.css");

		// add resize listeners... this is pretty hacky
		_content.widthProperty().addListener((obs, old, val) -> onWidthChange());
		_content.heightProperty().addListener((obs, old, val) -> onHeightChange());
		_confirm.getView().widthProperty().addListener((obs, old, val) -> onWidthChange());
		_confirm.getView().heightProperty().addListener((obs, old, val) -> onHeightChange());
		_container.widthProperty().addListener((obs, old, val) -> onWidthChange());
		_container.heightProperty().addListener((obs, old, val) -> onHeightChange());

		// TODO: add listener so we know when to update content
		updateContent();
	}

	private void updateContent() {
		// TODO: get these from model
		int nextValue = 400;
		String categoryName = "Geography";
		String categoryIcon = "icon-missing.png";

		// update values
		_value.setText("$" + nextValue);
		_category.setText(categoryName);
		_icon.setImage("../assets/" + categoryIcon);
	}

	private void onWidthChange() {
		double containerWidth = _container.getWidth();
		double contentWidth = _content.getBoundsInLocal().getWidth();
		double confirmWidth = _confirm.getView().getBoundsInLocal().getWidth();
		double iconWidth = _icon.getView().getBoundsInLocal().getWidth();

		// set _content so that _text is centered
		_content.setLayoutX((containerWidth - contentWidth - iconWidth) / 2);

		// set _confirm to bottom-right (16px margin)
		_confirm.getView().setLayoutX(containerWidth - confirmWidth - 16);
	}

	private void onHeightChange() {
		double containerHeight = _container.getHeight();
		double contentHeight = _content.getBoundsInLocal().getHeight();
		double confirmHeight = _confirm.getView().getBoundsInLocal().getHeight();

		// set _content to center
		_content.setLayoutY((containerHeight - contentHeight) / 2);

		// set _confirm to bottom-right (16px margin)
		_confirm.getView().setLayoutY(containerHeight - confirmHeight - 16);
	}

	@Override
	public Pane getView() {
		return _container;
	}
}
