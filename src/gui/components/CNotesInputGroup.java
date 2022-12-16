package gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import util.GUIValidator;

import javax.sound.sampled.Line;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CNotesInputGroup extends GridPane{
    private Label inputLabel;
    private Label validationMessage;
    private TextArea textNotes;
    private CSelectImageNotes imageNotes;

    public CNotesInputGroup(String title) {
        this.add(new Label(title),0,0);
        initRootPane();
        this.getColumnConstraints().add(new ColumnConstraints(250));
    }

    public void resetFields() {
        this.textNotes.setText("");
        this.imageNotes.resetSelectedFiles();
    }

    private void initRootPane() {
        this.validationMessage = new Label();
        this.textNotes = new TextArea();
        this.imageNotes = new CSelectImageNotes();

        textNotes.setPrefColumnCount(15);
        textNotes.setPrefRowCount(8);

        this.add(validationMessage, 1,0);
        this.add(textNotes, 1,1);
        this.add(imageNotes, 1,2);
    }

    public String getTextNotes() {
        return textNotes.getText();
    }

    public LinkedList<File> getImageNotes() {
        return imageNotes.getSelectedFiles();
    }
}
