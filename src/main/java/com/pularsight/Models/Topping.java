package com.pularsight.Models;

import com.pularsight.ENUMS.SandwichSize;
import com.pularsight.ENUMS.ToppingType;

/**
 * Topping represents a single topping placed on a sandwich.
 *
 * It pairs a ToppingType (what the topping IS) with an isExtra flag
 * (whether the customer requested a double portion of a premium topping).
 *
 * WHY A SEPARATE CLASS instead of a plain ToppingType list?
 *   The "extra" concept only applies at order time — it is not inherent to
 *   the topping itself.  Keeping the flag here lets Sandwich.getPrice() stay
 *   clean: it iterates Topping objects and asks each one for its cost given
 *   the sandwich size, rather than maintaining two parallel lists (normal /
 *   extra) for every premium category.
 *
 * IMMUTABILITY:
 *   Fields are final after construction.  If a user wants to change "extra"
 *   they remove the topping and re-add it.  This avoids subtle bugs where a
 *   shared Topping reference is mutated after being added to a sandwich.
 */
public class Topping {

    private final ToppingType type;
    private final boolean     isExtra;  // true = double portion (premium toppings only)

    /** Standard constructor – used for regular (single) toppings. */
    public Topping(ToppingType type) {
        this(type, false);
    }

    /** Full constructor – used when the user requests an extra (double) serving. */
    public Topping(ToppingType type, boolean isExtra) {
        this.type    = type;
        this.isExtra = isExtra;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public ToppingType getType()    { return type;    }
    public boolean     isExtra()    { return isExtra; }

    /**
     * Calculates the cost of this topping for the given sandwich size.
     *
     * DESIGN: price logic lives here (not in Sandwich) so that adding a new
     * topping category (e.g. PREMIUM_SAUCE) only requires updating
     * this method and ToppingCategory — not Sandwich.getPrice().
     *
     * Regular toppings and sauces are always free (return 0.0).
     * Premium toppings charge the base price; "extra" adds a surcharge on top.
     *
     * @param size the SandwichSize that determines the price tier
     * @return the dollar cost of this topping
     */
    public double getPrice(SandwichSize size) {
        switch (type.getCategory()) {
            case MEAT:
                double meatBase  = size.getMeatPrice();
                double meatExtra = isExtra ? size.getExtraMeatPrice() : 0.0;
                return meatBase + meatExtra;

            case CHEESE:
                double cheeseBase  = size.getCheesePrice();
                double cheeseExtra = isExtra ? size.getExtraCheesePrice() : 0.0;
                return cheeseBase + cheeseExtra;

            case REGULAR:
            case SAUCE:
            default:
                return 0.0;   // free toppings — no charge
        }
    }

    /**
     * Returns a formatted summary for use on the receipt.
     * Example: "  + Bacon (extra)"
     */
    public String getReceiptLine() {
        String extra = isExtra ? " (extra)" : "";
        return "      + " + type.getDisplayName() + extra;
    }

    @Override
    public String toString() {
        return type.getDisplayName() + (isExtra ? " (extra)" : "");
    }
}