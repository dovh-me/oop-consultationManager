package gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class ApplicationFrame extends JFrame {
    private Stack<JPanel> backNavigation = new Stack<>();
    private JPanel active;
    private JPanel mainMenuPanel;
    private JPanel doctorsMainPanel;
    private JPanel addDoctorPanel;
    private JPanel viewDoctorPanel;
    private JPanel editDoctorPanel;
    private JPanel consultationsMainPanel;
    private JPanel addConsultation;
    private JPanel viewConsultations;

    public ApplicationFrame(JPanel mainMenuPanel, JPanel doctorsMainPanel, JPanel addDoctorPanel, JPanel viewDoctorPanel, JPanel editDoctorPanel, JPanel consultationsMainPanel, JPanel addConsultation, JPanel viewConsultations) throws HeadlessException {
        this.mainMenuPanel = mainMenuPanel;
        this.doctorsMainPanel = doctorsMainPanel;
        this.addDoctorPanel = addDoctorPanel;
        this.viewDoctorPanel = viewDoctorPanel;
        this.editDoctorPanel = editDoctorPanel;
        this.consultationsMainPanel = consultationsMainPanel;
        this.addConsultation = addConsultation;
        this.viewConsultations = viewConsultations;

        this.active = this.mainMenuPanel;
    }

    public void start() {
        ApplicationFrame af = new ApplicationFrame(
                new TwoButtonMainPanel(
                        new JButton("Doctors"), new JButton("Consultations"), new JLabel("Main Menu")
                ),
                new TwoButtonMainPanel(
                        new JButton("Add Doctor"), new JButton("View Doctors"), new JLabel("Doctors")
                ),
                new JPanel(),
                new JPanel(),
                new JPanel(),
                new JPanel(),
                new JPanel(),
                new JPanel()
                );
    }
}
