import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
    private static final int PORT = 9999;
    private static final int THREAD_POOL_SIZE = 64;
    private final Map<String, Map<String, Handler>> handlers;
    private final ExecutorService executor;

    public Server() {
        //    this.validPaths = validPaths;
        this.handlers = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void addHandler(String method, String path, Handler handler) {
        handlers.computeIfAbsent(method, k -> new HashMap<>()).put(path, handler);
    }

    public void listen(int port) {
        try (final var serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    final var socket = serverSocket.accept();
                    executor.execute(() -> handleConnection(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection(Socket socket) {
        try (
                final var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                final var out = new BufferedOutputStream(socket.getOutputStream());
        ) {
            final var requestLine = in.readLine();
            final var parts = requestLine.split(" ");

            if (parts.length != 3) {
                return;
            }

            final var method = parts[0];
            final var path = parts[1];

            final var handlerMap = handlers.get(method);
            if (handlerMap != null) {
                final var handler = handlerMap.get(path);
                if (handler != null) {
                    handler.handle(new Request(method, path, in), out);
                    return;
                }
            }

            // Если не нашли обработчик, то возвращаем 404 Not Found
            out.write((
                    "HTTP/1.1 404 Not Found\r\n" +
                            "Content-Length: 0\r\n" +
                            "Connection: close\r\n" +
                            "\r\n"
            ).getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

