package gui.pages;

import gui.components.layouts.ApplicationFrame;
import gui.components.layouts.ThreeBigButtonPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends Page {
    private JButton addConsultationButton;
    private JButton viewConsultationsButton;
    private JButton viewDoctors;

    public MainMenu() {
        this.initButtons();

        this.setLayout(new BorderLayout());
        ThreeBigButtonPanel mainPanel = new ThreeBigButtonPanel(
                this.addConsultationButton, this.viewConsultationsButton, this.viewDoctors
        );
        this.add(mainPanel, BorderLayout.CENTER);
        this.setBackground(Color.BLUE);
        ApplicationFrame.setApplicationTitle("Main Menu");
    }

    private void initButtons() {
        // Initialize buttons
        this.addConsultationButton = new JButton("Add Consultation", new ImageIcon(this.getClass().getResource("/assets/consultation.png")));
        this.viewConsultationsButton = new JButton("View Consultations", new ImageIcon(this.getClass().getResource("/assets/consultation.png")));
        this.viewDoctors = new JButton("View Doctors", new ImageIcon(this.getClass().getResource("/assets/doctor.png")));

        // Register mouse click event listeners
        this.addConsultationButton.addActionListener(( actionEvent) -> {
        });

        this.viewConsultationsButton.addActionListener((actionEvent -> {

        }));

        this.viewDoctors.addActionListener((actionEvent -> {
            Application.app.af.navigateTo(Application.app.getViewDoctors());
        }));
    }
}
