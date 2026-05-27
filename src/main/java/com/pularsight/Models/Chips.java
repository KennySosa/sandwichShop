package com.pularsight.Models;


import com.pularsight.Interfaces.Orderable;


public class Chips implements Orderable<Chips> {

    //Fixed price as defined in the project requirements
    private static final double CHIPS_PRICE = 1.50;

    private final String type;  // e.g. "Regular", "BBQ", "Salt & Vinegar"

    public Chips(String type) {
        this.type = type;
    }

    private Chips(Chips source) {
        this.type = source.type;
    }

    //Orderable ------------------------------------------------------------------------

    @Override
    public double getPrice() { return CHIPS_PRICE; }

    @Override
    public String getReceiptLine() {
        return String.format("  Chips (%s) .................... $%.2f", type, getPrice());
    }

    @Override
    public Chips copy() { return new Chips(this); }

    // Accessors --------------------------------------------------------------------------
    public String getType() { return type; }

    @Override
    public String toString() {
        return String.format("%s Chips — $%.2f", type, getPrice());
    }
}