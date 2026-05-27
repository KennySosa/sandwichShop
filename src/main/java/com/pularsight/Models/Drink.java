package com.pularsight.Models;

import com.pularsight.ENUMS.DrinkSize;
import com.pularsight.Interfaces.Orderable;

/**
 * Drink represents a beverage added to an order.
 *
 * OOP: Implements Orderable<Drink> — allows Drink to live alongside Sandwich
 * and Chips in the same Order list without casting.
 *
 * DESIGN: Price is derived from DrinkSize (enum), so Drink itself has no
 * price field — it delegates to its size enum constant, keeping the pricing
 * table in one place (the enum).
 */
public class Drink implements Orderable<Drink> {

    private final DrinkSize size;
    private final String    flavor;  // customer-supplied free-text flavor

    public Drink(DrinkSize size, String flavor) {
        this.size   = size;
        this.flavor = flavor;
    }

    /** Copy constructor — flavor and size are immutable, so a shallow copy is fine. */
    private Drink(Drink source) {
        this.size   = source.size;
        this.flavor = source.flavor;
    }

    // ── Orderable ─────────────────────────────────────────────────────────────

    @Override
    public double getPrice() {
        return size.getPrice();  // delegates to DrinkSize enum — no magic numbers
    }

    @Override
    public String getReceiptLine() {
        return String.format("  Drink (%s %s) ................. $%.2f",
                size.getDisplayName(), flavor, getPrice());
    }

    @Override
    public Drink copy() {
        return new Drink(this);
    }

    // ── Accessors ─────────────────────────────────────────────────────────────
    public DrinkSize getSize()   { return size;   }
    public String    getFlavor() { return flavor; }

    @Override
    public String toString() {
        return String.format("%s %s — $%.2f", size.getDisplayName(), flavor, getPrice());
    }
}