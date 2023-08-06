import java.io.BufferedOutputStream;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        final var server = new Server();

        // Добавление обработчиков
        server.addHandler("GET", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                // TODO: Обработка GET запроса на путь "/messages"
                String lastParam = request.getQueryParam("last");
                String responseBody = "Hello from GET request with last param: " + lastParam;
                writeResponse(responseStream, responseBody);
            }
        });

        server.addHandler("POST", "/messages", new Handler() {
            public void handle(Request request, BufferedOutputStream responseStream) throws IOException {
                // TODO: Обработка POST запроса на путь "/messages"
                String responseBody = "Hello from POST ";
                writeResponse(responseStream, responseBody);
            }
        });

        server.listen(9999);
    }

    private static void writeResponse(BufferedOutputStream responseStream, String responseBody) throws IOException {
        byte[] content = responseBody.getBytes();
        responseStream.write((
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + content.length + "\r\n" +
                        "Connection: close\r\n" +
                        "\r\n"
        ).getBytes());
        responseStream.write(content);
        responseStream.flush();
    }
}