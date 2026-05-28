package com.pularsight.ENUMS;

/*
 * SandwichSize enum captures the three available sandwich sizes along with
 * every price tier that depends on size.
 *
 * i embed prices in enum cuz
 * the spec define exactly three sizes with fixed pricing tables
 * create extra coupling. its for the most part fixed values
 */
public enum SandwichSize {

    FOUR_INCH  ("4\"",  5.50, 1.00, 0.50, 0.75, 0.30),
    EIGHT_INCH ("8\"",  7.00, 2.00, 1.00, 1.50, 0.60),
    TWELVE_INCH("12\"", 8.50, 3.00, 1.50, 2.25, 0.90);

    //---- Fields --------------------------------------------------------------------------------
    private final String displayName;
    private final double basePrice;
    private final double meatPrice;
    private final double extraMeatPrice;
    private final double cheesePrice;
    private final double extraCheesePrice;

    //---- Constructor ----------------------------------------------------------------------
    SandwichSize(String displayName,
                 double basePrice,
                 double meatPrice,
                 double extraMeatPrice,
                 double cheesePrice,
                 double extraCheesePrice) {
        this.displayName      = displayName;
        this.basePrice        = basePrice;
        this.meatPrice        = meatPrice;
        this.extraMeatPrice   = extraMeatPrice;
        this.cheesePrice      = cheesePrice;
        this.extraCheesePrice = extraCheesePrice;
    }

    //---- Accessors ---------------------------------------------------------------------
    public String getDisplayName()      { return displayName;      }
    public double getBasePrice()        { return basePrice;        }
    public double getMeatPrice()        { return meatPrice;        }
    public double getExtraMeatPrice()   { return extraMeatPrice;   }
    public double getCheesePrice()      { return cheesePrice;      }
    public double getExtraCheesePrice() { return extraCheesePrice; }

    @Override
    public String toString() { return displayName; }
}