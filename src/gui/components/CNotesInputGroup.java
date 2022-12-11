package gui.components;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import util.GUIValidator;

import javax.sound.sampled.Line;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CNotesInputGroup extends GridPane{
    private Label validationMessage;
    private TextArea textNotes;
    private CSelectImageNotes imageNotes;

    public CNotesInputGroup() {
        initRootPane();
        initImageNotePane();
    }

    public void resetFields() {
        this.textNotes.setText("");
        this.imageNotes.resetSelectedFiles();
    }

    private void initRootPane() {
        this.validationMessage = new Label();
        this.add(validationMessage, 0,0);
    }

    private void initImageNotePane() {

    }

    private void handleAddImageNote() {

    }

    public String getTextNotes() {
        return textNotes.getText();
    }

    public LinkedList<File> getImageNotes() {
        return imageNotes.getSelectedFiles();
    }
}
