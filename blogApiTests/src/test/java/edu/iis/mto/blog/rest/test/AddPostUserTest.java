package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

public class AddPostUserTest extends FunctionalTests {


    @Test
    public void testForAddPositWithCorrectData() {
        JSONObject jsonObj = new JSONObject().put("entry", "Testing");

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post("/blog/user/1/post");
    }

    @Test
    public void testForAddPositWithIncorrectData() {
        JSONObject jsonObj = new JSONObject().put("entry", "Testing-Incorrect");

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_CREATED)
                .when()
                .post("/blog/user/2/post");
    }
}
