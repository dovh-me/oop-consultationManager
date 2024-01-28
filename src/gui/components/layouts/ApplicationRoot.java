package gui.components.layouts;

import gui.components.Page;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import main.GUIApplication;

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
        this.getStylesheets().add("/assets/styles/styles.css");
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
        this.activePage.onExit();
        Page prevPage = this.activePage.getPrevPage();
        prevPage = prevPage.getPrevNavigationRedirect() != null? prevPage.getPrevNavigationRedirect(): prevPage;
        this.setActivePage(prevPage);
        if(prevPage != null) prevPage.setVisible(true);
        this.activePage.onNavigation();
        contentPanel.setCenter(this.activePage);
        applicationTitle.setText(this.activePage.getTitle());
    }

    public void navigateTo(Page pageToNavigate) {
        Page currentPage = this.activePage;
        this.activePage.setVisible(false);
        this.activePage.onExit();
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
        GridPane pane = new GridPane();
        ColumnConstraints fullWidthConstraint = new ColumnConstraints();
        fullWidthConstraint.setPercentWidth(100);
        pane.getColumnConstraints().add(fullWidthConstraint);
        backNavigationButton.setGraphic(new ImageView(new Image("/assets/back-arrow.png")));
        backNavigationButton.setOnAction(e -> navigateToPrev());

        Button closeButton = new Button("X");
        closeButton.getStyleClass().add("close-btn");
        closeButton.setMaxSize(15,15);
        closeButton.setOnAction(event -> {
            GUIApplication.primaryStage.close();
        });
        headerPanel.setStyle("-fx-background-color: #2f17a6;");
        headerPanel.setPadding(new Insets(2));

        applicationTitle.setAlignment(Pos.CENTER);
        applicationTitle.setStyle("-fx-text-fill: white;");

        headerPanel.setCenter(applicationTitle);
        headerPanel.setLeft(backNavigationButton);
        headerPanel.setRight(closeButton);
        pane.addRow(0, headerPanel);

        // init navbar
        FlowPane navBarPane = new FlowPane();
        Button mainMenuButton = createNavBarButton("Main Menu", new ImageView("/assets/home-icon.png"));
        Button addConsultationButton = createNavBarButton("Add Consultation", new ImageView("/assets/medical-add.png"));
        Button viewConsultationsButton = createNavBarButton("View Consultations", new ImageView("/assets/medical-view.png"));
        Button viewDoctorsButton =createNavBarButton("View Doctors", new ImageView("/assets/doctor.png"));

        // Register mouse click event listeners
        mainMenuButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getMainMenu()));
        addConsultationButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getCheckAvailability()));
        viewConsultationsButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getViewConsultations()));
        viewDoctorsButton.setOnAction(actionEvent -> GUIApplication.app.af.navigateTo(GUIApplication.app.getViewDoctors()));

        navBarPane.setAlignment(Pos.CENTER_LEFT);
        navBarPane.setHgap(5);
        navBarPane.setPadding(new Insets(5));
        navBarPane.setStyle("-fx-background-color: #bad8ff;");

        navBarPane.getChildren().addAll(mainMenuButton,addConsultationButton,viewConsultationsButton,viewDoctorsButton);
        pane.addRow(1, navBarPane);

        this.setTop(pane);
    }

    private Button createNavBarButton(String titleText, ImageView buttonGraphic) {
        buttonGraphic.setPreserveRatio(true);
        buttonGraphic.setFitWidth(20);

        Button btn = new Button(titleText);

        btn.getStyleClass().add("nav-btn");
        btn.setTooltip(new Tooltip(titleText));
        btn.setGraphic(buttonGraphic);
        btn.setGraphicTextGap(5);
        btn.setContentDisplay(ContentDisplay.LEFT);
        btn.setPrefHeight(30);
        btn.setPrefWidth(170);
        return btn;
    }

    private void intiFooterPanel(String rightsText) {
        FlowPane pane = new FlowPane();
        Label rightsLabel = new Label(rightsText);
        rightsLabel.setStyle("-fx-text-fill: white;");
        pane.setAlignment(Pos.CENTER);
        pane.setStyle("-fx-background-color: #2f17a6;");
        pane.setPadding(new Insets(5));
        pane.getChildren().add(rightsLabel);
        this.setBottom(pane);
    }
}
