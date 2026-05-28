package com.pularsight.Ui;


import com.pularsight.Interfaces.Orderable;
import com.pularsight.Models.Chips;
import com.pularsight.Models.Drink;
import com.pularsight.Models.Sandwich;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/*
 * order aggregates all items a customer wants to purchase.
 *
 *generics------------
 *   the internal list is typed as ListOrderable a list of any orderable
 *   regardless of the concrete type parameter this lets it store sandwich
 *   drink and chips in the same list without casting or duping add/remove
 *   methods. wildcard <?> is here because we only read from the
 *   list polymorphically ( getPrice / getReceiptLine);
 *
 *streams--------------
 *   getTotal() uses a single stream pipeline to sum every item's price.
 *   getReceiptText() uses stream + collect(joining) to build the full receipt
 *   body no StringBuilder boilerplate.
 *
 *   isValid() enforces the business rule: an order with 0 sandwiches must contain
 *   at least one drink or chips.  Keeping this logic here (not in the UI) means
 *   any future UI gets the same rule automatically.
 */
public class Order {

    // The combined list of all Orderable items regardless of their concrete type.
    private final List<Orderable<?>> items;

    /*
     * Timestamp is captured at Order creation (not at checkout) so that a
     * receipt filename accurately reflects when the order was started.
     */
    private final LocalDateTime orderTime;

    //---- Constructor ------------------------------------------------------------

    public Order() {
        this.items     = new ArrayList<>();
        this.orderTime = LocalDateTime.now();
    }

    //---- Item management ------------------------------------------------------


    //Generic add method — accepts any Orderable regardless of its type param.

    public void addItem(Orderable<?> item) {
        items.add(item);
    }

    // Removes an item by its position in the list
    public boolean removeItem(int index) {
        if (index < 0 || index >= items.size()) return false;
        items.remove(index);
        return true;
    }

    // Returns an unmodifiable view of all items so callers cannot mutate the list.
    public List<Orderable<?>> getItems() {
        return Collections.unmodifiableList(items);
    }

    //---- Filtering helpers ------------------------------------------------------------

    /*
     * Returns only the Sandwich items using streams + instanceof pattern.
     *
     * STREAMS: filter(instanceof) + map(cast) + collect is the standard Java
     * idiom for extracting a subtype from a heterogeneous list without manual
     * loops or unchecked warnings.
     */
    public List<Sandwich> getSandwiches() {
        return items.stream()
                .filter(i -> i instanceof Sandwich)
                .map(i -> (Sandwich) i)
                .collect(Collectors.toList());
    }

    public List<Drink> getDrinks() {
        return items.stream()
                .filter(i -> i instanceof Drink)
                .map(i -> (Drink) i)
                .collect(Collectors.toList());
    }

    public List<Chips> getChips() {
        return items.stream()
                .filter(i -> i instanceof Chips)
                .map(i -> (Chips) i)
                .collect(Collectors.toList());
    }

    //---- Business logic -----------------------------------------------------------

    /*
     * Validates the order per the business rule:
     *   - At least one item must be present.
     *   - If there are NO sandwiches, there must be at least one drink or chips.
     *
     * @return true if the order meets the minimum requirements
     */
    public boolean isValid() {
        if (items.isEmpty()) return false;
        boolean hasSandwich = items.stream().anyMatch(i -> i instanceof Sandwich);
        boolean hasDrinkOrChips = items.stream()
                .anyMatch(i -> i instanceof Drink || i instanceof Chips);
        return hasSandwich || hasDrinkOrChips;
    }

    /*
     * Calculates the order total.
     *
     * STREAMS: mapToDouble converts each Orderable to its price via the interface
     * method, then .sum() aggregates — entirely polymorphic, no type-checking needed.
     */
    public double getTotal() {
        return items.stream()
                .mapToDouble(Orderable::getPrice)
                .sum();
    }

    /*
     * Builds the full receipt text as a single String.
     *
     * STREAMS: map(getReceiptLine) + collect(joining) assembles the multi-line
     * receipt body cleanly.  The header and footer are concatenated via string
     * formatting rather than another stream, since they are structural (not per-item).
     */
    public String getReceiptText() {
        String header = String.format(
                "╔══════════════════════════════════════╗%n" +
                        "║          🥪 AROUND'A CORNER 🥪      ║%n" +
                        "║      Your Receipt — Thank You!       ║%n" +
                        "╠══════════════════════════════════════╣%n" +
                        "  Order Time: %s%n" +
                        "──────────────────────────────────────%n",
                orderTime.toString().replace("T", " ").substring(0, 19));

        String itemLines = items.stream()
                .map(Orderable::getReceiptLine)
                .collect(Collectors.joining("\n"));

        String footer = String.format(
                "%n──────────────────────────────────────%n" +
                        "  ORDER TOTAL: .................. $%.2f%n" +
                        "╚══════════════════════════════════════╝",
                getTotal());

        return header + itemLines + footer;
    }

    //---- Accessors --------------------------------------------------------------------

    public LocalDateTime getOrderTime() { return orderTime; }
    public boolean       isEmpty()      { return items.isEmpty(); }
    public int           getItemCount() { return items.size(); }
}