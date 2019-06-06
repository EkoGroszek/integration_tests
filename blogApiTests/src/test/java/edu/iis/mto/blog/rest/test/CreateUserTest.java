package edu.iis.mto.blog.rest.test;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class CreateUserTest extends FunctionalTests
    {

    private static final String USER_API = "/blog/user";
    private static final String ADMIN_API = "/blog/admin/user";


    @Test
    public void givenTwoUserWithThisSomeEmail_whenSecondUserTryRegister_thenSecondUserGet409Error()
        {
        //given
        Long id = addUserWithEmail("tracy@domain.com", HttpStatus.SC_CREATED);
        //when
        addUserWithEmail("tracy@domain.com", 409);
        //then second user has 409 error

        //clean
        adminDeleteUser4Ever(id);
        }


    @Test
    public void givenNewNotConfirmedUser_whenUserTryAddPost_thenUserGetPermissionError()
        {
        //given
        Long userId = addUserWithEmail("new@domain.com", HttpStatus.SC_CREATED);
        //when
        postBlogPost(userId, HttpStatus.SC_BAD_REQUEST);
        //then permission error HttpStatus.SC_BAD_REQUEST

        //clean
        adminDeleteUser4Ever(userId);
        }

    @Test
    public void givenUserAccountWithConfirmedByAdmin_whenUserTryAddPost_thenUserGetSuccessCode()
        {
        //given
        Long userId = addUserWithEmail("new@domain.com", HttpStatus.SC_CREATED);
        adminConfirmAccountUser(userId);
        //when
        Long postId = postBlogPost(userId, HttpStatus.SC_CREATED);
        //then success add HttpStatus.SC_CREATED

        //clean
        adminDeleteUser4Ever(userId);
        }

    @Test
    public void givenTwoAccountAndOnePost_whenNotConfirmedUserTryLikePost_thenThisUserGetError()
        {
        //given
        Long user1 = addUserWithEmail("user1@domain.com", HttpStatus.SC_CREATED);
        adminConfirmAccountUser(user1);
        Long user2 = addUserWithEmail("user2@domain.com", HttpStatus.SC_CREATED);
        Long user1Post = postBlogPost(user1, HttpStatus.SC_CREATED);
        //when
        likePost(user2, user1Post, HttpStatus.SC_BAD_REQUEST);
        //then error HttpStatus.SC_BAD_REQUEST

        //clean
        adminDeleteUser4Ever(user1);
        adminDeleteUser4Ever(user2);
        }

    @Test
    public void givenTwoUserConfirmedByAdminAndOnePost_whenConfirmedUserLikePost_thenUserGetSuccessCode()
        {
        //given
        Long user1 = addUserWithEmail("user1@domain.com", HttpStatus.SC_CREATED);
        Long user2 = addUserWithEmail("user2@domain.com", HttpStatus.SC_CREATED);
        adminConfirmAccountUser(user1);
        adminConfirmAccountUser(user2);
        Long user1Post = postBlogPost(user1, HttpStatus.SC_CREATED);
        //when
        Boolean success = likePost(user2, user1Post, HttpStatus.SC_OK);
        //then success HttpStatus.SC_OK and
        Assert.assertTrue(success);
        //clean
        adminDeleteUser4Ever(user1);
        adminDeleteUser4Ever(user2);
        }

    @Test
    public void givenTwoUserConfirmedByAdminAndOnePost_whenConfirmedUserLikePostTwoTimes_thenUserGetFalseChangeLike()
        {
        //given
        Long user1 = addUserWithEmail("user1@domain.com", HttpStatus.SC_CREATED);
        Long user2 = addUserWithEmail("user2@domain.com", HttpStatus.SC_CREATED);
        adminConfirmAccountUser(user1);
        adminConfirmAccountUser(user2);
        Long user1Post = postBlogPost(user1, HttpStatus.SC_CREATED);
        //when
        likePost(user2, user1Post, HttpStatus.SC_OK);
        Boolean successChangeLike = likePost(user2, user1Post, HttpStatus.SC_OK);
        //then success HttpStatus.SC_OK and
        Assert.assertFalse(successChangeLike);

        //clean
        adminDeleteUser4Ever(user1);
        adminDeleteUser4Ever(user2);
        }

    @Test
    public void givenUserConfirmedByAdminAndOnePost_whenUserLikeOwnPost_thenUserGetErrorCode()
        {
        //given
        Long user1 = addUserWithEmail("user1@domain.com", HttpStatus.SC_CREATED);
        adminConfirmAccountUser(user1);
        Long user1Post = postBlogPost(user1, HttpStatus.SC_CREATED);
        //when
        likePost(user1, user1Post, HttpStatus.SC_BAD_REQUEST);
        //then success HttpStatus.SC_BAD_REQUEST

        //clean
        adminDeleteUser4Ever(user1);
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

    private void adminConfirmAccountUser(Long id)
        {
        RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .expect()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK)
                .when()
                .get(ADMIN_API + "/confirm/" + id);
        }

    private Long postBlogPost(Long id, int exceptedStatus)
        {
        JSONObject jsonObj = new JSONObject().put("entry", "");
        Response response = RestAssured.given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(exceptedStatus)
                .when()
                .post(USER_API + "/" + id + "/post");

        if (exceptedStatus >= 400)
            {
            return null;
            }

        return response.andReturn()
                .jsonPath()
                .getLong("id");
        }

    private Boolean likePost(Long userId, Long postId, int exceptedStatus)
        {
        JSONObject jsonObj = new JSONObject().put("entry", "");
        Response response = RestAssured
                .given()
                .accept(ContentType.JSON)
                .header("Content-Type", "application/json;charset=UTF-8")
                .body(jsonObj.toString())
                .expect()
                .log()
                .all()
                .statusCode(exceptedStatus)
                .when()
                .post(USER_API + "/" + userId + "/like/" + postId) //user/{userId}/like/{postId}
                .andReturn();

        if (exceptedStatus >= 400)
            {
            return null;
            }
        return Boolean.valueOf(response.asString());
        }
    }
