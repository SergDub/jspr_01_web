import java.io.BufferedReader;
import java.util.Map;

class Request {
    private final String method;
    private final String path;
    private final BufferedReader in;
    private final Map<String, String> queryParams;


    public Request(String method, String path, BufferedReader in, Map<String, String> queryParams) {
        this.method = method;
        this.path = path;
        this.in = in;
        this.queryParams = queryParams;
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

    public Map<String, String> getQueryParams() {
        return queryParams;
    }
    public String getQueryParam(String name) {
        return queryParams.get(name);
    }
}
