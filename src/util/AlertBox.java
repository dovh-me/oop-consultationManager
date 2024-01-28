package util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertBox {
    private static final Alert alert = new Alert(Alert.AlertType.NONE);

    public static void showErrorAlert(String m) {
        setAlertProps("Error", m, Alert.AlertType.ERROR);
        alert.show();
    }

    public static void showWarningAlert(String m) {
        setAlertProps("Warning", m, Alert.AlertType.WARNING);
        alert.show();
    }

    public static boolean showConfirmationAlert(String m) {
        setAlertProps("Confirmation", m, Alert.AlertType.CONFIRMATION);
        return alert.showAndWait()
                .filter(response -> response == ButtonType.OK).isPresent();
    }

    public static void showInformationAlert(String m) {
        setAlertProps("Information", m, Alert.AlertType.INFORMATION);
        alert.show();
    }

    private static void setAlertProps(String title, String message, Alert.AlertType alertType) {
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeight(300);
        alert.setWidth(400);
        alert.setAlertType(alertType);
    }
}
