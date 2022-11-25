package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Consultation implements Serializable {
    LocalDateTime dateTime;
    private float cost;
    private String notes;

    public Consultation() {
    }

    public Consultation(LocalDateTime dateTime, float cost, String notes) {
        this.dateTime = dateTime;
        this.cost = cost;
        this.notes = notes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
