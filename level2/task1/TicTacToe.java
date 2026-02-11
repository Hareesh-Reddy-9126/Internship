import java.util.Scanner;

public class TicTacToe {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
            char[][] board = new char[3][3];
            initBoard(board);

            char currentPlayer = 'X';
            int moves = 0;
            boolean gameOver = false;

            while (!gameOver) {
                printBoard(board);
                int row = readNumber(scanner, "Player " + currentPlayer + ", enter row (1-3): ") - 1;
                int col = readNumber(scanner, "Player " + currentPlayer + ", enter column (1-3): ") - 1;

                if (!isValidMove(board, row, col)) {
                    System.out.println("Invalid move. Try again.");
                    continue;
                }

                board[row][col] = currentPlayer;
                moves++;

                if (hasWinner(board, currentPlayer)) {
                    printBoard(board);
                    System.out.println("Player " + currentPlayer + " wins!");
                    gameOver = true;
                } else if (moves == 9) {
                    printBoard(board);
                    System.out.println("It's a draw!");
                    gameOver = true;
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                }
            }

            System.out.print("Play again? (y/n): ");
            String answer = scanner.next().trim();
            playAgain = answer.equalsIgnoreCase("y");
            scanner.nextLine();
        }

        scanner.close();
    }

    private static void initBoard(char[][] board) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                board[r][c] = ' ';
            }
        }
    }

    private static void printBoard(char[][] board) {
        System.out.println();
        for (int r = 0; r < board.length; r++) {
            System.out.print(" ");
            for (int c = 0; c < board[r].length; c++) {
                System.out.print(board[r][c]);
                if (c < board[r].length - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (r < board.length - 1) {
                System.out.println("---+---+---");
            }
        }
        System.out.println();
    }

    private static boolean isValidMove(char[][] board, int row, int col) {
        if (row < 0 || row >= 3 || col < 0 || col >= 3) {
            return false;
        }
        return board[row][col] == ' ';
    }

    private static boolean hasWinner(char[][] board, char player) {
        for (int r = 0; r < 3; r++) {
            if (board[r][0] == player && board[r][1] == player && board[r][2] == player) {
                return true;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (board[0][c] == player && board[1][c] == player && board[2][c] == player) {
                return true;
            }
        }

        if (board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }

        if (board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }

        return false;
    }

    private static int readNumber(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                return scanner.nextInt();
            }
            System.out.println("Please enter a number.");
            scanner.next();
        }
    }
}
