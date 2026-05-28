package com.pularsight.ENUMS;

/*
 * ToppingCategory classifies toppings by their pricing tier.
 *
 * separated from toppingType cuz
 *toppingType is the which topping, toppingCategory is the pricing rule
 *adding a new category in the future only requires changing this enum and the pricing method
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