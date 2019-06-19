package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;

public class UserLikePost {

    private static final String USER_API = "/blog/user/{userid}/like/{postId}";

    @Test
    public void isAllowUserConfirmedToAddLikePost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post(USER_API, 3, 1);
    }

    @Test
    public void isNotAllowUserNotConfirmedToAddLikePost() {
        RestAssured.given()
                   .expect()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(USER_API, 2, 1);
    }

}
