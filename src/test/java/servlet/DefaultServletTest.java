package servlet;

import http.request.HttpRequest;
import http.request.HttpRequestFactory;
import http.response.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import static http.response.HttpStatus.NOT_FOUND;
import static http.response.HttpStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

class DefaultServletTest {
    private DefaultServlet defaultServlet;

    @BeforeEach
    void setUp() {
        defaultServlet = new DefaultServlet();
    }

    @Test
    @DisplayName("Request URI에 해당하는 리소스가 Templates에 있는 경우 200 OK")
    void existTemplateResource() throws IOException {
        String requestMessage = "GET /index.html HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Accept: */*";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        HttpRequest request = HttpRequestFactory.makeHttpRequest(in);
        HttpResponse response = new HttpResponse(request.getVersion(), new PipedOutputStream());

        defaultServlet.handle(request, response);

        assertThat(response.getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("Request URI에 해당하는 리소스가 Static에 있는 경우 200 OK")
    void existStaticResource() throws IOException {
        String requestMessage = "GET /css/styles.css HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Accept: */*";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        HttpRequest request = HttpRequestFactory.makeHttpRequest(in);
        HttpResponse response = new HttpResponse(request.getVersion(), new PipedOutputStream());

        defaultServlet.handle(request, response);

        assertThat(response.getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("Request URL에 해당하는 리소스가 없는 경우 404 Not Found")
    void resourceNotExist() throws IOException {
        String requestMessage = "GET /not_exist.html HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Accept: */*";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        HttpRequest request = HttpRequestFactory.makeHttpRequest(in);
        HttpResponse response = new HttpResponse(request.getVersion(), new PipedOutputStream());

        defaultServlet.handle(request, response);

        assertThat(response.getStatus()).isEqualTo(NOT_FOUND);
    }
}