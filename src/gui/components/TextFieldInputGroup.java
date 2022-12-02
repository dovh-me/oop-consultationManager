package gui.components;

import util.Validator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

public class TextFieldInputGroup extends JPanel {
    JTextField inputField;
    JLabel inputLabel;

    public TextFieldInputGroup(String title, Validator<String>... onChangeValidators) {
        this.setLayout(new BorderLayout());

        this.inputField = new JTextField();
        this.inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                boolean isValid = false;
                for(Validator<String> validator: onChangeValidators) {
                    isValid = validator.validate(Character.toString(e.getKeyChar()));

                    if(!isValid) {
                        e.consume();
                        break;
                    }
                }
            }
        });
        this.inputLabel = new JLabel(title);

        this.add(this.inputLabel,BorderLayout.EAST);
        this.add(this.inputField, BorderLayout.WEST);
    }

    public String getText() {
        return this.inputField.getText();
    }
}
