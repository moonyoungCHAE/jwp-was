package servlet;

import db.DataBase;
import http.request.HttpRequest;
import http.request.HttpRequestFactory;
import http.response.HttpResponse;
import http.session.HttpSession;
import http.session.HttpSessionTable;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedOutputStream;

import static http.HttpHeaders.LOCATION;
import static http.response.HttpStatus.FOUND;
import static http.response.HttpStatus.OK;
import static org.assertj.core.api.Assertions.assertThat;

class UserListServletTest {
    private UserListServlet userListServlet;

    @BeforeEach
    void setUp() {
        userListServlet = new UserListServlet();
    }

    @Test
    @DisplayName("로그인 상태일 경우(Cookie 값이 logined=true) 경우 사용자 목록을 출력")
    void showListWhenLogin() throws IOException {
        User user = new User("userId", "password", "userName", "email@woo.wa");
        DataBase.addUser(user);

        HttpSession session = HttpSessionTable.getSession("1", () -> String.valueOf(1));
        session.setAttribute("logined", "true");

        String requestMessage = "GET /user/list HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Accept: */*\n"
                + "Cookie: sessionId=" + session.getId();
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        HttpRequest request = HttpRequestFactory.makeHttpRequest(in);
        HttpResponse response = new HttpResponse(request.getVersion(), new PipedOutputStream());

        userListServlet.handle(request, response);

        assertThat(response.getStatus()).isEqualTo(OK);
    }

    @Test
    @DisplayName("로그아웃 상태일 경우(Cookie 값이 logined=false) 경우 login.html로 이동")
    void showListWhenLogout() throws IOException {
        String requestMessage = "GET /user/list HTTP/1.1\n"
                + "Host: localhost:8080\n"
                + "Connection: keep-alive\n"
                + "Accept: */*\n"
                + "Cookie: logined=false";
        InputStream in = new ByteArrayInputStream(requestMessage.getBytes());
        HttpRequest request = HttpRequestFactory.makeHttpRequest(in);
        HttpResponse response = new HttpResponse(request.getVersion(), new PipedOutputStream());

        userListServlet.handle(request, response);

        assertThat(response.getStatus()).isEqualTo(FOUND);
        assertThat(response.getHeaders().getHeader(LOCATION)).isEqualTo("/index.html");
    }
}