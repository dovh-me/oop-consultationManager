package gui.components;

import gui.pages.Application;

import javax.swing.*;

public abstract class Page extends JPanel {
    private Page prevPage;

    public Page() {
        this.setOpaque(true);
    }

    public void navigateBack() {
        if (this.prevPage != null) {
            // Hide the current page
            this.setVisible(false);
            // Show the prev page
            this.prevPage.setVisible(true);

            // Set the active Page in the application object to the prevPage of the current page
            Application.af.setActivePage(
                    this.prevPage != Application.af.getDefaultActivePage()?
                    this.prevPage: null);
        }
    }

    public void navigateTo(Page prevPage) {
        this.prevPage = prevPage;
        Application.af.setActivePage(this);
    }
}
