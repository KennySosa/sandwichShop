package com.pularsight.Interfaces;


public interface Orderable<T> {

    /*
     * returns the calculated price of this item.
     * implementations must compute this dynamically so that toppings
     *  added after construction are reflected immediately.
     */
    double getPrice();
    String getReceiptLine();
    T copy();
}