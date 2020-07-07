package it.multidialogo.rest.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorUtils {

    public static void parseDotNotation(String errorMsg) {
        for (String s : errorMsg.split("\\.")) {
            String propertyName = s;
            int index = -1;
            if (isArrayField(s)) {
                propertyName = getFieldFromArrayName(s);
                index = getIndexFromArrayName(s);
            }
            System.out.print("Attributo: " + propertyName);
            if (index >= 0) {
                System.out.println("[" + index + "]");
            } else {
                System.out.println();
            }
        }
    }

    private static boolean isArrayField(String fieldName) {
        return fieldName.contains("[");
    }

    private static String getFieldFromArrayName(String fieldName) {
        return fieldName.substring(0, fieldName.indexOf("["));
    }

    private static int getIndexFromArrayName(String fieldName) {
        Pattern pattern = Pattern.compile("[\\d+]");
        Matcher matcher = pattern.matcher(fieldName);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group());
        }
        return -1;
    }
}
