package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private LikePost likePost;
    private BlogPost blogPost;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setFirstName("Janusz");
        user.setLastName("Polaczek");
        user.setEmail("janusz@cebula.pl");
        user.setAccountStatus(AccountStatus.NEW);
        entityManager.persist(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("testedEntry");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setUser(user);
        likePost.setPost(blogPost);
    }

    @Test
    public void shouldNotFindLikePost() {
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(0));
    }

    @Test
    public void shouldFindOneLikePost() {
        entityManager.persist(likePost);
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(1));
    }

    @Test
    public void shouldFindThatSameLikePost(){
        likePostRepository.save(likePost);
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(1));
        assertThat(likePostList.get(0),is(likePost));
    }
}