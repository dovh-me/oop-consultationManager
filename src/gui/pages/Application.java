package gui.pages;

import gui.components.ApplicationFrame;

import javax.swing.*;

public class Application {
    public static ApplicationFrame af;

    public void start() {
        af = new ApplicationFrame(
                "Westminster Consultation Manager",
                new MainMenu()
        );

        af.setSize(600, 800);
        af.setVisible(true);
        af.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
