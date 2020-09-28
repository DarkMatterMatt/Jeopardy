package se206.quinzical.views;

import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.QuizModel;

/**
 * View for input field for answer screen
 */
public class AnswerInputView extends View {
    private final HBox _container = new HBox();
    private final TextField _answerInput = new TextField();
    private final QuizModel _model;

    public AnswerInputView(QuizModel model) {
        _model = model;

        // user input
        _answerInput.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        _answerInput.setOnAction(e -> submit());
        HBox.setHgrow(_answerInput, Priority.ALWAYS);

        // submit button
        ImageView submitImageView = new ImageView(new Image(getClass().getResourceAsStream("../assets/submit.png")));
        submitImageView.setFitWidth(32);
        submitImageView.setPreserveRatio(true);
        submitImageView.setSmooth(true);
        submitImageView.setCache(true);
        submitImageView.getStyleClass().add("submit");

        // square background for submit button
        VBox submitContainer = new VBox(submitImageView);
        submitContainer.setOnMouseClicked(e -> submit());
        submitContainer.getStyleClass().addAll("btn", "submit-container");
        submitContainer.prefWidthProperty().bind(_answerInput.heightProperty());

        addStylesheet("answer-input.css");
        _container.getStyleClass().add("input-container");
        _container.getChildren().addAll(_answerInput, submitContainer);
    }

    public void clear() {
        _answerInput.clear();
    }

    public TextField getInputField() {
    	return _answerInput;
    }
    private void submit() {
        _model.answerQuestion(_answerInput.getText());
        clear();
    }

    public HBox getView() {
        return _container;
    }
}
