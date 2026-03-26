package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private String id;
    private String username;
    private double amount;
    private Category category;
    private LocalDate date;
    private String note;

    public Expense(String username, double amount, Category category, String note) {
        this.id = java.util.UUID.randomUUID().toString().substring(0, 8);
        this.username = username;
        this.amount = amount;
        this.category = category;
        this.date = LocalDate.now();
        this.note = note;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getUsername() { return username; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }
}
