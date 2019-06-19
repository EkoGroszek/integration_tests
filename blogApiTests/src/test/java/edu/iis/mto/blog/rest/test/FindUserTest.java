package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class FindUserTest {

    private static final String USER_API = "/blog/user/find";

    @Test
    public void shouldFindTwoUsersWithNameBrian() {
        String name = "Brian";
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", name)
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("list.size()", equalTo(2))
                   .when()
                   .get(USER_API);
    }

    @Test
    public void shouldFindOneUserWithNameJohn() {
        String name = "John";
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", name)
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("list.size()", equalTo(1))
                   .when()
                   .get(USER_API);
    }

    @Test
    public void shouldNotFindRemovedUsersByEmail() {
        String email = "zofia@domain.com";
        RestAssured.given()
                   .accept(ContentType.JSON)
                   .header("Content-Type", "application/json;charset=UTF-8")
                   .param("searchString", email)
                   .expect()
                   .log()
                   .all()
                   .statusCode(HttpStatus.SC_OK)
                   .and()
                   .body("list.size()", equalTo(0))
                   .when()
                   .get(USER_API);
    }

}
