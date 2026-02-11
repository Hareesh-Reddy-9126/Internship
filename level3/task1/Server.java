import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        BufferedReader console = null;
        Thread readerThread = null;
        final boolean[] running = {true};

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server listening on port " + PORT + "...");
            socket = serverSocket.accept();
            System.out.println("Client connected: " + socket.getInetAddress());

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            console = new BufferedReader(new InputStreamReader(System.in));

            BufferedReader finalIn = in;
            Socket finalSocket = socket;
            readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = finalIn.readLine()) != null) {
                        System.out.println("Client: " + line);
                        if ("exit".equalsIgnoreCase(line.trim())) {
                            System.out.println("Client requested exit. Closing connection.");
                            running[0] = false;
                            break;
                        }
                    }
                } catch (IOException e) {
                    if (running[0]) {
                        System.out.println("Read error: " + e.getMessage());
                    }
                } finally {
                    running[0] = false;
                    try { finalSocket.close(); } catch (IOException ignored) { }
                }
            });
            readerThread.start();

            while (running[0]) {
                String message = console.readLine();
                if (message == null) {
                    continue;
                }
                out.println(message);
                if ("exit".equalsIgnoreCase(message.trim())) {
                    running[0] = false;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        } finally {
            running[0] = false;
            closeQuietly(in);
            if (out != null) {
                out.close();
            }
            closeQuietly(console);
            closeQuietly(socket);
            closeQuietly(serverSocket);
            if (readerThread != null && readerThread.isAlive()) {
                try {
                    readerThread.join(500);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println("Server stopped.");
        }
    }

    private static void closeQuietly(ServerSocket s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static void closeQuietly(Socket s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static void closeQuietly(BufferedReader r) {
        if (r != null) {
            try {
                r.close();
            } catch (IOException ignored) {
            }
        }
    }
}
