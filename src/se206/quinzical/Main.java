package se206.quinzical;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import se206.quinzical.models.QuinzicalModel;
import se206.quinzical.views.QuinzicalView;
import se206.quinzical.views.View;
import se206.quinzical.views.QuizView;

/**
 * Main stage setup
 */
public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // create main model and view for Quinzical
        QuinzicalModel model = new QuinzicalModel();
        View view = new QuinzicalView(model);

        // add stylesheets to scene
        Scene scene = new Scene(view.getView());
        String theme = getClass().getResource("styles/theme.css").toExternalForm();
        scene.getStylesheets().addAll(theme);

        // remove default stage decoration, set title & add taskbar icon
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Quinzical");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("assets/quinzical.png")));
        primaryStage.setScene(scene);
        primaryStage.show();

        // handle dragging and resizing of window
        DragAndResizeHelper.addResizeListener(primaryStage);
    }
}
