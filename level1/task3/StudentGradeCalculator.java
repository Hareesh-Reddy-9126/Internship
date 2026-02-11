import java.util.Scanner;

public class StudentGradeCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of grades: ");
        int count = scanner.nextInt();

        if (count <= 0) {
            System.out.println("Error: Number of grades must be greater than zero.");
            scanner.close();
            return;
        }

        double sum = 0.0;

        for (int i = 1; i <= count; i++) {
            System.out.print("Enter grade " + i + ": ");
            double grade = scanner.nextDouble();
            sum += grade;
        }

        double average = sum / count;
        System.out.printf("Average grade: %.2f%n", average);

        scanner.close();
    }
}
