import java.util.Scanner;

public class PasswordStrengthChecker {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        if (password == null || password.isEmpty()) {
            System.out.println("Error: Password cannot be empty.");
            scanner.close();
            return;
        }

        boolean hasMinLength = password.length() >= 8;
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        String specials = "!@#$%^&*()-_=+[]{};:'\"|\\,<.>/?`~";
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (!hasUpper && ch >= 'A' && ch <= 'Z') {
                hasUpper = true;
            } else if (!hasLower && ch >= 'a' && ch <= 'z') {
                hasLower = true;
            } else if (!hasDigit && ch >= '0' && ch <= '9') {
                hasDigit = true;
            } else if (!hasSpecial && specials.indexOf(ch) >= 0) {
                hasSpecial = true;
            }
        }

        int satisfied = 0;
        if (hasMinLength) satisfied++;
        if (hasUpper) satisfied++;
        if (hasLower) satisfied++;
        if (hasDigit) satisfied++;
        if (hasSpecial) satisfied++;

        String strength;
        if (satisfied <= 2) {
            strength = "Weak";
        } else if (satisfied <= 4) {
            strength = "Medium";
        } else {
            strength = "Strong";
        }

        System.out.println("Strength: " + strength + " (" + satisfied + "/5)");

        if (satisfied == 5) {
            System.out.println("All requirements satisfied.");
        } else {
            System.out.println("Missing requirements:");
            if (!hasMinLength) System.out.println("- Minimum length of 8 characters");
            if (!hasUpper) System.out.println("- At least one uppercase letter");
            if (!hasLower) System.out.println("- At least one lowercase letter");
            if (!hasDigit) System.out.println("- At least one digit");
            if (!hasSpecial) System.out.println("- At least one special character");
        }

        scanner.close();
    }
}
