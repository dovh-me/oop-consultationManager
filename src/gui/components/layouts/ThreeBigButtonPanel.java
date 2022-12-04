package gui.components.layouts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class ThreeBigButtonPanel extends JPanel{
    JPanel centerPanel = new JPanel();

    public ThreeBigButtonPanel(JButton... buttons) {
        // Style the content panel
        this.setLayout(new GridLayout(1,3));
        this.add(centerPanel, BorderLayout.CENTER);
        this.centerPanel.setBackground(Color.RED);

        Dimension btnDimension = new Dimension(200, 200);
        for (JButton button : buttons) {
            // Set dimensions for the buttons
            button.setPreferredSize(btnDimension);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            // Add buttons to the content panel
            centerPanel.add(button);
        }
    }
}