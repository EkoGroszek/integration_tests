package edu.iis.mto.blog.rest.test;

import static org.hamcrest.CoreMatchers.is;

import org.apache.http.HttpStatus;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

public class SearchUserTest extends FunctionalTests {

    @Test public void shouldReturnUserByFirstName() {
        String fullFirstName = "John";

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
                   .get("/blog/user/find?searchString=" + fullFirstName);
    }

    @Test public void shouldReturnUserByLastName() {
        String fullLastName = "Steward";

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
                   .get("/blog/user/find?searchString=" + fullLastName);
    }

    @Test public void shouldReturnUserByEmailAddress() {
        String emailAddress = "john@domain.com";

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
                   .get("/blog/user/find?searchString=" + emailAddress);
    }

    @Test public void shouldNotFindRemovedUser() {
        String lastNameOfRemovedUser = "Testowy";

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
                   .get("/blog/user/find?searchString=" + lastNameOfRemovedUser);
    }

}
