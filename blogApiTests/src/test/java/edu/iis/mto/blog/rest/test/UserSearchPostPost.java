package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.hasItem;

public class UserSearchPostPost {

    @Test
    public void testForCheckCountPostsForValidUsersTest() {
        JSONObject jsonObj = new JSONObject();

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(1))
                .when()
                .get("/blog/user/1/post");
    }

    @Test
    public void testForCheckCountPostsForValidUsersTestWithoutPost() {
        JSONObject jsonObj = new JSONObject();

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("size()", is(0))
                .when()
                .get("/blog/user/2/post");
    }

    @Test
    public void testForCheckCountLikesForRemovedUsersTest_shouldReturnCorrectNumberLikesPost() {
        JSONObject jsonObj = new JSONObject();

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .and()
                .body("likesCount", hasItem(0))
                .when()
                .get("/blog/user/1/post");
    }

    @Test
    public void testForCheckCountPostsForRemovedUsersTest_shouldReturnBardRequest() {
        JSONObject jsonObj = new JSONObject();

        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .get("/blog/user/4/post");

    }


}
