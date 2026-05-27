package com.pularsight.Ui;

import com.pularsight.ENUMS.*;
import com.pularsight.Models.Chips;
import com.pularsight.Models.Drink;
import com.pularsight.Models.Sandwich;
import com.pularsight.Models.Topping;
import com.pularsight.Signatures.SignatureSandwich;
import com.pularsight.Signatures.SignatureSandwichFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
 * UserInterface handles all console input/output for program.
 *
 * DESIGN PRINCIPLES:
 * this class only handles I/O and delegates all
 * business logic (pricing, validation, receipt writing) to the model layer.
 * shared input helpers (promptInt, promptYesNo, displayMenu) are
 * extracted into private methods so no screen-interaction code is duped.
 * every menu option is compared to a named constant or
 * clearly labeled inline so the code is self-documenting.
 *
 * SCANNER:
 *   One Scanner wraps System.in for when program runs for the rest of user duration
 * i had too many Opening/closing a Scanner per method that cause "stream closed" errors
 * so i only used one because closing a Scanner also closes the underlying stream.
 */
public class UserInterface {

    //---- Constants ---------------------------------------------------------------
    private static final String DIVIDER = "──────────────────────────────────────";
    private static final String HEADER  =
            "╔══════════════════════════════════════╗\n" +
                    "║         🥪 AROUND'A CORNER  🥪      ║\n" +
                    "╚══════════════════════════════════════╝";

    //--- Fields -------------------------------------------------------------------
    private final Scanner scanner;
    private       Order   currentOrder;

    //---- Constructor -----------------------------------------------------------
    public UserInterface() {
        this.scanner      = new Scanner(System.in);
        this.currentOrder = new Order();
    }

