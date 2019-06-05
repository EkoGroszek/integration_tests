package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

public class TestEntityBuilder
    {
    public static User getUser()
        {
        return User.builder().accountStatus(AccountStatus.NEW).email("user@user.com").firstName(
                "Jan").lastName("Kowalski").build();
        }

    public static BlogPost getBlogPost()
        {
        return BlogPost.builder().entry("").build();
        }

    public static LikePost getLikePost()
        {
        return LikePost.builder().build();
        }
    }
