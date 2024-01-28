package gui.components;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import util.ConsoleLog;

public abstract class Page extends StackPane {
    private Page prevPage;
    private Page prevNavigationRedirect = null;

    public Page getPrevNavigationRedirect() {
        return prevNavigationRedirect;
    }

    public void setPrevNavigationRedirect(Page prevNavigationRedirect) {
        this.prevNavigationRedirect = prevNavigationRedirect;
    }

    public Page() {
        super();
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #a5cafa;");
    }

    public void onNavigation() {
        ConsoleLog.info("Loading " + this.getTitle() + " page...");
    }

    public void onExit() {
        ConsoleLog.info("Navigating out of " + this.getTitle() + " page...");
    }

    public Page getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Page prevPage) {
        this.prevPage = prevPage;
    }

    public abstract String getTitle();
}
