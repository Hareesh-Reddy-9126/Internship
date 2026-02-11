import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter base currency (e.g., USD): ");
        String base = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

        System.out.print("Enter target currency (e.g., INR): ");
        String target = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

        System.out.print("Enter amount: ");
        if (!scanner.hasNextDouble()) {
            System.out.println("Invalid amount.");
            scanner.close();
            return;
        }
        double amount = scanner.nextDouble();
        scanner.nextLine(); // consume trailing newline

        if (base.length() != 3 || target.length() != 3) {
            System.out.println("Currency codes must be 3 letters (e.g., USD, EUR).");
            scanner.close();
            return;
        }

        String json;
        try {
            json = fetchRatesJson(base);
        } catch (IOException e) {
            System.out.println("Network error: " + e.getMessage());
            scanner.close();
            return;
        }

        if (json == null || json.isEmpty()) {
            System.out.println("Failed to retrieve data.");
            scanner.close();
            return;
        }

        if (!json.contains("\"result\":\"success\"")) {
            String info = extractInfo(json);
            if (info != null) {
                System.out.println("API reported failure: " + info);
            } else {
                System.out.println("API reported failure. Check currency codes.");
            }
            scanner.close();
            return;
        }

        Double rate = extractRate(json, target);
        if (rate == null) {
            System.out.println("Could not find rate for target currency: " + target);
            scanner.close();
            return;
        }

        double converted = amount * rate;
        System.out.printf("%f %s = %f %s%n", amount, base, converted, target);

        scanner.close();
    }

    private static String fetchRatesJson(String base) throws IOException {
        String endpoint = "https://open.er-api.com/v6/latest/" + base;
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int status = conn.getResponseCode();
        if (status != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP status " + status);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } finally {
            conn.disconnect();
        }
    }

    private static Double extractRate(String json, String target) {
        int ratesIdx = json.indexOf("\"rates\"");
        if (ratesIdx == -1) {
            return null;
        }
        int braceStart = json.indexOf('{', ratesIdx);
        if (braceStart == -1) {
            return null;
        }
        int braceEnd = findMatchingBrace(json, braceStart);
        if (braceEnd == -1) {
            return null;
        }

        String ratesBlock = json.substring(braceStart + 1, braceEnd); // contents inside rates {...}
        String key = "\"" + target + "\":";
        int keyIdx = ratesBlock.indexOf(key);
        if (keyIdx == -1) {
            return null;
        }
        int valueStart = keyIdx + key.length();
        while (valueStart < ratesBlock.length() && Character.isWhitespace(ratesBlock.charAt(valueStart))) {
            valueStart++;
        }
        int valueEnd = valueStart;
        while (valueEnd < ratesBlock.length()) {
            char ch = ratesBlock.charAt(valueEnd);
            if ((ch >= '0' && ch <= '9') || ch == '.' || ch == '-' || ch == '+' || ch == 'e' || ch == 'E') {
                valueEnd++;
            } else {
                break;
            }
        }
        if (valueStart == valueEnd) {
            return null;
        }
        String numberStr = ratesBlock.substring(valueStart, valueEnd).trim();
        try {
            return Double.parseDouble(numberStr);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private static String extractInfo(String json) {
        String key = "\"error-type\":";
        int idx = json.indexOf(key);
        if (idx == -1) {
            return null;
        }
        int start = json.indexOf('"', idx + key.length());
        if (start == -1) {
            return null;
        }
        int end = json.indexOf('"', start + 1);
        if (end == -1) {
            return null;
        }
        return json.substring(start + 1, end);
    }

    private static int findMatchingBrace(String text, int openIdx) {
        int depth = 0;
        for (int i = openIdx; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '{') {
                depth++;
            } else if (ch == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }
}
