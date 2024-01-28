package gui.components.layouts;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.awt.*;

public class ThreeBigButtonPanel extends GridPane {

    public ThreeBigButtonPanel(Button... buttons) {
        super();
        this.setHgap(5);
        this.setAlignment(Pos.CENTER);

        for (int i =0;i< buttons.length; i++) {
            // Add buttons to the content panel
            this.add(buttons[i],i,0);
        }
        this.setMinSize(600,200);
    }
}