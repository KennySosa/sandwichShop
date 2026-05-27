package com.pularsight.ENUMS;

/*
 * ToppingCategory classifies toppings by their pricing tier.
 *
 * separated from toppingType cuz
 *   ToppingType is the which topping
 *   ToppingCategory is the pricing rule
 *   Adding a new category in the future only requires
 *   changing this enum and the pricing method
 */
public enum ToppingCategory {
    MEAT    ("Meat",    true),
    CHEESE  ("Cheese",  true),
    REGULAR ("Regular", false),
    SAUCE   ("Sauce",   false);

    private final String  displayName;
    private final boolean isPremium;

    ToppingCategory(String displayName, boolean isPremium) {
        this.displayName = displayName;
        this.isPremium   = isPremium;
    }

    public String  getDisplayName() { return displayName; }
    public boolean isPremium()      { return isPremium;   }

    @Override
    public String toString() { return displayName; }
}