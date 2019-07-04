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
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LikePostRepository likePostRepository;

    User user;
    BlogPost blogPost;
    LikePost likePost;

    @Before
    public void init(){
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        repository.save(user);

        blogPost = new BlogPost();
        blogPost.setUser(user);
        blogPost.setEntry("someEntry");
        entityManager.persist(blogPost);

        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);
    }


    @Test
    public void shouldFindOneLikePostWhenOneLikePostIsAdded(){
        entityManager.persist(likePost);
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(1));

    }

    @Test
    public void shouldFindTwoLikePostWhenTwoDifferentLikePostAreAdded(){
        LikePost secondLikePost = new LikePost();
        secondLikePost.setPost(blogPost);
        secondLikePost.setUser(user);

        entityManager.persist(likePost);
        entityManager.persist(secondLikePost);

        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(2));

    }

    @Test
    public void shouldNotFindAnyLikePostWhenNoneHasBeenAdded(){
        entityManager.persist(likePost);
        List<LikePost> likePostList = likePostRepository.findAll();
        assertThat(likePostList.size(),is(1));

    }

}
