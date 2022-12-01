package gui.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TwoButtonMainPanel extends JPanel{
    JButton leftButton;
    JButton rightButton;
    JLabel titleLabel;
    JPanel titlePanel = new JPanel();
    JPanel centerPanel = new JPanel();

    public TwoButtonMainPanel(JButton leftButton, JButton rightButton, JLabel titleLabel) {
        this.leftButton = leftButton;
        this.rightButton = rightButton;
        this.titleLabel = titleLabel;

        this.setLayout(new BorderLayout());

        // Add the label to the title panel
        titlePanel.add(titleLabel);
        // Add the buttons to the center panel
        centerPanel.add(this.leftButton, BorderLayout.EAST);
        centerPanel.add(this.rightButton, BorderLayout.WEST);

        // Adding stuff to the main panel
        this.add(titlePanel, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
    }
}