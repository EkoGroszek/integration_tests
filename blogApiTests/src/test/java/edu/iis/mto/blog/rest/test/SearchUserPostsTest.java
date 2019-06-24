package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.core.Is.is;

public class SearchUserPostsTest extends FunctionalTests {

    private static final String GET_API_START = "/blog/user/";
    private static final String GET_API_END = "/post";

    @Test
    public void searchOfPostOfRemovedUserShouldReturnBadRequest() {
        String userId = "3";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(GET_API_START + userId + GET_API_END);
    }

    @Test
    public void searchOfPostOfUserWithNoPostShouldReturnNothing() {
        String userId = "2";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(0))
                .when()
                .get(GET_API_START + userId + GET_API_END);
    }

    @Test
    public void searchOfPostsOfUserWithMultiplePostShouldReturnProperAmount() {
        String userId = "4";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(2))
                .when()
                .get(GET_API_START + userId + GET_API_END);
    }

    @Test
    public void searchOfPostOfUserShouldReturnPostWithProperLikesAmountWhenPostHasLikes() {
        String userId = "1";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post("/blog/user/4/like/4");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .and()
                .body("likesCount", hasItems(1))
                .when()
                .get(GET_API_START + userId + GET_API_END);
    }

    @Test
    public void searchOfPostOfUserShouldReturnPostWithProperLikesAmountWhenPostHasNoLikes() {
        String userId = "5";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .and()
                .body("likesCount", hasItems(0))
                .when()
                .get(GET_API_START + userId + GET_API_END);
    }
}
