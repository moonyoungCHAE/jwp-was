package http.response;

import java.util.HashMap;
import java.util.Map;

public class ResponseHeader {
    public static final String CONTENT_LENGTH_NAME = "Content-Length";
    public static final String CONTENT_TYPE_NAME = "Content-Type";
    public static final String LOCATION_NAME = "Location";
    public static final String COOKIE_NAME = "Set-Cookie";

    private static final String HEADER_DELIMITER = ": ";
    private static final String CRLF = "\r\n";

    private Map<String, String> responseHeader;

    private ResponseHeader(Map<String, String> responseHeader) {
        this.responseHeader = responseHeader;
    }

    public static ResponseHeader of() {
        ResponseHeader responseHeader = new ResponseHeader(new HashMap<>());
        responseHeader.put("Content-Type", "text/plain;charset=utf-8");
        responseHeader.put("Content-Length", "0");

        return responseHeader;
    }

    public void put(String headerKey, String headerValue) {
        responseHeader.put(headerKey, headerValue);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : responseHeader.keySet()) {
            sb.append(key).append(HEADER_DELIMITER).append(responseHeader.get(key)).append(CRLF);
        }
        return sb.toString();
    }
}