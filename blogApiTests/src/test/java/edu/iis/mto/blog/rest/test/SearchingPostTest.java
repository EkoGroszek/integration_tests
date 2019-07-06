package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class SearchingPostTest {

    @Test public void searchingForPostOfRemovedUserShouldNotSucceed() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post("/blog/user/" + 6 + "/post");
    }

    @Test public void searchingForPostsOfSpecificUserShouldReturnGoodAmountOfPosts() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", is(3))
                   .when()
                   .get("/blog/user/" + 1 + "/post");
    }

}
