package se206.quinzical.views;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class IconView extends View {
	private final ImageView _image = new ImageView();
	private final HBox _container = new HBox(_image);

	public IconView(String pathRelativeToIconView) {
		this();
		setImage(pathRelativeToIconView);
	}

	public IconView(Image img) {
		this();
		setImage(img);
	}

	public IconView() {
		_image.setPreserveRatio(true);
		_image.setSmooth(true);
		_image.setCache(true);
		_image.getStyleClass().add("icon-view-image");
		_container.getStyleClass().add("icon-view");
		_container.setAlignment(Pos.CENTER);
	}

	public IconView setImage(String pathRelativeToIconView) {
		_image.setImage(new Image(IconView.class.getResourceAsStream(pathRelativeToIconView)));
		return this;
	}

	public IconView setImage(Image img) {
		_image.setImage(img);
		return this;
	}

	public IconView setSize(int width, int height) {
		setContainerSize(width, height);
		setImageSize(width, height);
		return this;
	}

	public IconView setContainerSize(int width, int height) {
		_container.setPrefWidth(width);
		_container.setPrefHeight(height);
		return this;
	}

	public IconView setImageSize(int width, int height) {
		_image.setFitWidth(width);
		_image.setFitHeight(height);
		return this;
	}

	public IconView setAlignment(Pos pos) {
		_container.setAlignment(pos);
		return this;
	}

	public IconView addClass(String... class_) {
		_container.getStyleClass().addAll(class_);
		return this;
	}

	@Override
	public HBox getView() {
		return _container;
	}
}
