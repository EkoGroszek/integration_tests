package edu.iis.mto.blog.rest.test;

import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SearchUserPostTest extends FunctionalTests {

    private static final String GET_API_START = "/blog/user/";
    private static final String GET_API_END = "/post";

    @Test
    public void searchOfPostsOfUserWithMultiplePostShouldReturnProperAmount() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", equalTo(4))
                   .when()
                   .get(GET_API_START + "1" + GET_API_END);
    }

    @Test
    public void searchOfPostOfRemovedUserShouldNotBeFound() {
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_BAD_REQUEST)
                   .when()
                   .post(GET_API_START + "3" + GET_API_END);
    }
}
