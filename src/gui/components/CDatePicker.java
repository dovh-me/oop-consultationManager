package gui.components;

import constants.Formats;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CDatePicker extends DatePicker {

    private LocalDate allowedStart;
    private LocalDate allowedEnd;

    public CDatePicker() {
        super();

        this.setConverter(new StringConverter<LocalDate>() {
            final DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(Formats.DATE_FORMAT);
            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    LocalDate ld = LocalDate.parse(string, dateFormatter);
                    if(allowedStart != null && ld.isBefore(allowedStart)) return null;
                    if(allowedEnd != null && ld.isAfter(allowedEnd)) return null;
                    return ld;
                } else {
                    return null;
                }
            }
        });

        this.setPromptText(Formats.DATE_FORMAT.toLowerCase());

        final Callback<DatePicker, DateCell> dayCellFactory =
                new Callback<DatePicker, DateCell>() {
                    @Override
                    public DateCell call(final DatePicker datePicker) {
                        return new DateCell() {
                            @Override
                            public void updateItem(LocalDate item, boolean empty) {
                                super.updateItem(item, empty);



                                if (allowedStart != null && item.isBefore(allowedStart)) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }

                                if(allowedEnd != null && item.isAfter(allowedEnd)) {
                                    setDisable(true);
                                    setStyle("-fx-background-color: #ffc0cb;");
                                }
                            }
                        };
                    }
                };
        this.setDayCellFactory(dayCellFactory);
    }

    public void setAllowedRange(LocalDate allowedStart, LocalDate allowedEnd) {
        this.allowedStart = allowedStart;
        this.allowedEnd = allowedEnd;
    }

    public void setAllowedStart(LocalDate allowedStart) {
        this.allowedStart = allowedStart;
    }

    public void setAllowedEnd(LocalDate allowedEnd) {
        this.allowedEnd = allowedEnd;
    }
}
