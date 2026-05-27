package com.pularsight.ENUMS;


/*
 * drinkSize enum for the three drink sizes offered at shop
 * prices are embedded directly, following the same single source
 * pattern used in SandwichSize.
 */
public enum DrinkSize {
    SMALL  ("Small",  2.00),
    MEDIUM ("Medium", 2.50),
    LARGE  ("Large",  3.00);

    private final String displayName;
    private final double price;

    DrinkSize(String displayName, double price) {
        this.displayName = displayName;
        this.price       = price;
    }

    public String getDisplayName() { return displayName; }
    public double getPrice()       { return price;       }

    @Override
    public String toString() { return displayName; }
}