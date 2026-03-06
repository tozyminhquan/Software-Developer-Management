package tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

// Lop chua cac phuong thuc ho tro nhap lieu tu ban phim
public class Acceptable {

    private static final Scanner sc = new Scanner(System.in);
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    // Nhap chuoi khong duoc de trong
    public static String checkString(String msg) {
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.err.println("Error: Input cannot be empty!");
            } else {
                return input;
            }
        }
    }

    // Nhap so nguyen trong khoang [min, max]
    public static int checkInt(String msg, int min, int max) {
        while (true) {
            try {
                System.out.print(msg);
                String input = sc.nextLine().trim();
                int number = Integer.parseInt(input);
                if (number < min || number > max) {
                    System.err.println("Error: Number must be between " + min + " and " + max);
                } else {
                    return number;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Please enter a valid integer!");
            }
        }
    }

    // Nhap chuoi phai khop voi regex
    public static String checkRegex(String msg, String pattern, String err) {
        while (true) {
            System.out.print(msg);
            String input = sc.nextLine().trim().toUpperCase();
            if (input.matches(pattern)) {
                return input;
            } else {
                System.err.println(err);
            }
        }
    }

    // Nhap ngay theo dinh dang dd/MM/yyyy
    public static String checkDate(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        while (true) {
            try {
                System.out.print(msg + " (dd/MM/yyyy): ");
                String input = sc.nextLine().trim();
                sdf.parse(input);
                return input;
            } catch (ParseException e) {
                System.err.println("Error: Invalid date! Please use dd/MM/yyyy format.");
            }
        }
    }

    // Nhap ngay phai o tuong lai (sau ngay hom nay)
    public static String checkFutureDate(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        Date today = new Date();
        while (true) {
            try {
                System.out.print(msg + " (dd/MM/yyyy): ");
                String input = sc.nextLine().trim();
                Date parsed = sdf.parse(input);
                if (!parsed.after(today)) {
                    System.err.println("Error: Start date must be in the future!");
                } else {
                    return input;
                }
            } catch (ParseException e) {
                System.err.println("Error: Invalid date! Please use dd/MM/yyyy format.");
            }
        }
    }

    // Chuyen String thanh Date
    public static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    // Hoi nguoi dung Y hoac N
    public static boolean checkBoolean(String msg) {
        while (true) {
            System.out.print(msg + " (Y/N): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("Y")) {
                return true;
            } else if (input.equalsIgnoreCase("N")) {
                return false;
            }
            System.err.println("Error: Please enter Y or N");
        }
    }

    // Doc mot dong tuy chon (co the bo trong de giu gia tri cu)
    public static String readOptionalLine() {
        return sc.nextLine().trim();
    }
}
