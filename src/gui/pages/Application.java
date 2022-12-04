package gui.pages;

import gui.components.layouts.ApplicationFrame;
import main.WestminsterSkinConsultationManager;
import util.ConsoleLog;

import javax.swing.*;
import javax.swing.text.View;

public class Application {
    public static Application app;
    public WestminsterSkinConsultationManager manager;
    public ApplicationFrame af;
    private MainMenu mainMenu;
    private ViewDoctors viewDoctors;

    public static void start(WestminsterSkinConsultationManager manager) {
        if(Application.app == null) {
            ConsoleLog.info("Starting GUI...");
            Application.app = new Application(manager);
            ConsoleLog.success("Successfully started the GUI instance of the application.");
        }
        else ConsoleLog.info("An instance of the application is already running.");
    }

    public Application(WestminsterSkinConsultationManager manager) {
        this.manager = manager;
        initPages();

        af = new ApplicationFrame(
                "Westminster Skin Consultation Manager",
                "Developed and distributed by Osura Hettiarachchi",
                this.mainMenu
        );
        af.load();
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public ViewDoctors getViewDoctors() {
        return viewDoctors;
    }

    private void initPages() {
        this.mainMenu = new MainMenu();
        this.viewDoctors = new ViewDoctors(this.manager.getDoctors());
    }
}
