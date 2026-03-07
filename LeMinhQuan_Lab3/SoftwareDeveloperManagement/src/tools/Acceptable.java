package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Acceptable {

    private static final Scanner sc = new Scanner(System.in);
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private static void checkRetry(int errCount) {
        if (errCount >= 3) {
            System.out.print("You failed 3 times. Do you want to return to menu? (Y/N): ");
            String ans = sc.nextLine().trim();
            if (ans.equalsIgnoreCase("Y")) {
                throw new ReturnToMenuException();
            }
        }
    }

    public static String checkString(String msg) {
        int errCount = 0;
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.err.println("Error: Input cannot be empty!");
                errCount++;
                checkRetry(errCount);
                if (errCount >= 3)
                    errCount = 0;
            } else {
                return input;
            }
        }
    }

    public static int checkInt(String msg, int min, int max) {
        int errCount = 0;
        while (true) {
            try {
                System.out.print(msg);
                String input = sc.nextLine().trim();
                int number = Integer.parseInt(input);
                if (number < min || number > max) {
                    System.err.println("Error: Number must be between " + min + " and " + max);
                    errCount++;
                    checkRetry(errCount);
                    if (errCount >= 3)
                        errCount = 0;
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid integer!");
                errCount++;
                checkRetry(errCount);
                if (errCount >= 3)
                    errCount = 0;
            }
        }
    }

    public static String checkRegex(String msg, String pattern, String err) {
        int errCount = 0;
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.matches(pattern)) {
                return input;
            } else {
                System.err.println(err);
                errCount++;
                checkRetry(errCount);
                if (errCount >= 3)
                    errCount = 0;
            }
        }
    }

    public static String checkDate(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        int errCount = 0;
        while (true) {
            try {
                System.out.print(msg + " (dd/MM/yyyy): ");
                String input = sc.nextLine().trim();
                sdf.parse(input);
                return input;
            } catch (ParseException e) {
                System.err.println("Error: Invalid date! Please use dd/MM/yyyy format.");
                errCount++;
                checkRetry(errCount);
                if (errCount >= 3)
                    errCount = 0;
            }
        }
    }

    public static String checkFutureDate(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        Date today = new Date();
        int errCount = 0;
        while (true) {
            try {
                System.out.print(msg + " (dd/MM/yyyy): ");
                String input = sc.nextLine().trim();
                Date parsed = sdf.parse(input);
                if (!parsed.after(today)) {
                    System.err.println("Error: Start date must be in the future!");
                    errCount++;
                    checkRetry(errCount);
                    if (errCount >= 3)
                        errCount = 0;
                } else {
                    return input;
                }
            } catch (ParseException e) {
                System.err.println("Error: Invalid date! Please use dd/MM/yyyy format.");
                errCount++;
                checkRetry(errCount);
                if (errCount >= 3)
                    errCount = 0;
            }
        }
    }

    public static boolean checkBoolean(String msg) {
        int errCount = 0;
        while (true) {
            System.out.print(msg + " (Y/N): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("Y")) {
                return true;
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            }
            System.err.println("Error: Please enter Y or N");
            errCount++;
            checkRetry(errCount);
            if (errCount >= 3)
                errCount = 0;
        }
    }

    public static String readOptionalLine() {
        return sc.nextLine().trim();
    }
}
