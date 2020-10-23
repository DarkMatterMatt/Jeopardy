package se206.quinzical.views;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import se206.quinzical.models.PresetQuinzicalModel;
import se206.quinzical.models.Question;

/**
 * This class is Pane type.
 * Shows category preview that shows:
 * - name of the category
 * - active question's value
 * Used by GameSwitch
 */
public class CategoryPreviewPane extends ViewBase {
	private final Label _category = new Label();
	private final Icon _confirm = new Icon();
	private final Pane _container = new Pane();
	private final HBox _content = new HBox();
	private final Icon _icon = new Icon();
	private final PresetQuinzicalModel _model;
	private final Text _playFor = new Text();
	private final VBox _text = new VBox();
	private final Text _value = new Text();

	public CategoryPreviewPane(PresetQuinzicalModel model) {
		_model = model;

		// category icon
		_icon.setSize(84, 84)
				.addClass("category-icon");

		// category name & description
		TextFlow description = new TextFlow(_playFor, _value);
		_text.getChildren().addAll(_category, description);

		// add text styles
		description.getStyleClass().addAll("category-description");
		_text.getStyleClass().addAll("text-container");
		_category.getStyleClass().addAll("text-gold", "text-bold", "text-large");
		_playFor.getStyleClass().addAll("text-white");
		_value.getStyleClass().addAll("text-white", "text-bold");

		// category icon and text are in a HBox
		_content.getChildren().addAll(_icon.getView(), _text);

		// confirm icon
		_confirm.setImage("/se206/quinzical/assets/submit.png")
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

	private void confirm() {
		_model.confirmCategory();
	}

	@Override
	public Pane getView() {
		return _container;
	}

	private void onHeightChange() {
		double containerHeight = _container.getHeight();
		double contentHeight = _content.getBoundsInLocal().getHeight();
		double confirmHeight = _confirm.getView().getBoundsInLocal().getHeight();

		if (contentHeight > containerHeight) {
			_content.setLayoutY((230 - 84) / 2);
			_confirm.getView().setLayoutY(230 - 48 - 32);
			// hacky fix: before anything is initialized, the content height is something
			// ridiculous because the width is tiny and the text wraps to be very very tall
			return;
		}

		// set _content to center
		_content.setLayoutY((containerHeight - contentHeight) / 2);

		// set _confirm to bottom-right (32px margin)
		_confirm.getView().setLayoutY(containerHeight - confirmHeight - 32);
	}

	private void onWidthChange() {
		double containerWidth = _container.getWidth();
		double confirmWidth = _confirm.getView().getBoundsInLocal().getWidth();
		double iconWidth = _icon.getView().isManaged() ? _icon.getView().getBoundsInLocal().getWidth() : 0;

		// content has a 48px margin on the left
		_content.setLayoutX(48);

		// set _confirm to bottom-right (32px margin)
		_confirm.getView().setLayoutX(containerWidth - confirmWidth - 32);

		// make text box wrap
		_text.setMaxWidth(containerWidth - iconWidth - 48 * 2);
	}

	private void updateContent(Question q) {
		// show / hide category stuff
		boolean shouldShow = q != null;
		_category.setVisible(shouldShow);
		_category.setManaged(shouldShow);
		_confirm.getView().setVisible(shouldShow);
		_confirm.getView().setManaged(shouldShow);
		_icon.getView().setVisible(shouldShow);
		_icon.getView().setManaged(shouldShow);
		_value.setVisible(shouldShow);
		_value.setManaged(shouldShow);

		if (q == null) {
			// reuse _playFor text box
			if (_model.getNumAttempted() == 0) {
				// first start
				_playFor.setText("Select a question to get started!");
			}
			else {
				// category has no more questions
				_playFor.setText("You've finished all the questions in this category!");
			}
		}
		else {
			int nextValue = q.getValue();
			String categoryName = (q.getCategory() != null) ? q.getCategory().getName() : "";

			// update values
			_playFor.setText("Play for ");
			_value.setText("$" + nextValue);
			_category.setText(categoryName);
			_model.skinCategoryImage(_icon, categoryName);
		}

		// content changes = size changes
		onHeightChange();
		onWidthChange();
	}
}
