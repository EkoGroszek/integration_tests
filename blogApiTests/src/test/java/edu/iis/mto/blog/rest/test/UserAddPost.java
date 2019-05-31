package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAddPost {

    private static final String USER_API = "/blog/user/{id}/post";

    @Test
    public void shouldAllowConfirmedUserAddPost() {
        JSONObject jsonObj = new JSONObject().put("entry", "Confirmed user post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_CREATED)
                   .when()
                   .post(USER_API, 1);
    }

    @Test
    public void shouldNotAllowUnconfirmedUserAddPost() {
        JSONObject jsonObj = new JSONObject().put("entry", "Confirmed user post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(USER_API, 2);
    }
}
