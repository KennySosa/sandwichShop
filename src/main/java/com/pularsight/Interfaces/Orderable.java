package com.pularsight.Interfaces;

/**
 * Orderable is the contract every item in an Order must satisfy.
 *
 * INTERFACE OVER ABSTRACT
 *   An interface defines a pure capability ("this thing can be ordered") without
 *   dictating implementation or forcing a single inheritance hierarchy.
 *   Sandwich, Drink, and Chips are very different objects; sharing an abstract
 *   class would either leave most methods empty or force artificial coupling.
 *   An interface lets each class implement only what it truly is.
 *
 * GENERICS (Orderable<T>)?
 *   The generic type parameter T allows callers to work with the concrete type
 *   when needed (e.g. in signature sandwich copy constructors) without unsafe
 *   casts, while still treating everything as Orderable<?> in the Order list.
 *
 * STREAMS CONNECTION:
 *   Because every cart item is Orderable, Order.getTotal() can call
 *       items.stream().mapToDouble(Orderable::getPrice).sum()
 *   without knowing whether it is summing sandwiches, drinks, or chips.
 */
public interface Orderable<T> {

    /**
     * Returns the calculated price of this item.
     * Implementations must compute this dynamically so that
     * toppings added after construction are reflected immediately.
     */
    double getPrice();

    /**
     * Returns a human-readable summary line for the receipt.
     * The format is left to each implementation.
     */
    String getReceiptLine();

    /**
     * Returns a deep copy of this item so an order snapshot is
     * immune to later mutations (e.g. editing a sandwich after adding it).
     */
    T copy();
}