package gui.components.layouts;

import gui.pages.Page;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ApplicationFrame extends JFrame {
    private static final JLabel applicationTitle = new JLabel();
    private final JButton backNavigationButton = new JButton();
    private final JPanel headerPanel =  new JPanel();
    private final JPanel contentPanel = new JPanel();
    private final Page defaultActivePage;
    private Page activePage;
    private ArrayList<Page> pages;

    public static void setApplicationTitle(String title) {
        applicationTitle.setText(title);
    }


    public ApplicationFrame(String frameTitle, String rightsText,Page activePage,
                            Page... pages) throws HeadlessException {
        this.setActivePage(activePage);
        this.defaultActivePage = activePage;
        this.setLayout(new BorderLayout());
        this.setTitle(frameTitle);
        this.initHeaderPanel();
        this.initContentPanel(pages);
        this.intiFooterPanel(rightsText);

        this.setSize(600, 800);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public void load() {
        this.setVisible(true);
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
        this.backNavigationButton.setEnabled(this.activePage.getPrevPage() != null);
    }

    public void navigateToPrev() {
        this.activePage.setVisible(false);
        Page prevPage = this.activePage.getPrevPage();
        this.setActivePage(prevPage);
        if(prevPage != null) prevPage.setVisible(true);
        contentPanel.add(this.activePage, BorderLayout.CENTER);
    }

    public void navigateTo(Page pageToNavigate) {
        Page currentPage = this.activePage;
        this.activePage.setVisible(false);
        pageToNavigate.setPrevPage(currentPage);
        this.setActivePage(pageToNavigate);
        this.activePage.setVisible(true);
        contentPanel.add(this.activePage, BorderLayout.CENTER);
    }

    public void resetPrevPages() {
        for (Page page : this.pages) {
            page.setPrevPage(null);
        }
    }

    private void initContentPanel(Page[] pages) {
        this.pages = new ArrayList<>(List.of(pages));

        this.contentPanel.setLayout(new BorderLayout());
        this.contentPanel.add(this.activePage, BorderLayout.CENTER);
        this.add(contentPanel, BorderLayout.CENTER);
    }

    private void initHeaderPanel() {
        backNavigationButton.setIcon(new ImageIcon(this.getClass().getResource("/assets/back-arrow.png")));
        backNavigationButton.addActionListener(e -> navigateToPrev());

        applicationTitle.setHorizontalAlignment(SwingConstants.CENTER);

        headerPanel.setLayout(new BorderLayout());
        headerPanel.add(applicationTitle, BorderLayout.CENTER);
        headerPanel.add(backNavigationButton, BorderLayout.LINE_START);
        this.add(headerPanel, BorderLayout.PAGE_START);
    }

    private void intiFooterPanel(String rightsText) {
        JLabel rightsLabel = new JLabel(rightsText);
        rightsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(rightsLabel, BorderLayout.PAGE_END);
    }
}
