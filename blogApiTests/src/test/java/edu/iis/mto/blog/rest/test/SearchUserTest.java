package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SearchUserTest extends FunctionalTests  {

    private static final String SEARCH_USER_API = "/blog/user/find?searchString=";

    @Test
    public void shouldFindUserByFullFirstName() {
        String user = "Brian";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByFullMail() {
        String user = "john@domain.com";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByFullLastName() {
        String user = "Steward";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartialFirstName() {
        String user = "ian";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartialMail() {
        String user = "ohn@dom";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldFindUserByPartialLastName() {
        String user = "eward";
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
                .get(SEARCH_USER_API + user);
    }

    @Test
    public void shouldNotFindRemovedUser() {
        String user = "kowalski";
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
                .get(SEARCH_USER_API + user);
    }


}


