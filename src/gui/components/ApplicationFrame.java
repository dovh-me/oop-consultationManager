package gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ApplicationFrame extends JFrame {
    private final JLabel applicationTitle = new JLabel();
    private final JButton backNavigationButton = new JButton();
    private final JPanel headerPanel =  new JPanel();
    private final JLayeredPane contentPanel = new JLayeredPane();
    private final Page defaultActivePage;
    private Page activePage;
    private ArrayList<Page> pages;

    public ApplicationFrame(String frameTitle, Page activePage,
                            Page... pages) throws HeadlessException {
        this.defaultActivePage = activePage;
        this.activePage = this.defaultActivePage;
        this.setLayout(new BorderLayout());
        this.setTitle(frameTitle);
        this.initHeaderPanel();
        this.initContentPanel(pages);
    }

    private void initContentPanel(Page[] pages) {
        for (Page page : pages) {
            this.contentPanel.add(page);
        }
        this.add(contentPanel, BorderLayout.CENTER);
    }

    private void initHeaderPanel() {
        backNavigationButton.addActionListener(e -> activePage.navigateBack());

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(applicationTitle, BorderLayout.CENTER);
        headerPanel.add(backNavigationButton);
    }

    public void setApplicationTitle(String title) {
        applicationTitle.setText(title);
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public Page getDefaultActivePage() {
        return defaultActivePage;
    }

    public Page getActivePage() {
        return activePage;
    }

    public void setActivePage(Page activePage) {
        this.activePage = activePage;
    }
}
