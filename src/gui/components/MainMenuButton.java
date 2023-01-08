package gui.components;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;

public class MainMenuButton extends Button {
    public MainMenuButton(String text, ImageView graphic) {
        super(text);
        graphic.setFitHeight(100);
        graphic.setFitWidth(100);
        graphic.setPreserveRatio(true);
        this.setGraphic(graphic);
        this.setContentDisplay(ContentDisplay.TOP);
        this.setGraphicTextGap(10);
        this.setMinSize(200, 200);
    }
}
