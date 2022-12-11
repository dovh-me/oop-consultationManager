package gui.components;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import util.ConsoleLog;

public abstract class Page extends StackPane {
    private Page prevPage;

    public Page() {
        super();
        this.setAlignment(Pos.CENTER);
    }

    public void onNavigation() {
        ConsoleLog.info("Loading " + this.getTitle() + " page...");
    };

    public Page getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(Page prevPage) {
        this.prevPage = prevPage;
    }

    public abstract String getTitle();
}