    //  PUBLIC ENTRY POINT
    /*
     * Starts the application loop.
     * Called once from Main; loops until the user exits.
     */
    public void start() {
        System.out.println("\n" + HEADER);
        System.out.println("  Welcome to DELI-cious — freshest subs in town!\n");

        boolean running = true;
        while (running) {
            showHomeMenu();
            int choice = promptInt("Enter choice: ", 1, 2);
            switch (choice) {
                case 1 -> runOrderFlow();
                case 2 -> {
                    System.out.println("\n  Thanks for visiting DELI-cious! See you next time! 🥪\n");
                    running = false;
                }
            }
        }
        scanner.close();
    }
    //  HOME MENU
    //Displays the two top-level choices: new order or exit.
    private void showHomeMenu() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  HOME MENU");
        System.out.println(DIVIDER);
        System.out.println("  1) New Order");
        System.out.println("  2) Exit");
        System.out.println(DIVIDER);
    }
    //  ORDER FLOW
    /*
     * Main order loop — the customer keeps adding items until they check out
     * or cancel.  A fresh Order is created at the start of each call so
     * previous orders do not bleed into the new one.
     */
    private void runOrderFlow() {
        currentOrder = new Order();  // reset for each new order
        boolean ordering = true;

        while (ordering) {
            showOrderMenu();
            int choice = promptInt("Enter choice: ", 1, 5);
            switch (choice) {
                case 1 -> addSandwich();
                case 2 -> addDrink();
                case 3 -> addChips();
                case 4 -> checkout();
                case 5 -> {
                    if (confirmCancel()) ordering = false;
                }
            }
            // After checkout the order is done — exit the loop
            if (choice == 4 && !currentOrder.isEmpty()) ordering = false;
        }
    }

    // Displays the order-in-progress menu with the current cart total.
    private void showOrderMenu() {
        System.out.println("\n" + DIVIDER);
        System.out.printf("  ORDER MENU  (Cart: %d item(s) — $%.2f)%n",
                currentOrder.getItemCount(), currentOrder.getTotal());
        System.out.println(DIVIDER);
        System.out.println("  1) Add Sandwich");
        System.out.println("  2) Add Drink");
        System.out.println("  3) Add Chips");
        System.out.println("  4) Checkout");
        System.out.println("  5) Cancel Order");
        System.out.println(DIVIDER);
    }

    //  ADD SANDWICH
    /*
     * Full sandwich builder flow.
     * First asks if the customer wants a signature or custom sandwich,
     * then walks through size → bread → toppings → toast.
     */
    private void addSandwich() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  BUILD YOUR SANDWICH");
        System.out.println(DIVIDER);
        System.out.println("  1) Custom Sandwich");
        System.out.println("  2) Signature Sandwich");
        System.out.println(DIVIDER);
        int choice = promptInt("Enter choice: ", 1, 2);

        Sandwich sandwich;
        if (choice == 2) {
            sandwich = chooseSignatureSandwich();
            if (sandwich == null) return;  // user backed out
            System.out.println("\n  ✔ Loaded: " + sandwich);
            if (promptYesNo("  Customize this sandwich? (y/n): ")) {
                customizeSandwich(sandwich);
            }
        } else {
            sandwich = buildCustomSandwich();
            if (sandwich == null) return;
        }

        currentOrder.addItem(sandwich);
        System.out.printf("%n  ✔ Sandwich added! (Subtotal: $%.2f)%n", sandwich.getPrice());
    }

    /*
     * Lets the user pick a signature sandwich from the factory list.
     *
     * STREAMS: IntStream.rangeClosed builds a numbered display without a
     * manual index counter variable.
     *
     * @return the chosen SignatureSandwich, or null if the user cancels
     */
    private SignatureSandwich chooseSignatureSandwich() {
        List<SignatureSandwich> signatures = SignatureSandwichFactory.getAllSignatures();

        System.out.println("\n  SIGNATURE SANDWICHES:");
        // STREAMS: rangeClosed + forEach prints "1) Name — $price" for each signature
        IntStream.rangeClosed(1, signatures.size()).forEach(i -> {
            SignatureSandwich s = signatures.get(i - 1);
            System.out.printf("  %d) %-22s $%.2f%n",
                    i, s.getSignatureName(), s.getPrice());
        });
        System.out.println("  " + signatures.size() + 1 + ") Back");

        int choice = promptInt("Enter choice: ", 1, signatures.size() + 1);
        if (choice == signatures.size() + 1) return null;

        // Return a copy so the factory's template is never mutated
        return signatures.get(choice - 1).copy();
    }

    /*
     * Guides the user through building a sandwich from scratch:
     * size → bread → toppings → toast.
     *
     * @return the configured Sandwich, or null if the user provides invalid input
     */
    private Sandwich buildCustomSandwich() {
        //---- Step 1: Size ----------------------------------------------------
        SandwichSize size = chooseSandwichSize();

        //---- Step 2: Bread ---------------------------------------------------
        BreadType bread = chooseBreadType();

        //---- Step 3: Construct the sandwich and add toppings -----------------
        Sandwich sandwich = new Sandwich(size, bread);
        customizeSandwich(sandwich);

        return sandwich;
    }

    /*
     * Handles the full topping customization loop for any Sandwich.
     * Extracted so both custom and signature sandwiches reuse the same flow.
     * This is a key DRY win — no duplicated topping-selection code.
     *
     * @param sandwich the sandwich to add toppings to (mutated in place)
     */
    private void customizeSandwich(Sandwich sandwich) {
        //==== Meats =============================================================
        addToppingsByCategory(sandwich, ToppingCategory.MEAT,
                "MEATS (premium)", sandwich.getSize());

        //==== Cheeses ============================================================
        addToppingsByCategory(sandwich, ToppingCategory.CHEESE,
                "CHEESES (premium)", sandwich.getSize());

        //==== Regular toppings (free)============================================
        addToppingsByCategory(sandwich, ToppingCategory.REGULAR,
                "REGULAR TOPPINGS (free)", sandwich.getSize());

        //==== Sauces (free)======================================================
        addToppingsByCategory(sandwich, ToppingCategory.SAUCE,
                "SAUCES (free)", sandwich.getSize());

        //==== Toast =============================================================
        boolean toasted = promptYesNo("\n  Would you like it toasted? (y/n): ");
        sandwich.setToasted(toasted);
    }

    /*
     * Displays all toppings of a given category and lets the user pick any number.
     * Uses streams to filter ToppingType values by category — avoiding a separate
     * hardcoded list for each category.
     * ---------------STREAMS HERE-------------------------------
     *   Arrays.stream(ToppingType.values()).filter(...) dynamically generates
     *   the topping list from the enum itself.  If a new topping is added to
     *   the enum, it automatically appears here with no UI changes needed.
     * @param sandwich the sandwich being built
     * @param category the category to display
     * @param label    human-readable section header
     * @param size     used to display the per-topping price
     */
    private void addToppingsByCategory(Sandwich sandwich,
                                       ToppingCategory category,
                                       String label,
                                       SandwichSize size) {
        // STREAMS: filter enum values to only those matching the requested category
        List<ToppingType> options = Arrays.stream(ToppingType.values())
                .filter(t -> t.getCategory() == category)
                .collect(Collectors.toList());

        System.out.println("\n  " + label + ":");
        for (int i = 0; i < options.size(); i++) {
            ToppingType t = options.get(i);
            // Show price for premium toppings, "free" for regular/sauce
            String priceTag = t.isPremium()
                    ? String.format("$%.2f", new Topping(t).getPrice(size))
                    : "free";
            System.out.printf("  %2d) %-20s %s%n", i + 1, t.getDisplayName(), priceTag);
        }
        System.out.printf("  %2d) Done with %s%n", options.size() + 1, label.split(" ")[0]);

        // Keep prompting until "Done" is chosen
        while (true) {
            int choice = promptInt("  Pick topping (or done): ", 1, options.size() + 1);
            if (choice == options.size() + 1) break;

            ToppingType chosen = options.get(choice - 1);
            boolean extra = false;

            // Only offer "extra" for premium toppings (meats and cheeses)
            if (chosen.isPremium()) {
                extra = promptYesNo("  Add extra " + chosen.getDisplayName() + "? (y/n): ");
            }

            sandwich.addTopping(new Topping(chosen, extra));
            System.out.println("  ✔ Added: " + chosen.getDisplayName() + (extra ? " (extra)" : ""));
        }
    }

    //==== Size & Bread pickers =======================================================

    /*
     * Displays sandwich sizes with their base prices and returns the chosen size.
     * STREAMS: IntStream.range builds the numbered list without a manual counter.
     */
    private SandwichSize chooseSandwichSize() {
        SandwichSize[] sizes = SandwichSize.values();
        System.out.println("\n  SANDWICH SIZE:");
        IntStream.range(0, sizes.length).forEach(i ->
                System.out.printf("  %d) %-10s $%.2f%n",
                        i + 1, sizes[i].getDisplayName(), sizes[i].getBasePrice()));
        int choice = promptInt("  Enter choice: ", 1, sizes.length);
        return sizes[choice - 1];
    }

    //Displays bread types and returns the chosen BreadType.
    private BreadType chooseBreadType() {
        BreadType[] breads = BreadType.values();
        System.out.println("\n  BREAD TYPE:");
        IntStream.range(0, breads.length).forEach(i ->
                System.out.printf("  %d) %s%n", i + 1, breads[i].getDisplayName()));
        int choice = promptInt("  Enter choice: ", 1, breads.length);
        return breads[choice - 1];
    }


    //----ADD DRINK--------------
    //Guides the user through size → flavor for a drink.
    private void addDrink() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  ADD A DRINK");
        System.out.println(DIVIDER);

        //==== Size ================================================================
        DrinkSize[] sizes = DrinkSize.values();
        System.out.println("  SIZE:");
        IntStream.range(0, sizes.length).forEach(i ->
                System.out.printf("  %d) %-10s $%.2f%n",
                        i + 1, sizes[i].getDisplayName(), sizes[i].getPrice()));
        int sizeChoice = promptInt("  Enter choice: ", 1, sizes.length);
        DrinkSize size = sizes[sizeChoice - 1];

        // ==== Flavor ==============================================================
        System.out.print("  Enter flavor (e.g. Cola, Lemonade): ");
        String flavor = scanner.nextLine().trim();
        if (flavor.isEmpty()) flavor = "Fountain Drink";

        currentOrder.addItem(new Drink(size, flavor));
        System.out.printf("%n  ✔ %s %s added! ($%.2f)%n",
                size.getDisplayName(), flavor, size.getPrice());
    }
    //----ADD CHIPS----------------------
    //Prompts for a chips flavor and adds chips to the order.
    private void addChips() {
        System.out.println("\n" + DIVIDER);
        System.out.println("  ADD CHIPS  ($1.50)");
        System.out.println(DIVIDER);

        String[] flavors = {"Regular", "BBQ", "Salt & Vinegar", "Sour Cream & Onion", "Jalapeño"};
        System.out.println("  FLAVOR:");
        IntStream.range(0, flavors.length).forEach(i ->
                System.out.printf("  %d) %s%n", i + 1, flavors[i]));
        int choice = promptInt("  Enter choice: ", 1, flavors.length);

        currentOrder.addItem(new Chips(flavors[choice - 1]));
        System.out.printf("%n  ✔ %s Chips added! ($1.50)%n", flavors[choice - 1]);
    }
    //  CHECKOUT
    /*
     * Handles the checkout flow:
     *   1. Validates the order (must have at least one item).
     *   2. Prints the receipt to the console.
     *   3. Asks for confirmation.
     *   4. Saves the receipt file via ReceiptWriter.
     */
    private void checkout() {
        if (currentOrder.isEmpty()) {
            System.out.println("\n  ⚠  Your order is empty! Add at least one item first.");
            return;
        }

        //==== Print the receipt preview ============================================
        System.out.println("\n" + currentOrder.getReceiptText());

        if (!promptYesNo("\n  Confirm order? (y/n): ")) {
            System.out.println("  Order not confirmed. Returning to order menu.");
            return;
        }

        //==== Save the receipt =====================================================
        try {
            String path = ReceiptWriter.saveReceipt(currentOrder);
            System.out.println("\n  ✔ Receipt saved to: " + path);
            System.out.println("  Thank you for your order! See you next time! 🥪");
        } catch (IOException e) {
            // Inform the user but do NOT crash — the order was still valid
            System.out.println("  ⚠  Could not save receipt file: " + e.getMessage());
            System.out.println("  Your order has been processed — enjoy your meal!");
        }
    }
    //  CANCEL
    /*
     * Asks the user to confirm before discarding the current order.
     * Prevents accidental cancellation if the order already has items.
     *
     * @return true if the user confirms the cancellation
     */
    private boolean confirmCancel() {
        if (currentOrder.isEmpty()) return true;  // nothing to lose — cancel immediately
        System.out.printf("%n  Your order has %d item(s) totalling $%.2f.%n",
                currentOrder.getItemCount(), currentOrder.getTotal());
        return promptYesNo("  Are you sure you want to cancel? (y/n): ");
    }
    //  SHARED INPUT HELPERS
    /*
     * Prompts the user for an integer within [min, max] (inclusive).
     * Loops until valid input is received — never crashes on bad input
     * @param prompt the message to display before waiting for input
     * @param min    minimum acceptable value (inclusive)
     * @param max    maximum acceptable value (inclusive)
     * @return a validated integer in [min, max]
     */
    private int promptInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
                System.out.printf("  ⚠  Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Invalid input — please enter a number.");
            }
        }
    }

    /*
     * Prompts the user for a yes/no answer.
     * Accepts 'y', 'Y', 'n', 'N' — loops on anything else.
     *
     * @param prompt the question to display
     * @return true for 'y'/'Y', false for 'n'/'N'
     */
    private boolean promptYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("  ⚠  Please enter 'y' or 'n'.");
        }
    }
}