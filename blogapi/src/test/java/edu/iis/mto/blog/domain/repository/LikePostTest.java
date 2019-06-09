package edu.iis.mto.blog.domain.repository;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostTest {

    @Autowired
    public TestEntityManager testEntityManager;
    @Autowired
    public LikePostRepository likePostRespository;
    @Autowired
    public UserRepository userRespository;

    public User user;
    public LikePost likePost;
    public BlogPost blogPost;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        userRespository.save(user);

        blogPost = new BlogPost();
        blogPost.setEntry("First Post");
        blogPost.setUser(user);
        testEntityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);

    }

    @Test
    public void isFindNoPostWithEmptyRespository() {
        List<LikePost> allPosts = likePostRespository.findAll();

        Assert.assertThat(allPosts, Matchers.hasSize(0));
    }

    @Test
    public void isSaveNewLikedPost() {

        LikePost thisLikedPost = likePostRespository.save(likePost);

        Assert.assertThat(thisLikedPost.getId(), Matchers.notNullValue());
    }

    @Test
    public void isFindOnePostLikedIfRespositoryHasOnePost() {
        LikePost thisLikedPost = testEntityManager.persist(likePost);
        List<LikePost> like = likePostRespository.findAll();

        Assert.assertThat(like, Matchers.hasSize(1));
        Assert.assertThat(like.get(0)
                              .getPost(),
                Matchers.equalTo(thisLikedPost.getPost()));
    }
}
