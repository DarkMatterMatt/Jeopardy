package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.Question;
import se206.quinzical.models.QuizModel;

public class CategoryPreview extends View {
	private final Pane _container = new Pane();
	private final IconView _confirm = new IconView();
	private final Label _category = new Label();
	private final Label _value = new Label();
	private final HBox _content = new HBox();
	private final IconView _icon = new IconView();
	private final PresetQuinzicalModel _model;

	public CategoryPreview(PresetQuinzicalModel model) {
		_model = model;

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
		_confirm.getView().setOnMouseClicked(e -> confirm());

		_container.getChildren().addAll(_content, _confirm.getView());
		_container.getStyleClass().add("category-preview");
		addStylesheet("category-preview.css");

		// add resize listeners
		_container.widthProperty().addListener((obs, old, val) -> onWidthChange());
		_container.heightProperty().addListener((obs, old, val) -> onHeightChange());

		// listen for question changes
		updateContent(_model.getCurrentQuestion());
		_model.getCurrentQuestionProperty().addListener((obs, oldVal, val) -> updateContent(val));
	}

	private void updateContent(Question q) {
		if (q == null) {
			System.out.println("CategoryPreview#updateContent: Question is null");
			return;
		}

		int nextValue = q.getValue();
		String categoryName = q.getCategory().getName();

		// update values
		_value.setText("$" + nextValue);
		_category.setText(categoryName);
		_model.skinCategoryImage(_icon, categoryName);

		// content changes = size changes
		onHeightChange();
		onWidthChange();
	}

	private void onWidthChange() {
		double containerWidth = _container.getWidth();
		double confirmWidth = _confirm.getView().getBoundsInLocal().getWidth();

		// content has a 48px margin on the left
		_content.setLayoutX(48);

		// set _confirm to bottom-right (32px margin)
		_confirm.getView().setLayoutX(containerWidth - confirmWidth - 32);
	}

	private void onHeightChange() {
		double containerHeight = _container.getHeight();
		double contentHeight = _content.getBoundsInLocal().getHeight();
		double confirmHeight = _confirm.getView().getBoundsInLocal().getHeight();

		// set _content to center
		_content.setLayoutY((containerHeight - contentHeight) / 2);

		// set _confirm to bottom-right (32px margin)
		_confirm.getView().setLayoutY(containerHeight - confirmHeight - 32);
	}

	private void confirm() {
		_model.confirmCategory();
	}

	@Override
	public Pane getView() {
		return _container;
	}
}
