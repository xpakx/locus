package io.github.xpakx.locus.bookmark;

import io.github.xpakx.locus.bookmark.dto.BookmarkRequest;
import io.github.xpakx.locus.bookmark.dto.TagRequest;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TagControllerTest {
    @LocalServerPort
    private int port;
    private String baseUrl;

    @Autowired
    JwtUtils jwt;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    TagRepository tagRepository;

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
        tagRepository.deleteAll();
    }
    private String tokenFor(String username) {
        return tokenFor(username, new ArrayList<>());
    }

    private String tokenFor(String username, List<GrantedAuthority> authorities) {
        return jwt.generateToken(new User(username, "", authorities));
    }

    @Test
    void shouldRespondWith401ToTagBookmarkIfNotAuthenticated() {
        when()
                .post(baseUrl + "/{bookmarkId}/tags", 1L)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToTagBookmarkIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
                .contentType(ContentType.JSON)
                .body(getTagRequest("tag"))
        .when()
                .post(baseUrl + "/{bookmarkId}/tags", 1L)
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    private TagRequest getTagRequest(String name) {
        return new TagRequest(name);
    }

    @Test
    void shouldRespondWith404ToTagBookmarkIfPageNotBookmarked() throws IOException {
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getTagRequest("tag"))
        .when()
                .post(baseUrl + "/{bookmarkId}/tags", 1L)
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404ToTagBookmarkIfBookmarkDoesNotBelongToUser() throws IOException {
        Long id = addBookmark("http://example.com", "user2");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getTagRequest("tag"))
        .when()
                .post(baseUrl + "/{bookmarkId}/tags", id)
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    private Long addBookmark(String url, String owner) {
        Bookmark bookmark = new Bookmark();
        bookmark.setContent("content");
        bookmark.setDate(LocalDate.now());
        bookmark.setUrl(url);
        bookmark.setOwner(owner);
        return bookmarkRepository.save(bookmark).getId();
    }

    @Test
    void shouldTagBookmark() throws IOException {
        Long id = addBookmark("http://example.com", "user1");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getTagRequest("tag"))
        .when()
                .post(baseUrl + "/{bookmarkId}/tags", id)
        .then()
                .log().body()
                .statusCode(OK.value());
    }

    @Test
    void shouldNotCreateMoreThanOneTagWithSameName() throws IOException {
        Long id = addBookmark("http://example.com", "user1");
        addTag("tag", "user1");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(getTagRequest("tag"))
        .when()
                .post(baseUrl + "/{bookmarkId}/tags", id)
        .then()
                .log().body()
                .statusCode(OK.value());

        assertThat(tagRepository.findAll(), hasSize(1));
    }

    private Long addTag(String name, String owner) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setOwner(owner);
        return tagRepository.save(tag).getId();
    }

    @Test
    void shouldRespondWith401ToUntagBookmarkIfNotAuthenticated() {
        when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", 1L, "tag")
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith401ToUntagBookmarkIfTokenIsWrong() {
        given()
                .auth()
                .oauth2("329432853295")
        .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", 1L, "tag")
        .then()
                .log().body()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith404ToUntagBookmarkIfPageNotBookmarked() throws IOException {
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", 1L, "tag")
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404ToUntagBookmarkIfBookmarkDoesNotBelongToUser() throws IOException {
        Long id = addBookmark("http://example.com", "user2");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", id, "tag")
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404ToUntagBookmarkIfTagDoesNotExist() throws IOException {
        Long id = addBookmark("http://example.com", "user1");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", id, "tag")
        .then()
                .log().body()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldUntagBookmark() throws IOException {
        Long id = addBookmark("http://example.com", "user1");
        Long tagId = addTag("tag", "user1");
        tagBookmark(id, tagId);
        given()
                .auth()
                .oauth2(tokenFor("user1"))
       .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", id, "tag")
       .then()
                .log().body()
                .statusCode(OK.value());
    }

    private void tagBookmark(Long bookmarkId, Long tagId) {
        Set<Tag> tags = new HashSet<>();
        tags.add(tagRepository.findById(tagId).get());
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .get();
        bookmark.setTags(tags);
        bookmarkRepository.save(bookmark);
    }

    @Test
    void shouldReturn200ToUntagRequestEvenIfTagBookmarkRelationDoesNotExist() throws IOException {
        Long id = addBookmark("http://example.com", "user1");
        addTag("tag", "user1");
        given()
                .auth()
                .oauth2(tokenFor("user1"))
        .when()
                .delete(baseUrl + "/{bookmarkId}/tags/{tag}", id, "tag")
        .then()
                .log().body()
                .statusCode(OK.value());
    }
}