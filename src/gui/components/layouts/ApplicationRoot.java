package gui.components.layouts;

import gui.components.Page;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ApplicationRoot extends BorderPane {
    private static final Label applicationTitle = new Label();
    private final Button backNavigationButton = new Button();
    private final BorderPane headerPanel =  new BorderPane();
    private final BorderPane contentPanel = new BorderPane();
    private final Page defaultActivePage;
    private Page activePage;
    private ArrayList<Page> pages;

    public static void setApplicationTitle(String title) {
        applicationTitle.setText(title);
    }


    public ApplicationRoot(String frameTitle, String rightsText, Page activePage,
                           Page... pages) throws HeadlessException {
        this.setActivePage(activePage);
        this.defaultActivePage = activePage;
        this.initHeaderPanel();
        this.initContentPanel(pages);
        this.intiFooterPanel(rightsText);
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
        this.backNavigationButton.setDisable(this.activePage.getPrevPage() == null);
    }

    public void navigateToPrev() {
        this.activePage.setVisible(false);
        Page prevPage = this.activePage.getPrevPage();
        this.setActivePage(prevPage);
        if(prevPage != null) prevPage.setVisible(true);
        this.activePage.onNavigation();
        contentPanel.setCenter(this.activePage);
    }

    public void navigateTo(Page pageToNavigate) {
        Page currentPage = this.activePage;
        this.activePage.setVisible(false);
        pageToNavigate.setPrevPage(currentPage);
        this.setActivePage(pageToNavigate);
        this.activePage.onNavigation();
        this.activePage.setVisible(true);
        contentPanel.setCenter(this.activePage);
        applicationTitle.setText(this.activePage.getTitle());
    }

    public void resetPrevPages() {
        for (Page page : this.pages) {
            page.setPrevPage(null);
        }
    }

    private void initContentPanel(Page[] pages) {
        this.pages = new ArrayList<>(Arrays.asList(pages));

        this.contentPanel.setCenter(this.activePage);
        this.setCenter(contentPanel);
    }

    private void initHeaderPanel() {
        backNavigationButton.setGraphic(new ImageView(new Image("/assets/back-arrow.png")));
        backNavigationButton.setOnAction(e -> navigateToPrev());

        applicationTitle.setAlignment(Pos.CENTER);

        headerPanel.setCenter(applicationTitle);
        headerPanel.setLeft(backNavigationButton);
        this.setTop(headerPanel);
    }

    private void intiFooterPanel(String rightsText) {
        FlowPane pane = new FlowPane();
        Label rightsLabel = new Label(rightsText);
        pane.setAlignment(Pos.CENTER);
        pane.getChildren().add(rightsLabel);
        this.setBottom(pane);
    }
}
