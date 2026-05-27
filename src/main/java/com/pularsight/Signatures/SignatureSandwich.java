package com.pularsight.Signatures;

import com.pularsight.ENUMS.BreadType;
import com.pularsight.ENUMS.SandwichSize;
import com.pularsight.Models.Sandwich;

/*
 * SignatureSandwich represents a pre-configured "house" sandwich.
 *
 * inheritance and polymorph
 *   The only things it adds are:
 *- A signatureName field shown on the receipt
 *- An overridden getReceiptLine() that prepends the signature label
 *- An overridden copy() that returns a SignatureSandwich
 *
 *   SignatureSandwichFactory (in util) builds the predefined BLT and Philly Cheese
 *   Steak objects.  Keeping construction logic in a factory means this class stays
 *   focused on behavior, not configuration.
 */
public class SignatureSandwich extends Sandwich {

    private final String signatureName;

    /*
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

    /*
     * copy constructor for this subclass.
     * calls parent copy constructor (which deep-copies toppings) then
     * copies signatureNam
     */
    private SignatureSandwich(SignatureSandwich source) {
        super(source);  // deep-copy toppings via Sandwich(Sandwich source)
        this.signatureName = source.signatureName;
    }

    public String getSignatureName() { return signatureName; }

    /*
     * overrides Sandwich.getReceiptLine() to prepend the signature label
     * polymorp Order.printReceipt() calls getReceiptLine() on every
     * orderable without knowing whether it holds a plain Sandwich or a
     * SignatureSandwich — the right version is called automatically
     */
    @Override
    public String getReceiptLine() {
        // Prefix the parent receipt line with the signature name banner
        return "  [Signature: " + signatureName + "]\n" + super.getReceiptLine();
    }

    @Override
    public SignatureSandwich copy() {
        return new SignatureSandwich(this);
    }

    @Override
    public String toString() {
        return "[Signature: " + signatureName + "] " + super.toString();
    }
}