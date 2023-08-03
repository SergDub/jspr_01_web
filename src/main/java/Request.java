import java.io.BufferedReader;

class Request {
    private final String method;
    private final String path;
    private final BufferedReader in;

    public Request(String method, String path, BufferedReader in) {
        this.method = method;
        this.path = path;
        this.in = in;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public BufferedReader getReader() {
        return in;
    }

}
