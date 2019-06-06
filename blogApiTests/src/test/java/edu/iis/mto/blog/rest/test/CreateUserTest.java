package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;

public class CreateUserTest extends FunctionalTests
    {

    private static final String USER_API = "/blog/user";
    private static final String ADMIN_API = "/blog/admin/user";


    @Test
    public void givenTwoUserWithThisSomeEmail_whenUsersTryRegister_thenSecondUserGet409Error()
        {
        Long id = addUserWithEmail("tracy@domain.com", HttpStatus.SC_CREATED);
        addUserWithEmail("tracy@domain.com", 409);
        adminDeleteUser4Ever(id);
        }


    @Test
    public void givenNewNotConfirmedUser_whenUserTryAddPost_thenUserGetPermissionError()
        {
        Long userId = addUserWithEmail("new@domain.com", HttpStatus.SC_CREATED);
        postBlogPost(userId,HttpStatus.SC_BAD_REQUEST);
        adminDeleteUser4Ever(userId);
        }

    private Long addUserWithEmail(String email, int exceptedStatus)
        {
        JSONObject jsonObj = new JSONObject().put("email", email);

        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(exceptedStatus)
                .when()
                .post(USER_API);

        if (exceptedStatus >= 400)
            {
            return null;
            }

        return response.andReturn()
                .jsonPath()
                .getLong("id");
        }

    private void adminDeleteUser4Ever(Long id)
        {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .delete(ADMIN_API + "/" + id);
        }

    private void postBlogPost(Long id,int exceptedStatus)
        {
        JSONObject jsonObj = new JSONObject().put("entry", "");
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(exceptedStatus)
                .when()
                .post(USER_API + "/" + id + "/post");
        }
    }
