package gui.components;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import util.AlertBox;
import util.ConsoleLog;

import java.io.ByteArrayInputStream;
import java.util.List;

public class CVerticalImageGallery extends VBox {
    private final VBox mainPane;

    public CVerticalImageGallery() {
        mainPane = new VBox();
        mainPane.setSpacing(10);
        this.getChildren().add(mainPane);
    }

    public void loadContent(List<byte[]> files) {
        try {
            mainPane.getChildren().removeIf((node -> true)); // removes all
            for (byte[] fileBytes : files) {
                    mainPane.getChildren().add(createImageItem(fileBytes));

            }
        } catch (Exception e) {
            ConsoleLog.error(e.getLocalizedMessage());
            AlertBox.showErrorAlert("There was an error loading note images");
        }
    }

    private VBox createImageItem(byte[] imageByteArray) {
        ImageView imageView  = new ImageView(new Image(new ByteArrayInputStream(imageByteArray)));
        VBox pane =  new VBox();

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        imageView.maxHeight(300);
        imageView.maxWidth(300);

        pane.setSpacing(5);
        pane.getChildren().add(imageView);

        return pane;
    }
}
