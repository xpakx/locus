package io.github.xpakx.locus.importing;

import io.github.xpakx.locus.bookmark.BookmarkRepository;
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
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ImportControllerTest {
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
        baseUrl = "http://localhost".concat(":").concat(port + "/api/v1/bookmarks/import/json");
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
    void shouldRespondWith401ToImportBookmarksIfNotAuthenticated() {
        when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToImportBookmarksIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
                .contentType(ContentType.JSON)
                .body(getBookmarksJsonRequest(List.of("http://example.com")))
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    private List<BookmarkRequest> getBookmarksJsonRequest(List<String> bookmarks) {
        return bookmarks
                .stream()
                .map(BookmarkRequest::new)
                .toList();
    }

    @Test
    void shouldImportBookmarks() throws IOException {
        doReturn("").when(urlReaderService).read(any(URL.class));
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(
                        getBookmarksJsonRequest(
                                List.of(
                                        "http://example.com/1",
                                        "http://example.com/2",
                                        "http://example.com/3"
                                )
                        )
                )
        .when()
                .post(baseUrl)
        .then()
                .log().body()
                .statusCode(OK.value());

        assertThat(bookmarkRepository.findAll(), hasSize(3));
    }

}