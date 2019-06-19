package edu.iis.mto.blog.domain.repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private LikePostRepository likePostRepo;

    @Autowired
    BlogPostRepository blogPostRepository;

    private User user;
    private BlogPost post;
    private LikePost like;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
        repository.save(user);

        post = new BlogPost();
        post.setEntry("My first post");
        post.setUser(user);
        blogPostRepository.save(post);

        like = new LikePost();
        like.setPost(post);
        like.setUser(user);
    }

    @Test
    public void shouldFindNoLikesPostIfRepositoryIsEmpty() {
        List<LikePost> posts = likePostRepo.findAll();

        Assert.assertThat(posts, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneLikePostIfRepositoryContainsOneLikePostEntity() {
        LikePost persistedLike = entityManager.persist(like);
        List<LikePost> likes = likePostRepo.findAll();

        Assert.assertThat(likes, Matchers.hasSize(1));
        Assert.assertThat(likes.get(0)
                               .getPost(),
                Matchers.equalTo(persistedLike.getPost()));
    }

    @Test
    public void shouldStoreANewLikePost() {

        LikePost persistedLike = likePostRepo.save(like);

        Assert.assertThat(persistedLike.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldReturnLikeInPostOfParticularUser() {
        likePostRepo.save(like);
        Optional<LikePost> optional = likePostRepo.findByUserAndPost(user, post);

        List<LikePost> likes = optional.map(Collections::singletonList)
                                       .orElseGet(Collections::emptyList);
        Assert.assertThat(likes.contains(like), Matchers.equalTo(true));
    }

    @Test
    public void shouldReturnFalseIfUserDoNotLikePost() {
        Optional<LikePost> optional = likePostRepo.findByUserAndPost(user, post);

        List<LikePost> likes = optional.map(Collections::singletonList)
                                       .orElseGet(Collections::emptyList);
        Assert.assertThat(likes.contains(like), Matchers.equalTo(false));
    }
}
