package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class FindUserPostTest {

    private static final String USER_API = "/blog/user/{id}/post";

    @Test
    public void isNotAllowedToSendPostByRemovedUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .get(USER_API, 4);
    }

    @Test
    public void isAllowToSendUnremocedPosts() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("list.size()", is(3))
                   .when()
                   .get(USER_API, 1);
    }

    @Test
    public void isSendZeroPostLiked() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("likesCount", hasItem(0))
                   .when()
                   .get(USER_API, 1);
    }
}
