import java.util.Scanner;

public class TemperatureConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the temperature value: ");
        double value = scanner.nextDouble();

        System.out.print("Enter the unit (C or F): ");
        String unitInput = scanner.next().trim();

        if (unitInput.length() != 1) {
            System.out.println("Error: Unit must be C or F.");
            scanner.close();
            return;
        }

        char unit = Character.toUpperCase(unitInput.charAt(0));

        if (unit == 'C') {
            double fahrenheit = (value * 9.0 / 5.0) + 32.0;
            System.out.printf("Converted temperature: %.2f F%n", fahrenheit);
        } else if (unit == 'F') {
            double celsius = (value - 32.0) * 5.0 / 9.0;
            System.out.printf("Converted temperature: %.2f C%n", celsius);
        } else {
            System.out.println("Error: Unit must be C or F.");
        }

        scanner.close();
    }
}
