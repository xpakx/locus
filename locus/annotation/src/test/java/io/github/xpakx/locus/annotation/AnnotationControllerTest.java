package io.github.xpakx.locus.annotation;

import io.github.xpakx.locus.annotation.dto.HighlightRequest;
import io.github.xpakx.locus.elasticsearch.ElasticSearchAspect;
import io.github.xpakx.locus.security.JwtUtils;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnnotationControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    JwtUtils jwt;
    @Autowired
    HighlightRepository highlightRepository;

    @MockBean
    ElasticSearchAspect aspect;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port + "/api/v1/annotations");
    }

    @AfterEach
    void tearDown() {
        highlightRepository.deleteAll();
    }
    private String tokenFor(String username) {
        return tokenFor(username, new ArrayList<>());
    }

    private String tokenFor(String username, List<GrantedAuthority> authorities) {
        return jwt.generateToken(new User(username, "", authorities));
    }

    @Test
    void shouldRespondWith401ToAnnotatePageIfNotAuthenticated() {
        when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToAnnotatePageIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
                .contentType(ContentType.JSON)
                .body(getHighlightRequest("http://example.com", "text"))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    private HighlightRequest getHighlightRequest(String url, String text) {
        return getHighlightRequest(url, text, null);
    }

    private HighlightRequest getHighlightRequest(String url, String text, String annotation) {
        return new HighlightRequest(url, text, Optional.ofNullable(annotation));
    }

    @Test
    void shouldHightlightText() {
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getHighlightRequest("http://example.com", "text"))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("url", equalTo("http://example.com"))
                .body("type", equalTo("TEXT"))
                .body("annotation", isNull())
                .body("text", equalTo("text"));
    }

}