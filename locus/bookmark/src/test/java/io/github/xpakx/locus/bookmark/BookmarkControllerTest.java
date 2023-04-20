package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.downloader.UrlReaderService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookmarkControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    JwtUtils jwt;
    @Autowired
    BookmarkRepository bookmarkRepository;

    @MockBean
    UrlReaderService urlReaderService;

    @MockBean
    ElasticSearchAspect aspect;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port + "/api/v1/bookmarks");
    }

    @AfterEach
    void tearDown() {
        bookmarkRepository.deleteAll();
    }
    private String tokenFor(String username) {
        return tokenFor(username, new ArrayList<>());
    }

    private String tokenFor(String username, List<GrantedAuthority> authorities) {
        return jwt.generateToken(new User(username, "", authorities));
    }

    @Test
    void shouldRespondWith401ToBookmarkPageIfNotAuthenticated() {
        when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToBookmarkPageIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
                .contentType(ContentType.JSON)
                .body(getBookmarkRequest("http://example.com"))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    private BookmarkRequest getBookmarkRequest(String url) {
        return new BookmarkRequest(url);
    }

    @Test
    void shouldBookmarkPage() throws IOException {
        doReturn("").when(urlReaderService).read(any(URL.class));
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getBookmarkRequest("http://example.com"))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("url", equalTo("http://example.com"));
    }

    @Test
    void shouldBookmarkPageWithCorrectContent() throws IOException {
        doReturn("webpage content").when(urlReaderService).read(any(URL.class));
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getBookmarkRequest("http://example.com"))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("url", equalTo("http://example.com"))
                .body("content", equalTo("webpage content"));
    }

    @Test
    void shouldRespondWith401ToViewBookmarkIfNotAuthenticated() {
        when()
                .get(baseUrl + "/{bookmarkId}", 1L)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToViewBookmarkIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
        .when()
                .get(baseUrl + "/{bookmarkId}", 1L)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith404ToViewBookmarkIfThereIsNoSuchBookmark() {
        given()
                .auth()
                .oauth2(tokenFor("user"))
        .when()
                .get(baseUrl + "/{bookmarkId}", 1L)
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldReturnBookmark()  {
        Long bookmarkId = addBookmark("http://example.com", "content", "user1");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .get(baseUrl + "/{bookmarkId}", bookmarkId)
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("url", equalTo("http://example.com"))
                .body("content", equalTo("content"));
    }

    @Test
    void shouldNotReturnBookmarkIfUserIsNotOwner()  {
        Long bookmarkId = addBookmark("http://example.com", "content", "user2");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .get(baseUrl + "/{bookmarkId}", bookmarkId)
        .then()
                .log().body()
                .statusCode(FORBIDDEN.value());
    }

    private Long addBookmark(String url, String content) {
        return addBookmark(url, content, "");
    }

    private Long addBookmark(String url, String content, String owner) {
        Bookmark bookmark = new Bookmark();
        bookmark.setContent(content);
        bookmark.setDate(LocalDate.now());
        bookmark.setUrl(url);
        bookmark.setOwner(owner);
        return bookmarkRepository.save(bookmark).getId();
    }

    @Test
    void shouldRespondWith401ToCheckBookmarkIfNotAuthenticated() {
        given()
                .param("url", "http://example.com")
        .when()
                .get(baseUrl + "/check")
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToCheckBookmarkIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
                .param("url", "http://example.com")
        .when()
                .get(baseUrl + "/check")
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithFalseToNonBookmarkedPage() throws IOException {
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .param("url", "http://example.com")
        .when()
                .get(baseUrl + "/check")
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("value", is(false));
    }

    @Test
    void shouldRespondWithTrueToBookmarkedPage() throws IOException {
        addBookmark("http://example.com", "");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .param("url", "http://example.com")
        .when()
                .get(baseUrl + "/check")
        .then()
                .log().body()
                .statusCode(OK.value())
                .body("value", is(true));
    }
}