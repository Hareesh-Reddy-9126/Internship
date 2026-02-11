import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileEncryptionDecryption {

    private static final int SHIFT = 3;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Encryption");
        System.out.println("2. Decryption");
        System.out.print("Enter choice (1 or 2): ");

        int choice;
        if (scanner.hasNextInt()) {
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
        } else {
            System.out.println("Invalid input. Please enter 1 or 2.");
            scanner.close();
            return;
        }

        if (choice != 1 && choice != 2) {
            System.out.println("Invalid choice. Please enter 1 for Encryption or 2 for Decryption.");
            scanner.close();
            return;
        }

        System.out.print("Enter input file path: ");
        String inputPath = scanner.nextLine().trim();

        System.out.print("Enter output file path: ");
        String outputPath = scanner.nextLine().trim();

        try {
            processFile(inputPath, outputPath, choice == 1);
            if (choice == 1) {
                System.out.println("Encryption completed successfully.");
            } else {
                System.out.println("Decryption completed successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        scanner.close();
    }

    private static void processFile(String inputPath, String outputPath, boolean encrypt) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String processed = encrypt ? encrypt(line) : decrypt(line);
                writer.write(processed);
                writer.newLine();
            }
        }
    }

    public static String encrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            char shifted = (char) (ch + SHIFT);
            sb.append(shifted);
        }
        return sb.toString();
    }

    public static String decrypt(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            char shifted = (char) (ch - SHIFT);
            sb.append(shifted);
        }
        return sb.toString();
    }
}
