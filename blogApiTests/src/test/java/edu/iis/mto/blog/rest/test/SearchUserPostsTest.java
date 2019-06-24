package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class SearchUserPostsTest extends FunctionalTests {

    private static final String GET_API_START = "/blog/user/";
    private static final String GET_API_END = "/post";

    @Test
    public void searchOfPostOfRemovedUserShouldReturnBadRequest() {
        String userId = "3";
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .when()
                .post(GET_API_START + userId + GET_API_END);
    }
}
