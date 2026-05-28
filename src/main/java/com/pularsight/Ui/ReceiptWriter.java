package com.pularsight.Ui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;

/*
 * ReceiptWriter handles all file I/O for saving order receipts.
 *
 * FILE
 *   Receipts are saved to the "receipts/" folder in my pc
 */
public class ReceiptWriter {

    // Folder where all receipts will be saved
    private static final String RECEIPTS_FOLDER = "receipts";

    // Date-time format required by the project spec.
    private static final DateTimeFormatter FILE_NAME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    // Prevent instantiation.
    private ReceiptWriter() {}

    /*
     * Saves the receipt for the given order to disk.
     * STEPS:
     * 1. Ensure the receipts/ directory exists (creates it if not).
     * 2. Build the filename from the order's timestamp.
     * 3. Write the receipt text produced by Order.getReceiptText().
     *
     *PrintWriter>BufferedWriter
     *   PrintWriter.println() handles platform line endings automatically,
     *   and it wraps FileWriter cleanly with try-with-resources.
     * @param order the completed order to save
     * @return the path of the file that was written, for UI confirmation
     * @throws IOException if the file cannot be created or written
     */
    public static String saveReceipt(Order order) throws IOException {
        //----Step 1: Ensure the receipts folder exists -----------------------------------
        File folder = new File(RECEIPTS_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();  // mkdirs() also creates any missing parent directories
        }

        //----Step 2: Build the timestamped filename ------------------------------------
        String fileName = order.getOrderTime().format(FILE_NAME_FORMATTER) + ".txt";
        File   file     = new File(folder, fileName);

        //----Step 3: Write the receipt text ------------------------------------------
        // try-with-resources guarantees the writer is closed even if an exception occurs
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println(order.getReceiptText());
        }

        return file.getPath();
    }
}