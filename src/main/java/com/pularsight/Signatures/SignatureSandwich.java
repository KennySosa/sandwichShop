package com.pularsight.Signatures;

import com.pularsight.ENUMS.BreadType;
import com.pularsight.ENUMS.SandwichSize;
import com.pularsight.Models.Sandwich;

/**
 * SignatureSandwich represents a pre-configured "house" sandwich.
 *
 * OOP CONCEPTS: INHERITANCE & POLYMORPHISM
 *   SignatureSandwich EXTENDS Sandwich rather than containing one.
 *   This means it IS-A Sandwich — it inherits all topping management,
 *   pricing logic, and the Orderable contract without duplicating a single line.
 *
 *   The only things it adds are:
 *     1. A signatureName field shown on the receipt.
 *     2. An overridden getReceiptLine() that prepends the signature label.
 *     3. An overridden copy() that returns a SignatureSandwich (preserving the name).
 *
 * BONUS REQUIREMENT:
 *   The spec says customers can customize a signature sandwich by adding/removing
 *   toppings.  Because SignatureSandwich inherits addTopping() / removeTopping()
 *   from Sandwich, no extra code is needed — customization works automatically.
 *
 * HOW SIGNATURES ARE CREATED:
 *   SignatureSandwichFactory (in util) builds the predefined BLT and Philly Cheese
 *   Steak objects.  Keeping construction logic in a factory means this class stays
 *   focused on behavior, not configuration.
 */
public class SignatureSandwich extends Sandwich {

    private final String signatureName;

    /**
     * Constructs a signature sandwich by delegating base-sandwich setup to
     * the parent constructor and then storing the signature name.
     *
     * @param signatureName the marketing name (e.g. "BLT")
     * @param size          inherited from Sandwich
     * @param breadType     inherited from Sandwich
     */
    public SignatureSandwich(String signatureName, SandwichSize size, BreadType breadType) {
        super(size, breadType);  // call parent constructor — DRY: no field duplication
        this.signatureName = signatureName;
    }

    /**
     * Copy constructor for this subclass.
     * Calls the parent copy constructor (which deep-copies toppings) then
     * copies the signatureName field.
     */
    private SignatureSandwich(SignatureSandwich source) {
        super(source);  // deep-copy toppings via Sandwich(Sandwich source)
        this.signatureName = source.signatureName;
    }

    public String getSignatureName() { return signatureName; }

    /**
     * Overrides Sandwich.getReceiptLine() to prepend the signature label.
     * POLYMORPHISM: Order.printReceipt() calls getReceiptLine() on every
     * Orderable without knowing whether it holds a plain Sandwich or a
     * SignatureSandwich — the right version is called automatically.
     */
    @Override
    public String getReceiptLine() {
        // Prefix the parent receipt line with the signature name banner
        return "  [Signature: " + signatureName + "]\n" + super.getReceiptLine();
    }

    /**
     * Returns a deep copy typed as SignatureSandwich (not just Sandwich).
     * Overriding copy() in the subclass ensures the Order snapshot keeps
     * the signatureName even after the customer customizes the sandwich.
     */
    @Override
    public SignatureSandwich copy() {
        return new SignatureSandwich(this);
    }

    @Override
    public String toString() {
        return "[Signature: " + signatureName + "] " + super.toString();
    }
}