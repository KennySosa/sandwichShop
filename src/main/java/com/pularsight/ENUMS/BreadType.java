package com.pularsight.ENUMS;

/**
 * BreadType represents the bread options offered at DELI-cious.
 *
 * WHY AN ENUM?
 *   Bread choice is a fixed, finite set defined by the business.
 *   An enum prevents invalid values (e.g. "sourdough") from ever entering
 *   the system and gives us a strongly-typed parameter instead of a raw String.
 */
public enum BreadType {
    WHITE ("White"),
    WHEAT ("Wheat"),
    RYE   ("Rye"),
    WRAP  ("Wrap");

    private final String displayName;

    BreadType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    @Override
    public String toString() { return displayName; }
}