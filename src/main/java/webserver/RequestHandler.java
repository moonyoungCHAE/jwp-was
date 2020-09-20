package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FileIoUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final byte[] DEFAULT_BODY = "Hello World".getBytes();
    private static final byte[] ERROR_BODY = "Error".getBytes();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = handle(new HttpRequest(in));
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static byte[] getDefaultBody() {
        return DEFAULT_BODY;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public static byte[] getErrorBody() {
        return ERROR_BODY;
    }

    public byte[] handle(HttpRequest httpRequest) {
        try {
            if (httpRequest.isResourcePath("/index.html")) {
                return FileIoUtils.loadFileFromClasspath("./templates/index.html");
            } else if (httpRequest.isResourcePath("/user/form.html")) {
                return FileIoUtils.loadFileFromClasspath("./templates/user/form.html");
            }
        } catch (IOException | URISyntaxException e) {
            return ERROR_BODY;
        }
        return DEFAULT_BODY;
    }
}
