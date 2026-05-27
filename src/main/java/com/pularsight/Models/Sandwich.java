package com.pularsight.Models;

import com.pularsight.ENUMS.BreadType;
import com.pularsight.ENUMS.SandwichSize;
import com.pularsight.ENUMS.ToppingCategory;
import com.pularsight.ENUMS.ToppingType;
import com.pularsight.Interfaces.Orderable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Sandwich implements Orderable<Sandwich> {

    //Core identity fields ----------------------------------------------------------------
    private final SandwichSize size;
    private final BreadType breadType;
    private       boolean      isToasted;


     //Toppings are stored in a single list of Topping objects.

    private final List<Topping> toppings;

    //Constructor----------------------------------------------------------------------------
    public Sandwich(SandwichSize size, BreadType breadType) {
        this.size      = size;
        this.breadType = breadType;
        this.isToasted = false;
        this.toppings  = new ArrayList<>();
    }

    /*
     *copy constructor – used by copy() and SignatureSandwich.
     * creates  copy a new topping list with the same Topping objects
     * gtopping is immutable, so shallow copies of each element are safe).
     */
    protected Sandwich(Sandwich source) {
        this.size      = source.size;
        this.breadType = source.breadType;
        this.isToasted = source.isToasted;
        this.toppings  = new ArrayList<>(source.toppings); // new list, same immutable elements
    }

    //Topping management -------------------------------------------------------------------------

    // Adds any topping to this sandwich
    public void addTopping(Topping topping) {
        toppings.add(topping);
    }

    // Removes a topping by type (removes first match). Returns true if found.
    public boolean removeTopping(ToppingType type) {
        return toppings.removeIf(t -> t.getType() == type);
    }

    // Returns a read-only view of all toppings
    public List<Topping> getToppings() {
        return List.copyOf(toppings);  // defensive copy — callers cannot mutate the internal list
    }

    /*
     * Returns only the toppings matching a given category.
     *
     * STREAMS- filter() + collect() replaces a manual for-loop + addToList pattern.
     * This is cleaner and easier to read for a single-predicate filter.
     */
    public List<Topping> getToppingsByCategory(ToppingCategory category) {
        return toppings.stream()
                .filter(t -> t.getType().getCategory() == category)
                .collect(Collectors.toList());
    }

    // Pricing ------------------------------------------------------------------

    /*
     * Calculates the total price of this sandwich.
     *
     * STREAMS- mapToDouble(t -> t.getPrice(size)) converts each Topping to its
     * dollar cost for our size, then .sum() aggregates — one concise pipeline
     * instead of an accumulator variable and a for-loop.
     *
     * Base price comes from the SandwichSize enum
     *
     * return total price in dollars
     */
    @Override
    public double getPrice() {
        double toppingCost = toppings.stream()
                .mapToDouble(t -> t.getPrice(size))
                .sum();
        return size.getBasePrice() + toppingCost;
    }

    //Orderable interface ------------------------------------------------

    /*
     * Produces a multi-line receipt block for this sandwich.
     *
     * STREAMS- joining() collector builds the topping lines from the list
     * without a StringBuilder loop.
     */
    @Override
    public String getReceiptLine() {
        String toastedNote = isToasted ? " [TOASTED]" : "";

        // Build a block: header line + one line per topping
        String toppingLines = toppings.isEmpty()
                ? "      (no toppings)"
                : toppings.stream()
                .map(Topping::getReceiptLine)
                .collect(Collectors.joining("\n"));

        return String.format("  Sandwich (%s, %s%s) .............. $%.2f%n%s",
                size.getDisplayName(),
                breadType.getDisplayName(),
                toastedNote,
                getPrice(),
                toppingLines);
    }

    // Returns a deep copy of this sandwich
    @Override
    public Sandwich copy() {
        return new Sandwich(this);
    }

    //Standard accessors ------------------------------------------------------------
    public SandwichSize getSize()      { return size;      }
    public BreadType    getBreadType() { return breadType; }
    public boolean      isToasted()    { return isToasted; }
    public void         setToasted(boolean toasted) { this.isToasted = toasted; }

    @Override
    public String toString() {
        return String.format("%s %s sandwich%s — $%.2f",
                size.getDisplayName(),
                breadType.getDisplayName(),
                isToasted ? " (toasted)" : "",
                getPrice());
    }
}