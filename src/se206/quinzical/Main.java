package se206.quinzical;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.models.util.KeyEventManager;
import se206.quinzical.views.QuinzicalSwitch;
import se206.quinzical.views.ViewBase;

/**
 * Main stage setup
 * This is the starting point of our application
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // create main model and view for Quinzical
        QuinzicalModel model = QuinzicalModel.load();
        ViewBase view = new QuinzicalSwitch(model);

        // add stylesheets to scene
        Scene scene = new Scene(view.getView());
        String theme = getClass().getResource("/se206/quinzical/styles/theme.css").toExternalForm();
        scene.getStylesheets().addAll(theme);

        // remove default stage decoration, set title & add taskbar icon
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Quinzical");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/se206/quinzical/assets/quinzical.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        // handle dragging and resizing of window
        DragAndResizeHelper.addResizeListener(primaryStage);

        // KeyEventManager handles key presses, allows anyone to listen to events
        KeyEventManager keyEventManager = KeyEventManager.getInstance();
        scene.setOnKeyPressed(keyEventManager::onKeyPress);
        scene.setOnKeyReleased(keyEventManager::onKeyRelease);
    }
}
