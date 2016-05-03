package com.peacockweb.billsplitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrew on 5/2/2016.
 */
public class PaymentSummary {
    String payer;
    String description;
    String date;
    List<String> recipients;
    double amount;
    Boolean isDebtPayment;

    PaymentSummary(String payer, String description, String date, String[] recipients, double amount, Boolean isDebtPayment) {
        this.payer = payer;
        this.description = description;
        this.date = date;
        this.recipients = Arrays.asList(recipients);
        this.amount = amount;
        this.isDebtPayment = isDebtPayment;
    }

    public String getPayerStatement() {
        String str = payer + " paid $" + amount;
        return str;
    }

    public String getRecipientsStatement() {
        String str = new String();
        if (isDebtPayment)
            str = "To: ";
        else
            str = "For: ";

        for (int i = 0; i < recipients.size(); i++) {
            if (i == 0)
                str += recipients.get(i);
            else
                str += ", " + recipients.get(i);
        }
        return str;
    }
}
