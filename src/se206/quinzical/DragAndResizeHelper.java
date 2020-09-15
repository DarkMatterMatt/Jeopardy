package se206.quinzical;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Allows stage movement and resizing by dragging edges.
 * Original author: Alexander Berg
 * Source: https://stackoverflow.com/a/24017605/6595777
 */

public class DragAndResizeHelper {
    public static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (Node child : children) {
                addListenerDeeply(child, listener);
            }
        }
    }

    public static void addResizeListener(Stage stage) {
        ResizeListener resizeListener = new ResizeListener(stage);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
        stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);
        ObservableList<Node> children = stage.getScene().getRoot().getChildrenUnmodifiable();
        for (Node child : children) {
            addListenerDeeply(child, resizeListener);
        }
    }

    static class ResizeListener implements EventHandler<MouseEvent> {
        private final Stage stage;
        private Cursor cursorEvent = Cursor.DEFAULT;
        private double stageStartX = 0;
        private double stageStartY = 0;
        private double startX = 0;
        private double startY = 0;

        public ResizeListener(Stage stage) {
            this.stage = stage;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {
            EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
            Scene scene = stage.getScene();

            double sceneX = mouseEvent.getSceneX();
            double sceneY = mouseEvent.getSceneY();
            double sceneWidth = scene.getWidth();
            double sceneHeight = scene.getHeight();
            double screenX = mouseEvent.getScreenX();
            double screenY = mouseEvent.getScreenY();

            int topDragThres = 16;
            int cornerThres = 16;
            int edgeThres = 8;

            if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
                if (sceneX < cornerThres && sceneY < cornerThres) {
                    cursorEvent = Cursor.NW_RESIZE;
                }
                else if (sceneX < cornerThres && sceneY > sceneHeight - cornerThres) {
                    cursorEvent = Cursor.SW_RESIZE;
                }
                else if (sceneX > sceneWidth - cornerThres && sceneY < cornerThres) {
                    cursorEvent = Cursor.NE_RESIZE;
                }
                else if (sceneX > sceneWidth - cornerThres && sceneY > sceneHeight - cornerThres) {
                    cursorEvent = Cursor.SE_RESIZE;
                }
                else if (sceneX < edgeThres) {
                    cursorEvent = Cursor.W_RESIZE;
                }
                else if (sceneX > sceneWidth - edgeThres) {
                    cursorEvent = Cursor.E_RESIZE;
                }
                else if (sceneY < topDragThres) {
                    cursorEvent = Cursor.MOVE;
                }
                else if (sceneY > sceneHeight - edgeThres) {
                    cursorEvent = Cursor.S_RESIZE;
                }
                else {
                    cursorEvent = Cursor.DEFAULT;
                }
                scene.setCursor(cursorEvent);
            }
            else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
                scene.setCursor(Cursor.DEFAULT);
            }
            else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
                startX = stage.getWidth() - sceneX;
                startY = stage.getHeight() - sceneY;
                stageStartX = stage.getX() - screenX;
                stageStartY = stage.getY() - screenY;
            }
            else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType) && !Cursor.DEFAULT.equals(cursorEvent)) {
                if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
                    // N, NW, NE, S, SW, SE
                    double minHeight = stage.getMinHeight() > (edgeThres * 2) ? stage.getMinHeight() : (edgeThres * 2);
                    if (Cursor.MOVE.equals(cursorEvent)) {
                        // N, moving scene (not resizing)
                        stage.setX(screenX + stageStartX);
                        stage.setY(screenY + stageStartY);
                    }
                    else if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) {
                        // NW, NE
                        if (stage.getHeight() > minHeight || sceneY < 0) {
                            stage.setHeight(stage.getY() - screenY + stage.getHeight());
                            stage.setY(screenY);
                        }
                    }
                    else {
                        // S, SW, SE
                        if (stage.getHeight() > minHeight || sceneY + startY - stage.getHeight() > 0) {
                            stage.setHeight(sceneY + startY);
                        }
                    }
                }

                if (!Cursor.MOVE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
                    // W, NW, SW, E, NE, SE
                    double minWidth = stage.getMinWidth() > (edgeThres * 2) ? stage.getMinWidth() : (edgeThres * 2);
                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
                        // W, NW, SW
                        if (stage.getWidth() > minWidth || sceneX < 0) {
                            stage.setWidth(stage.getX() - screenX + stage.getWidth());
                            stage.setX(screenX);
                        }
                    }
                    else {
                        // E, NE, SE
                        if (stage.getWidth() > minWidth || sceneX + startX - stage.getWidth() > 0) {
                            stage.setWidth(sceneX + startX);
                        }
                    }
                }
            }
        }
    }
}
