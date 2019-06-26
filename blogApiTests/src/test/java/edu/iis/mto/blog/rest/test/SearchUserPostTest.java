package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SearchUserPostTest extends FunctionalTests {

    @Test public void shouldReturn400WhenSearchingForPostOfRemovedUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post("/blog/user/" + 4 + "/post");
    }

    @Test public void shouldReturnGoodAmountOfPostsOfSpecificUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", is(2))
                   .when()
                   .get("/blog/user/" + 1 + "/post");
    }

    @Test public void shouldReturnEmptyListOfPostsWhenThereIsNoPostForSpecificUser() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", is(0))
                   .when()
                   .get("/blog/user/" + 2 + "/post");
    }

    @Test public void shouldReturnPostWithSpecificAmountOfLikesIfPostHasLikes() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .when()
                   .post("/blog/user/5/like/4");
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", is(1))
                   .and()
                   .body("likesCount", hasItems(1))
                   .when()
                   .get("/blog/user" + 1 + "/post");
    }

}
