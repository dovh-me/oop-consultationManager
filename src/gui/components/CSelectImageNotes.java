package gui.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.ConsoleLog;

import java.io.File;
import java.util.LinkedList;

public class CSelectImageNotes extends VBox {
    private LinkedList<File> selectedFiles;
    private Label validationMessage;
    private Button selectFileButton;

    public CSelectImageNotes() {
        FileChooser fileChooser = configFileChooser();
        this.selectedFiles = new LinkedList<>();
        this.validationMessage = new Label();
        this.selectFileButton = new Button("Select File");

        try {
            selectFileButton.setOnAction((event) -> {
                handleFileChoose(fileChooser.showOpenDialog(new Stage()));
            });
        } catch (Exception e) {
            this.validationMessage.setText("There was an error selecting the file");
            ConsoleLog.error("There was an exception selecting image note");
            e.printStackTrace();
        }

        // add elements to the main pane
        this.getChildren().addAll(selectFileButton, validationMessage);
    }

    private void addSelectedFileRecord(File file) {
        Button removeRecordButton = new Button("X");
        Label recordURL = new Label(file.toString());
        FlowPane pane = new FlowPane();

        selectedFiles.add(file);
        removeRecordButton.setOnAction((event) -> {
            selectedFiles.remove(file);
            this.getChildren().remove(pane);
        });

        //layout
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.prefWidth(200);
        pane.setHgap(10);

        // styling
        removeRecordButton.setStyle("-fx-background-color: #f00");

        // add record to the main pane
        pane.getChildren().addAll(recordURL, removeRecordButton);
        this.getChildren().add(pane);
    }

    private void handleFileChoose(File file) {
        if(file == null || !file.isFile() || !file.exists()) return;

        addSelectedFileRecord(file);
    }

    private FileChooser configFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image...");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        return fileChooser;
    }

    public void resetSelectedFiles() {
        this.selectedFiles = new LinkedList<>();
        // remove all elements except for the selectFileButton and validationMessage
        this.getChildren().removeIf((e) -> e != selectFileButton || e != validationMessage);
    }

    public LinkedList<File> getSelectedFiles() {
        return selectedFiles;
    }
}
