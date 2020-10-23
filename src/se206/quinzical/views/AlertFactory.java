package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206.quinzical.DragAndResizeHelper;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.util.TextToSpeech;

/**
 * AlertFactory puts alert screen on a separate window.
 * Serves as a warning for user action when exiting or resetting
 */
public class AlertFactory {
	private static final ButtonType BUTTON_NO = new ButtonType("Actually nah", ButtonBar.ButtonData.NO);
	private static final ButtonType BUTTON_YES = new ButtonType("Yup", ButtonBar.ButtonData.YES);

	/**
	 * Create alert dialog with specified theme, content & buttons.
	 *
	 * @param themeClass  theme to apply, should be fetched from {@link QuinzicalModel#getTheme()}
	 * @param header      title message to display
	 * @param message     message content / description to display
	 * @param buttonTypes buttons to add
	 */
	private static Alert createAlert(String themeClass, String header, String message, ButtonType... buttonTypes) {
		// create alert, set content
		Alert alert = new Alert(AlertType.NONE);
		alert.setHeaderText(header);
		alert.setContentText(message);

		// add buttons & style alert
		DialogPane dialog = alert.getDialogPane();
		dialog.getButtonTypes().addAll(buttonTypes);
		dialog.getStylesheets().addAll(
				AlertFactory.class.getResource("/se206/quinzical/styles/theme.css").toExternalForm(),
				AlertFactory.class.getResource("/se206/quinzical/styles/dialogue.css").toExternalForm());
		dialog.getStyleClass().add(themeClass);

		// remove stage styles, make it draggable & resizable
		Stage dialogStage = (Stage) dialog.getScene().getWindow();
		dialogStage.initStyle(StageStyle.UNDECORATED);
		DragAndResizeHelper.addResizeListener(dialogStage);

		return alert;
	}

	/**
	 * This method will display a warning message (provided through method
	 * parameter) with just 'Yup' button
	 */
	public static void getCustomWarning(QuinzicalModel model, String header, String message) {
		Alert alert = createAlert(
				model.getTheme().getThemeClass(),
				header,
				message,
				new ButtonType("Yup", ButtonBar.ButtonData.YES)
		);
		alert.showAndWait();
	}

	/**
	 * Activate the exit alert
	 */
	public static void getExitAlert(QuinzicalModel model) {
		Alert alert = createAlert(
				model.getTheme().getThemeClass(),
				"Sad to see you go!",
				"Are you sure you want to exit Quinzical?",
				BUTTON_YES,
				BUTTON_NO
		);
		alert.showAndWait()
				.filter(res -> res == BUTTON_YES)
				.ifPresent(res -> {
					TextToSpeech.getInstance().cancel();
					Platform.exit();
				});
	}

	/**
	 * Activate the reset alert
	 */
	public static void getResetAlert(QuinzicalModel model) {
		Alert alert = createAlert(
				model.getTheme().getThemeClass(),
				"Be careful! You are about to lose your money",
				"Are you sure you want to reset?",
				BUTTON_YES,
				BUTTON_NO
		);
		alert.showAndWait()
				.filter(res -> res == BUTTON_YES)
				.ifPresent(res -> {
					TextToSpeech.getInstance().cancel();
					model.reset();
				});
	}
}
