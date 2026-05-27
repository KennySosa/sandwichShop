package com.pularsight.ENUMS;

/*
 *toppingType enum represents every possible topping available at shop
 *
 *
 *category field:
 *   storing toppingCategory directly on each constant means toppingType.STEAK.getCategory()
 *   returns toppingCategory.MEAT without any external lookup.  This removes the need for
 *   duplicated if/else chains wherever we need to know whether a topping is premium.
 */
public enum ToppingType {

    // ── Premium: Meats ────────────────────────────────────────────────────────
    STEAK      ("Steak",       ToppingCategory.MEAT),
    HAM        ("Ham",         ToppingCategory.MEAT),
    SALAMI     ("Salami",      ToppingCategory.MEAT),
    ROAST_BEEF ("Roast Beef",  ToppingCategory.MEAT),
    CHICKEN    ("Chicken",     ToppingCategory.MEAT),
    BACON      ("Bacon",       ToppingCategory.MEAT),

    // ── Premium: Cheeses ──────────────────────────────────────────────────────
    AMERICAN   ("American",    ToppingCategory.CHEESE),
    PROVOLONE  ("Provolone",   ToppingCategory.CHEESE),
    CHEDDAR    ("Cheddar",     ToppingCategory.CHEESE),
    SWISS      ("Swiss",       ToppingCategory.CHEESE),

    // ── Regular: Vegetables (always free) ────────────────────────────────────
    LETTUCE    ("Lettuce",     ToppingCategory.REGULAR),
    PEPPERS    ("Peppers",     ToppingCategory.REGULAR),
    ONIONS     ("Onions",      ToppingCategory.REGULAR),
    TOMATOES   ("Tomatoes",    ToppingCategory.REGULAR),
    JALAPENOS  ("Jalapeños",   ToppingCategory.REGULAR),
    CUCUMBERS  ("Cucumbers",   ToppingCategory.REGULAR),
    PICKLES    ("Pickles",     ToppingCategory.REGULAR),
    GUACAMOLE  ("Guacamole",   ToppingCategory.REGULAR),
    MUSHROOMS  ("Mushrooms",   ToppingCategory.REGULAR),

    // ── Regular: Sauces (always free) ────────────────────────────────────────
    MAYO           ("Mayo",            ToppingCategory.SAUCE),
    MUSTARD        ("Mustard",         ToppingCategory.SAUCE),
    KETCHUP        ("Ketchup",         ToppingCategory.SAUCE),
    RANCH          ("Ranch",           ToppingCategory.SAUCE),
    THOUSAND_ISLAND("Thousand Island", ToppingCategory.SAUCE),
    VINAIGRETTE    ("Vinaigrette",     ToppingCategory.SAUCE);

    // ── Fields ────────────────────────────────────────────────────────────────
    private final String          displayName;
    private final ToppingCategory category;

    // ── Constructor ───────────────────────────────────────────────────────────
    ToppingType(String displayName, ToppingCategory category) {
        this.displayName = displayName;
        this.category    = category;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public String          getDisplayName() { return displayName; }
    public ToppingCategory getCategory()    { return category;    }

    // Convenience: is this topping premium (meat or cheese)
    public boolean isPremium() {
        return category == ToppingCategory.MEAT || category == ToppingCategory.CHEESE;
    }

    @Override
    public String toString() { return displayName; }
}