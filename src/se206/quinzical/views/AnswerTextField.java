package se206.quinzical.views;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import se206.quinzical.models.QuizModel;

/**
 * This class is atom type.
 * It is an input field that allows users to type their answer.
 */
public class AnswerTextField extends ViewBase {
    private static final int INCORRECT_FLASH_DURATION_MS = 70;
    private static final int NUM_INCORRECT_FLASHES = 3;

    private final HBox _container = new HBox();
    private final TextField _answerInput = new TextField();
    private final QuizModel _model;

    public AnswerTextField(QuizModel model) {
        _model = model;

        // user input
        _answerInput.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        _answerInput.setOnAction(e -> submit());
        _answerInput.getStyleClass().addAll("text-white");
        HBox.setHgrow(_answerInput, Priority.ALWAYS);

        // submit button
        ImageView submitImageView = new ImageView(new Image(getClass().getResourceAsStream("/se206/quinzical/assets/submit.png")));
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

    public void flashAnswerIncorrect(javafx.event.EventHandler<javafx.event.ActionEvent> onFinished) {
        _answerInput.getStyleClass().add("text-red");
        _answerInput.getStyleClass().remove("text-white");

        // flashing animation
        Timeline collisionAnimation = new Timeline(new KeyFrame(
                Duration.millis(INCORRECT_FLASH_DURATION_MS),
                ev -> _answerInput.setVisible(!_answerInput.isVisible())
        ));
        collisionAnimation.setCycleCount(NUM_INCORRECT_FLASHES * 2);

        // remove red colouring when flashing is finished, call callback
        collisionAnimation.setOnFinished(ev -> {
            _answerInput.getStyleClass().add("text-white");
            _answerInput.getStyleClass().remove("text-red");
            onFinished.handle(ev);
        });
        collisionAnimation.play();
    }

    private void submit() {
        _model.answerQuestion(_answerInput.getText());
    }

    public void focus() {
        _answerInput.requestFocus();
    }

    public HBox getView() {
        return _container;
    }
}
