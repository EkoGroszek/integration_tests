package edu.iis.mto.blog.domain.repository;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;

import static edu.iis.mto.blog.domain.repository.TestEntityBuilder.getBlogPost;
import static edu.iis.mto.blog.domain.repository.TestEntityBuilder.getLikePost;
import static edu.iis.mto.blog.domain.repository.TestEntityBuilder.getUser;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest
    {

    @Autowired
    LikePostRepository likePostRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void givenOnePost_whenGetAllPost_thenOnePostWillBeReturned()
        {
        makeAndSaveLikePost(getUser(), getBlogPost());

        List<LikePost> likePosts = likePostRepository.findAll();

        Assert.assertThat(likePosts, Matchers.hasSize(1));
        }

    @Test
    public void givenOnePost_whenWeFindPostByCorrectUserAndBlogPost_thenWeGetThisPost()
        {
        User user = getUser();
        BlogPost blogPost = getBlogPost();
        makeAndSaveLikePost(user, blogPost);

        Optional<LikePost> post = likePostRepository.findByUserAndPost(user, blogPost);

        Assert.assertTrue(post.isPresent());
        }

    @Test
    public void givenOnePost_whenWeFindPostByBadUserAndBlogPost_thenPostDoesNotExist()
        {
        User user = getUser();
        BlogPost blogPost = getBlogPost();
        makeAndSaveLikePost(user, blogPost);

        User otherUser = getUser();
        otherUser.setEmail("other@email.com");
        entityManager.persist(otherUser);

        Optional<LikePost> post = likePostRepository.findByUserAndPost(otherUser, blogPost);

        Assert.assertTrue(!post.isPresent());
        }

    private void makeAndSaveLikePost(User user, BlogPost blogPost)
        {
        blogPost.setUser(user);
        LikePost likePost = getLikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
        likePostRepository.save(likePost);
        }

    }
