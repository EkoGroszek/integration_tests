package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class CreatePostTest extends FunctionalTests {

    @Test
    public void shouldReturn201WhenPostBlogPostByConfirmedUser() {
        JSONObject jsonObject = new JSONObject().put("entry", "stub blog post");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post("/blog/user/1/post");
    }

    @Test
    public void shouldReturn403WhenPostBlogPostByNewUser() {
        JSONObject jsonObject = new JSONObject().put("entry", "stub blog post");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObject.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_FORBIDDEN)
                .when()
                .post("/blog/user/2/post");
    }

}
