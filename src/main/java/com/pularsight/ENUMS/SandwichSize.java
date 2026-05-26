package com.pularsight.ENUMS;

/**
 * SandwichSize enum captures the three available sandwich sizes along with
 * every price tier that depends on size.
 *
 * WHY EMBED PRICES IN THE ENUM?
 *   The spec defines exactly three sizes with fixed pricing tables.
 *   By attaching every price to its size constant we get a single source of
 *   truth: changing the 8" meat price means editing ONE line here, nowhere else.
 *   A separate PricingService or static map would spread this knowledge out and
 *   create extra coupling.
 *
 * FIELDS (all per-size prices from the requirements):
 *   basePrice    – flat cost for the bread/sandwich itself
 *   meatPrice    – cost per meat topping
 *   extraMeat    – surcharge for a "double" meat
 *   cheesePrice  – cost per cheese topping
 *   extraCheese  – surcharge for "extra" cheese
 */
public enum SandwichSize {

    FOUR_INCH  ("4\"",  5.50, 1.00, 0.50, 0.75, 0.30),
    EIGHT_INCH ("8\"",  7.00, 2.00, 1.00, 1.50, 0.60),
    TWELVE_INCH("12\"", 8.50, 3.00, 1.50, 2.25, 0.90);

    // ── Fields ────────────────────────────────────────────────────────────────
    private final String displayName;
    private final double basePrice;
    private final double meatPrice;
    private final double extraMeatPrice;
    private final double cheesePrice;
    private final double extraCheesePrice;

    // ── Constructor ───────────────────────────────────────────────────────────
    SandwichSize(String displayName,
                 double basePrice,
                 double meatPrice,
                 double extraMeatPrice,
                 double cheesePrice,
                 double extraCheesePrice) {
        this.displayName      = displayName;
        this.basePrice        = basePrice;
        this.meatPrice        = meatPrice;
        this.extraMeatPrice   = extraMeatPrice;
        this.cheesePrice      = cheesePrice;
        this.extraCheesePrice = extraCheesePrice;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public String getDisplayName()      { return displayName;      }
    public double getBasePrice()        { return basePrice;        }
    public double getMeatPrice()        { return meatPrice;        }
    public double getExtraMeatPrice()   { return extraMeatPrice;   }
    public double getCheesePrice()      { return cheesePrice;      }
    public double getExtraCheesePrice() { return extraCheesePrice; }

    @Override
    public String toString() { return displayName; }
}