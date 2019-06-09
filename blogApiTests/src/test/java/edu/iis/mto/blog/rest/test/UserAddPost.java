package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class UserAddPost {

    public static String USER_API = "/blog/user/{id}/post";

    @Test
    public void isAllowUserToAddPost() {
        JSONObject jsonObject = new JSONObject().put("entry", "This user post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObject.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_CREATED)
                   .when()
                   .post(USER_API, 1);
    }

    @Test
    public void isNotAllowWhenUserPostIsNotConfirmed() {
        JSONObject jsonObject = new JSONObject().put("entry", "This user post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObject.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_CREATED)
                   .when()
                   .post(USER_API, 2);
    }
}
