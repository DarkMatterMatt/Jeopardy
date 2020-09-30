package se206.quinzical.views;

import javafx.scene.Parent;

public abstract class ViewBase {
    public abstract Parent getView();

    protected void addStylesheet(String filename) {
        String stylesheet = getClass().getResource("../styles/" + filename).toExternalForm();
        getView().getStylesheets().add(stylesheet);
    }
}
