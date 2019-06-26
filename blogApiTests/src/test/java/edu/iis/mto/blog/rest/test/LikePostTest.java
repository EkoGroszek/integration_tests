package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItem;

public class LikePostTest extends FunctionalTests {

    @Test public void shouldReturn201WhenLikeBlogPostByConfirmedUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post("/blog/user" + 1 + "/like/" + 1);
    }

    @Test public void shouldReturn403WhenLikeBlogPostByNewUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_FORBIDDEN)
                   .when()
                   .post("/blog/user" + 2 + "/like/" + 1);
    }

    @Test public void shouldReturn403WhenLikeBlogPostByOwnerOfThisPost() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_FORBIDDEN)
                   .when()
                   .post("/blog/user" + 1 + "/like/" + 1);
    }

    @Test public void shouldReturn403WhenLikeBlogPostByUserWithRemovedStatus() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_FORBIDDEN)
                   .when()
                   .post("/blog/user" + 4 + "/like/" + 1);
    }

    @Test public void shouldReturnOnlyOneLikeWhenTheSameUserLikesTheSamePost() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post("/blog/user" + 1 + "/like/" + 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post("/blog/user/" + 1 + "/like/" + 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .when()
                   .get("/blog/user/4/post")
                   .then()
                   .body("likesCount", hasItem(1));
    }

}
