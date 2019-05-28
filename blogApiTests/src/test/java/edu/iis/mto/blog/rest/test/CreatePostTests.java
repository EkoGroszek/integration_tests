package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class CreatePostTests extends FunctionalTests {

    private static final String POST_API_START = "/blog/user/";
    private static final String POST_API_END = "/post";

    @Test
    public void postBlogPostByConfirmedUserReturns201Code() {
        String userId = "1";
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_CREATED)
                   .when()
                   .post(POST_API_START + userId + POST_API_END);
    }

    @Test
    public void postBlogPostByNewUserReturns403Code() {
        String userId = "2";
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_FORBIDDEN)
                   .when()
                   .post(POST_API_START + userId + POST_API_END);
    }

    @Test
    public void postBlogPostByCRemovedUserReturns403Code() {
        String userId = "3";
        JSONObject jsonObj = new JSONObject().put("entry", "blog post");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .body(jsonObj.toString())
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_FORBIDDEN)
                   .when()
                   .post(POST_API_START + userId + POST_API_END);
    }
}
