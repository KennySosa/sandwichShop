package com.pularsight.ENUMS;

/**
 * ToppingCategory classifies toppings by their pricing tier.
 *
 * WHY SEPARATE FROM ToppingType?
 *   Separation of concerns: ToppingType is the "what" (which topping),
 *   ToppingCategory is the "how priced" (which pricing rule applies).
 *   Adding a new category (e.g. PREMIUM_SAUCE) in the future only requires
 *   changing this enum and the pricing method — no hunting through Topping
 *   or Sandwich code.
 *
 *   MEAT and CHEESE are premium and carry size-based costs.
 *   REGULAR and SAUCE are always free (included).
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