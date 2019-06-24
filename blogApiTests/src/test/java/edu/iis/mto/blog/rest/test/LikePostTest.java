package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;

public class LikePostTest extends FunctionalTests {

    private static final String LIKE_POST_API_START = "/blog/user/";

    @Test
    public void LikeBlogPostByConfirmedUserReturns201Code() {
        String userId = "1";
        String postId = "1";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST_API_START + userId + "/like/" + postId);
    }

    @Test
    public void TwoLikesBySameUserDoesNotCountAsTwo() {
        String userId = "1";
        String postId = "2";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST_API_START + userId + "/like/" + postId);
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .post(LIKE_POST_API_START + userId + "/like/" + postId);
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .when()
                .get("/blog/user/4/post")
                .then()
                .body("likesCount", hasItem(1));
    }
}
