package com.byusluer.fitnessanalyticsapp1.util;

import lombok.experimental.UtilityClass;

import java.text.DecimalFormat;

@UtilityClass
public class NumberFormatter {

    private static final DecimalFormat TWO_DECIMAL = new DecimalFormat("#.##");

    public static String formatDouble(Double value) {
        return value == null ? "0.00" : TWO_DECIMAL.format(value);
    }
}
