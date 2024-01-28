package gui.components;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.util.ArrayList;

public class CNotesInputGroup extends GridPane{
    private TextArea textNotes;
    private CSelectImageNotes imageNotes;

    public CNotesInputGroup(String title) {
        this.add(new Label(title),0,0);
        initRootPane();

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(50);
        this.getColumnConstraints().addAll(columnConstraints, columnConstraints);
    }

    public void resetFields() {
        this.textNotes.setText("");
        this.imageNotes.resetSelectedFiles();
    }

    private void initRootPane() {
        Label validationMessage = new Label();
        this.textNotes = new TextArea();
        this.imageNotes = new CSelectImageNotes();

        textNotes.setPrefColumnCount(15);
        textNotes.setPrefRowCount(8);

        this.setVgap(10);
        this.add(validationMessage, 1,0);
        this.add(textNotes, 1,1);
        this.add(imageNotes, 1,2);
    }

    public String getTextNotes() {
        return textNotes.getText();
    }

    public ArrayList<File> getImageNotes() {
        return imageNotes.getSelectedFiles();
    }

    public void setTextNotes(String text) {
        this.textNotes.setText(text);
    }

    public void setImageNotes(ArrayList<File> noteImages) {
        this.imageNotes.setSelectedFiles(noteImages);
    }
}
