package com.pularsight.Signatures;

import com.pularsight.ENUMS.BreadType;
import com.pularsight.ENUMS.SandwichSize;
import com.pularsight.ENUMS.ToppingType;
import com.pularsight.Models.Topping;

import java.util.Arrays;
import java.util.List;

/*
 * SignatureSandwichFactory creates the predefined signature sandwiches
 *
 * factory:
 *The factory pattern separates construction logic from the SignatureSandwich
 *class itself.  If a new signature sandwich is added or prices change, only
 *this class needs to be edited — SignatureSandwich, Order, and UI are untouched.
 * using static factory methods cuz:
 *Each method returns a fresh SignatureSandwich instance so that every call
 *gets its own independent object. The customer can then customize (add/remove
 *toppings) without affecting future orders of the same signature
 * generics:
 *getAllSignatures() returns List<SignatureSandwich> (not List<Sandwich>),
 *so the UI can display the signatureName without castin
 */
public class SignatureSandwichFactory {

    // Prevent instantiation  this is a utility class.
    private SignatureSandwichFactory() {}

    //---- Signature Definitions --------------------------------------------------

    /*
     * BLT: 8" White bread, Bacon, Cheddar, Lettuce, Tomato, Ranch — toasted.
     * Pre-configured per the project spec bonus section.
     */
    public static SignatureSandwich createBLT() {
        SignatureSandwich blt = new SignatureSandwich("BLT",
                SandwichSize.EIGHT_INCH,
                BreadType.WHITE);
        blt.addTopping(new Topping(ToppingType.BACON));
        blt.addTopping(new Topping(ToppingType.CHEDDAR));
        blt.addTopping(new Topping(ToppingType.LETTUCE));
        blt.addTopping(new Topping(ToppingType.TOMATOES));
        blt.addTopping(new Topping(ToppingType.RANCH));
        blt.setToasted(true);
        return blt;
    }

    /*
     * Philly Cheese Steak: 8" White bread, Steak, American Cheese,
     * Peppers, Mayo — toasted.
     */
    public static SignatureSandwich createPhillyCheesesteak() {
        SignatureSandwich philly = new SignatureSandwich("Philly Cheese Steak",
                SandwichSize.EIGHT_INCH,
                BreadType.WHITE);
        philly.addTopping(new Topping(ToppingType.STEAK));
        philly.addTopping(new Topping(ToppingType.AMERICAN));
        philly.addTopping(new Topping(ToppingType.PEPPERS));
        philly.addTopping(new Topping(ToppingType.MAYO));
        philly.setToasted(true);
        return philly;
    }

    /*
     * Returns all available signature sandwiches as a list.
     *
     * using Arrays.asList is here cuz the catalogue is fixed at runtime
     * If the list needed to grow i can just use new ArrayList<>(Arrays.asList(...)).
     */
    public static List<SignatureSandwich> getAllSignatures() {
        return Arrays.asList(
                createBLT(),
                createPhillyCheesesteak()
        );
    }
}
