package gui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.time.LocalTime;

public class CTimeField extends HBox {
    private final TextField hours;
    private final TextField minutes;
    public CTimeField() {
        super();
        hours = new TextField();
        minutes = new TextField();
        StringConverter<Integer> minConverter = new IntRangeStringConverter(0, 59);
        minutes.setTextFormatter(new TextFormatter<>(minConverter, 0));
        hours.setTextFormatter(new TextFormatter<>(new IntRangeStringConverter(0, 23), 0));
        prepareTextField(hours);
        prepareTextField(minutes);


        this.getChildren().addAll(hours, createLabel(),minutes);
        this.setPadding(new Insets(4));
        this.setStyle("-fx-background-color: white;");
    }

    public static void prepareTextField(TextField tf) {
        tf.setAlignment(Pos.CENTER);
        tf.setBackground(Background.EMPTY);
        tf.setBorder(Border.EMPTY);
        tf.setPadding(Insets.EMPTY);
        tf.setPrefColumnCount(2);
    }

    public static class IntRangeStringConverter extends StringConverter<Integer> {

        private final int min;
        private final int max;

        public IntRangeStringConverter(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString(Integer object) {
            return String.format("%02d", object);
        }

        @Override
        public Integer fromString(String string) {
            int integer = Integer.parseInt(string);
            if (integer > max || integer < min) {
                throw new IllegalArgumentException();
            }

            return integer;
        }

    }

    public static Label createLabel() {
        Label label = new Label(":");
        label.setPrefWidth(3);
        return label;
    }

    public LocalTime getValue() {
        try {
            return LocalTime.of(Integer.parseInt(hours.getText()), Integer.parseInt(minutes.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid time format");
            e.printStackTrace();
        }
        return null;
    }

    public void setValue(LocalTime value) {
        this.hours.setText(Integer.toString(value.getHour()));
        this.minutes.setText(Integer.toString(value.getMinute()));
    }

    public void resetValue() {
            this.hours.setText("00");
            this.minutes.setText("00");
    }
}
