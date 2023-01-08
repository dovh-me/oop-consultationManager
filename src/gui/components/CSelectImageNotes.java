package gui.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.AlertBox;
import util.ConsoleLog;

import java.io.File;
import java.util.ArrayList;

public class CSelectImageNotes extends VBox {
    private final ObservableList<File> selectedFiles;
    private final VBox recordPane;

    public CSelectImageNotes() {
        FileChooser fileChooser = configFileChooser();
        this.selectedFiles = FXCollections.observableList(new ArrayList<>());
        Label validationMessage = new Label();
        Button selectFileButton = new Button("Select File");
        ColumnConstraints halfConstraint = new ColumnConstraints();
        halfConstraint.setPercentWidth(50);
        this.recordPane = new VBox();
        ScrollPane scrollWrapper = new ScrollPane();
        scrollWrapper.setVmax(110);
        scrollWrapper.setMinHeight(110);
        scrollWrapper.setMaxHeight(110);
        scrollWrapper.setMinWidth(420);
        scrollWrapper.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollWrapper.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        recordPane.setMinHeight(160);
        recordPane.setMinWidth(400);
        recordPane.setAlignment(Pos.CENTER);

        this.selectedFiles.addListener((ListChangeListener<File>) (change) -> {
            if(change.next() && change.wasAdded()) {
                for (File file : change.getAddedSubList()) {
                    addSelectedFileRecord(file);
                }
            }
        });

        try {
            selectFileButton.setOnAction((event) -> {
                handleFileChoose(fileChooser.showOpenDialog(new Stage()));
            });
        } catch (Exception e) {
            validationMessage.setText("There was an error selecting the file");
            ConsoleLog.error("There was an exception selecting image note");
            e.printStackTrace();
        }

        scrollWrapper.setContent(recordPane);
        // add elements to the main pane
        GridPane topPaneWrapper = new GridPane();
        topPaneWrapper.getColumnConstraints().addAll(halfConstraint, halfConstraint);
        topPaneWrapper.addRow(0, selectFileButton,validationMessage);
        this.getChildren().addAll(topPaneWrapper, scrollWrapper);
    }

    private void addSelectedFileRecord(File file) {
        Button removeRecordButton = new Button("X");
        Label recordURL = new Label(file.getAbsolutePath());
        GridPane pane = new GridPane();

        removeRecordButton.setOnAction((event) -> {
            System.out.println("Closing event triggered");
            selectedFiles.remove(file);
            this.recordPane.getChildren().remove(pane);
        });

        //layout
        recordURL.setMaxWidth(240);
        recordURL.setWrapText(false);
        recordURL.setTooltip(new Tooltip(file.getAbsolutePath()));
        pane.setAlignment(Pos.CENTER_RIGHT);
        pane.setMaxWidth(300);
        pane.getColumnConstraints().addAll(new ColumnConstraints(240), new ColumnConstraints(40));
        pane.setHgap(20);

        // styling
        removeRecordButton.setStyle("-fx-background-color: #f00");

        // add record to the main pane
        pane.addRow(0,recordURL, removeRecordButton);
        this.recordPane.getChildren().add(pane);
    }

    private void handleFileChoose(File file) {
        if(file == null || !file.isFile() || !file.exists()) return;
        if(this.selectedFiles.contains(file)) {
            AlertBox.showWarningAlert("File already selected");
            return;
        }
        selectedFiles.add(file);
    }

    private FileChooser configFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image...");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("PNG", "*.png"),
            new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );

        return fileChooser;
    }

    public void resetSelectedFiles() {
        this.selectedFiles.removeAll();
        // remove all elements except for the selectFileButton and validationMessage
        this.recordPane.getChildren().removeIf((e) -> true);
    }

    public ArrayList<File> getSelectedFiles() {
        return new ArrayList<>(selectedFiles);
    }

    public void setSelectedFiles(ArrayList<File> selectedFiles) {
        this.selectedFiles.setAll(selectedFiles);
    }
}
