package gui.pages;

import gui.components.Page;
import gui.components.TwoButtonMainPanel;

import javax.swing.*;

public class MainMenu extends Page {

    private TwoButtonMainPanel mainPanel;

    public MainMenu() {
        mainPanel = new TwoButtonMainPanel(
                new JButton("Add Consultation"),
                new JButton("View Consultations"),
                new JButton("View Doctors"),
                new JLabel("Main Menu")
        );
        this.add(mainPanel);
    }
}
