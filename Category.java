package models;

public enum Category {
    FOOD("Food 🍔"),
    TRAVEL("Travel 🚗"),
    SHOPPING("Shopping 🛍️"),
    BILLS("Bills 💡"),
    ENTERTAINMENT("Entertainment 🎮"),
    HEALTH("Health 🏥"),
    CUSTOM("Custom");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
