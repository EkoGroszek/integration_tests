package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SearchUserTest extends FunctionalTests {

    @Test
    public void shouldFindUserByFullFirstName() {
        String user = "John";
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("size()", is(1))
                   .when()
                   .get("/blog/user/find?searchString=" + user);
    }

    @Test
    public void shouldNotFindRemovedUser() {
        String user = "Removed";
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
                   .get("/blog/user/find?searchString=" + user);
    }

}
