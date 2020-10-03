package se206.quinzical.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206.quinzical.DragAndResizeHelper;
import se206.quinzical.models.QuinzicalModel;

public class AlertFactory {

	public static void getExitAlert(Taskbar origin) {
		Alert exitAlert = new Alert(AlertType.NONE);
		exitAlert.setHeaderText("Sad to see you go!");
		exitAlert.setContentText("Are you sure you want to exit Quinzical?");

		// style alert
		DialogPane dialogue = exitAlert.getDialogPane();
		ButtonType yes = new ButtonType("Yup",ButtonBar.ButtonData.YES);
		ButtonType no = new ButtonType("Actually nah",ButtonBar.ButtonData.NO);
		dialogue.getButtonTypes().addAll(yes, no);
		dialogue.getStylesheets().addAll(
				origin.getClass().getResource("/se206/quinzical/styles/theme.css").toExternalForm(),
				origin.getClass().getResource("/se206/quinzical/styles/dialogue.css").toExternalForm()
		);
		// stage of dialogue
		Stage diaStage = (Stage) dialogue.getScene().getWindow();
		diaStage.initStyle(StageStyle.UNDECORATED);
		DragAndResizeHelper.addResizeListener(diaStage);
		exitAlert.showAndWait().filter(res -> res==yes).ifPresent(res -> {
			Platform.exit();
		});
	}

	public static void getResetAlert(Taskbar origin, QuinzicalModel model) {
		Alert resetAlert = new Alert(AlertType.NONE);
		resetAlert.setHeaderText("Be careful! You are about to lose your money");
		resetAlert.setContentText("Are you sure you want to reset?");

		// style alert
		DialogPane dialogue = resetAlert.getDialogPane();
		ButtonType yes = new ButtonType("Yup",ButtonBar.ButtonData.YES);
		ButtonType no = new ButtonType("Actually nah",ButtonBar.ButtonData.NO);
		dialogue.getButtonTypes().addAll(yes, no);
		dialogue.getStylesheets().addAll(
				origin.getClass().getResource("/se206/quinzical/styles/theme.css").toExternalForm(),
				origin.getClass().getResource("/se206/quinzical/styles/dialogue.css").toExternalForm()
		);
		// stage of dialogue
		Stage diaStage = (Stage) dialogue.getScene().getWindow();
		diaStage.initStyle(StageStyle.UNDECORATED);
		DragAndResizeHelper.addResizeListener(diaStage);
		resetAlert.showAndWait().filter(res -> res==yes).ifPresent(res -> {
			model.reset();
		});
	}

}
