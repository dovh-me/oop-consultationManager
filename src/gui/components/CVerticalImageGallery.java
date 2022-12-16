package gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import util.AlertBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.List;

public class CVerticalImageGallery extends VBox {
    private final VBox mainPane;

    public CVerticalImageGallery() {
        mainPane = new VBox();
        mainPane.setSpacing(10);
        this.getChildren().add(mainPane);
    }

    public void loadContent(List<File> files) {
        try {
            for (File file : files) {
                try {
                    mainPane.getChildren().add(createImageItem(file));
                } catch (MalformedURLException | FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            AlertBox.showErrorAlert("There was an error loading note images");
        }
    }

    private VBox createImageItem(File file) throws IOException {
        System.out.println(file.toURI().toURL());
        ImageView imageView  = new ImageView(new Image(Files.newInputStream(file.toPath())));
        VBox pane =  new VBox();

        imageView.setPreserveRatio(true);
        imageView.setFitWidth(imageView.getImage().getWidth());
        imageView.setFitHeight(imageView.getImage().getHeight());
        imageView.maxHeight(300);
        imageView.maxWidth(300);

        Label imageCaptionLabel = new Label(file.getName());
        imageCaptionLabel.setAlignment(Pos.CENTER);

        pane.setSpacing(5);
        pane.getChildren().addAll(imageView,imageCaptionLabel);

        return pane;
    }
}
