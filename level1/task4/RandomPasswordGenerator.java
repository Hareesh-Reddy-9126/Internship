import java.security.SecureRandom;
import java.util.Scanner;

public class RandomPasswordGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter desired password length: ");
        int length = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Include lowercase letters? (y/n): ");
        boolean useLower = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include uppercase letters? (y/n): ");
        boolean useUpper = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include numbers? (y/n): ");
        boolean useNumbers = scanner.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Include special characters? (y/n): ");
        boolean useSpecial = scanner.nextLine().trim().equalsIgnoreCase("y");

        if (length <= 0) {
            System.out.println("Error: Password length must be greater than zero.");
            scanner.close();
            return;
        }

        StringBuilder pool = new StringBuilder();
        if (useLower) {
            pool.append("abcdefghijklmnopqrstuvwxyz");
        }
        if (useUpper) {
            pool.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        }
        if (useNumbers) {
            pool.append("0123456789");
        }
        if (useSpecial) {
            pool.append("!@#$%^&*()-_=+[]{};:,.<>?/\\|");
        }

        if (pool.length() == 0) {
            System.out.println("Error: Select at least one character type.");
            scanner.close();
            return;
        }

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(pool.length());
            password.append(pool.charAt(index));
        }

        System.out.println("Generated password: " + password);

        scanner.close();
    }
}
