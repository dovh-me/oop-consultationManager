package main;

import gui.components.layouts.ApplicationRoot;
import gui.pages.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import util.ConsoleLog;

public class GUIApplication extends Application{
    public static Stage primaryStage;
    public static GUIApplication app;
    public WestminsterSkinConsultationManager manager;
    public ApplicationRoot af;
    private MainMenu mainMenu;
    private ViewDoctors viewDoctors;
    private ViewConsultations viewConsultations;
    private CheckAvailability checkAvailability;
    private AddConsultation patientInfo;
    private ViewConsultation viewConsultation;

    private static WestminsterSkinConsultationManager mTemp;

    public static void start(WestminsterSkinConsultationManager manager) {
        try {
            GUIApplication.mTemp = manager;
            ConsoleLog.info("Starting GUI...");
            GUIApplication.launch(GUIApplication.class);
            ConsoleLog.success("GUI instance stopped...");
        } catch (IllegalStateException e) {
            ConsoleLog.error("Unfortunately you have to restart the CLI application to respawn a GUI. Sorry for the inconvenience caused.");
        }
    }

    public void initializeConsultationManager(WestminsterSkinConsultationManager manager) {
        this.manager = manager;
        initPages();

        af = new ApplicationRoot(
                "Westminster Skin Consultation Manager",
                "Developed and distributed by Osura Hettiarachchi",
                this.mainMenu,
                this.viewConsultations,
                this.viewDoctors,
                this.checkAvailability,
                this.patientInfo,
                this.viewConsultation
        );
        af.load();
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public ViewDoctors getViewDoctors() {
        return viewDoctors;
    }

    public CheckAvailability getCheckAvailability() {
        return checkAvailability;
    }

    public ViewConsultations getViewConsultations() {
        return viewConsultations;
    }

    public AddConsultation getPatientInfo() {
        return patientInfo;
    }

    public ViewConsultation getViewConsultation() {
        return viewConsultation;
    }

    private void initPages() {
        this.mainMenu = new MainMenu();
        this.viewDoctors = new ViewDoctors();
        this.checkAvailability = new CheckAvailability();
        this.viewConsultations = new ViewConsultations();
        this.patientInfo = new AddConsultation();
        this.viewConsultation = new ViewConsultation();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GUIApplication.app = this;
        GUIApplication.primaryStage = primaryStage;

        // TODO: make mTemp field non static or refactor
        initializeConsultationManager(GUIApplication.mTemp);
        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.setAlwaysOnTop(true);
        primaryStage.setFullScreen(true);
        primaryStage.show();

        root.getChildren().add(af);
    }
}
