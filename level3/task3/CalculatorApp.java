import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorApp extends JFrame implements ActionListener {
    private final JTextField display;
    private double storedValue = 0.0;
    private String pendingOp = null;
    private boolean startNewNumber = true; // when true, next digit replaces display

    public CalculatorApp() {
        super("Calculator");
        display = new JTextField();
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setFont(new Font("SansSerif", Font.BOLD, 24));
        display.setText("0");

        JPanel buttonPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        String[] labels = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };
        for (String lbl : labels) {
            JButton btn = new JButton(lbl);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 18));
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        setLayout(new BorderLayout(5, 5));
        add(display, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 400);
        setLocationRelativeTo(null); // center on screen
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (isDigit(cmd)) {
            handleDigit(cmd);
        } else if (isOperator(cmd)) {
            handleOperator(cmd);
        } else if ("C".equals(cmd)) {
            clearAll();
        } else if ("=".equals(cmd)) {
            computeResult();
        }
    }

    private boolean isDigit(String cmd) {
        return cmd.length() == 1 && Character.isDigit(cmd.charAt(0));
    }

    private boolean isOperator(String cmd) {
        return "+".equals(cmd) || "-".equals(cmd) || "*".equals(cmd) || "/".equals(cmd);
    }

    private void handleDigit(String digit) {
        if (startNewNumber) {
            display.setText(digit);
            startNewNumber = false;
        } else {
            display.setText(display.getText() + digit);
        }
    }

    private void handleOperator(String op) {
        try {
            storedValue = Double.parseDouble(display.getText());
            pendingOp = op;
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private void computeResult() {
        if (pendingOp == null) {
            return; // nothing to do
        }
        try {
            double current = Double.parseDouble(display.getText());
            double result;
            switch (pendingOp) {
                case "+":
                    result = storedValue + current;
                    break;
                case "-":
                    result = storedValue - current;
                    break;
                case "*":
                    result = storedValue * current;
                    break;
                case "/":
                    if (current == 0.0) {
                        display.setText("Div by 0");
                        pendingOp = null;
                        startNewNumber = true;
                        return;
                    }
                    result = storedValue / current;
                    break;
                default:
                    return;
            }
            display.setText(trimTrailingZero(result));
            pendingOp = null;
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            pendingOp = null;
            startNewNumber = true;
        }
    }

    private void clearAll() {
        storedValue = 0.0;
        pendingOp = null;
        startNewNumber = true;
        display.setText("0");
    }

    private String trimTrailingZero(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorApp().setVisible(true));
    }
}
