package gui.pages;

import gui.components.MainMenuButton;
import gui.components.Page;
import gui.components.layouts.ApplicationRoot;
import gui.components.layouts.ThreeBigButtonPanel;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import main.GUIApplication;

public class MainMenu extends Page {
    private BorderPane contentPane;
    private MainMenuButton addConsultationButton;
    private MainMenuButton viewConsultationsButton;
    private MainMenuButton viewDoctorsButton;

    public MainMenu() {
        this.initButtons();

        this.contentPane = new BorderPane();
        ThreeBigButtonPanel mainPanel = new ThreeBigButtonPanel(
                this.addConsultationButton, this.viewConsultationsButton, this.viewDoctorsButton
        );
        this.contentPane.setCenter(mainPanel);
        this.setStyle("-fx-background-color:#6262db");
        this.getChildren().add(contentPane);
        ApplicationRoot.setApplicationTitle("Main Menu");
    }

    @Override
    public String getTitle() {
        return "Main Menu";
    }

    private void initButtons() {
        // Initialize buttons
        this.addConsultationButton = new MainMenuButton("Add Consultation",new ImageView("/assets/consultation.png"));
        this.viewConsultationsButton = new MainMenuButton("View Consultations",new ImageView("/assets/consultation.png"));
        this.viewDoctorsButton = new MainMenuButton("View Doctors",new ImageView("assets/doctor.png"));

        // Register mouse click event listeners
        this.addConsultationButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getCheckAvailability()));

        this.viewConsultationsButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getViewConsultations()));

        this.viewDoctorsButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getViewDoctors()));
    }
}
