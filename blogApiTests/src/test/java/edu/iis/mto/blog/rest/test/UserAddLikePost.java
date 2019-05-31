package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAddLikePost {

    private static final String USER_API = "/blog/user/{userid}/like/{postId}";

    @Test
    public void shouldAllowOnlyConfirmedUserAddLikePost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);
    }

    @Test
    public void shouldNotAllowUnconfirmedUserAddLikePost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(USER_API, 2, 1);
    }

    @Test
    public void oneUserCantAddMultipleLikesToOnePost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);

        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);

        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .when()
                   .get("/blog/user/{id}/post", 1)
                   .then()
                   .body("likesCount", hasItem(1));
    }

    @Test
    public void shouldNotAllowAddLikeToOwnPost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(USER_API, 1, 1);
    }

}
