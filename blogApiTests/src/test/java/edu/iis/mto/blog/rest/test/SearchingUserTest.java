package edu.iis.mto.blog.rest.test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class SearchingUserTest extends FunctionalTests{


    @Test public void requestWithFirstNameOfUser_ThatIsNotRemovedFromDataBase_ShouldReturnUsersData() {
        String fullFirstName = "Robert";

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

    @Test public void requestWithLastNameOfUser_ThatIsNotRemovedFromDataBase_ShouldReturnUsersData() {
        String fullFirstName = "Witczak";

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

    @Test public void requestWithLastNameOfUser_ThatIsRemovedFromDataBase_ShouldNotReturnUsersData() {
        String fullFirstName = "Jachimczak";

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
                   .get("/blog/user/find?searchString=" + fullFirstName);
    }

    @Test public void requestWithFirstNameOfUser_ThatIsRemovedFromDataBase_ShouldNotReturnUsersData() {
        String fullFirstName = "Wiktor";

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
                   .get("/blog/user/find?searchString=" + fullFirstName);
    }



}
