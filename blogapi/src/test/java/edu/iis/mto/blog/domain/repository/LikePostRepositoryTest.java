package edu.iis.mto.blog.domain.repository;

import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LikePostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikePostRepository likePostRepository;

    private User user;

    private BlogPost blogPost;

    private LikePost likePost;

    @Before
    public void setUp() {
        initializeUserData();
        initializeBlogPostData();
        initializeLikePostData();
    }

    @Test
    public void shouldReturnOnlyOneLikeIfThereIsOnlyOneLikePostInRepository() {
        // given
        likePostRepository.save(likePost);

        // when
        Optional<LikePost> fetchedFromRepository = likePostRepository.findByUserAndPost(user, blogPost);
        List<LikePost> likedPost = fetchedFromRepository.map(Collections::singletonList)
                .orElseGet(Collections::emptyList);

        // then
        Assert.assertThat(likedPost.size(), is(1));
    }

    @Test
    public void shouldInvokeFindByUserAndPostMethodOnlyOnceWhenFetchingDataFromRepository() {
        // given
        LikePostRepository mockedLikePostRepository = Mockito.mock(LikePostRepository.class);

        // when
        Mockito.when(mockedLikePostRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());
        mockedLikePostRepository.findByUserAndPost(user, blogPost);

        // then
        Mockito.verify(mockedLikePostRepository, Mockito.times(1)).findByUserAndPost(any(), any());
    }

    @Test
    public void shouldReturnEmptyListOfLikedPostIfThereIsNoDataInRepository() {
        // when
        Optional<LikePost> likedPosts = likePostRepository.findByUserAndPost(user, blogPost);

        // then
        Assert.assertThat(likedPosts, is(Optional.empty()));
    }

    @Test
    public void shouldModifyExistingLikePostInDatabaseIfTheOneAlreadyExists() {
        // given
        likePostRepository.save(likePost);
        List<LikePost> likedPosts = likePostRepository.findAll();
        LikePost fetchedPost = likedPosts.get(0);

        // when
        fetchedPost.getPost().setEntry("updatedEntry");
        likePostRepository.save(fetchedPost);
        likedPosts = likePostRepository.findAll();

        // then
        Assert.assertThat(likedPosts.get(0).getPost().getEntry(), is(fetchedPost.getPost().getEntry()));
    }

    private void initializeLikePostData() {
        likePost = new LikePost();
        likePost.setPost(blogPost);
        likePost.setUser(user);
    }

    private void initializeBlogPostData() {
        blogPost = new BlogPost();
        blogPost.setEntry("stubEntry");
        blogPost.setUser(user);
        entityManager.persist(blogPost);
    }

    private void initializeUserData() {
        user = new User();
        user.setFirstName("Patryk");
        user.setLastName("Gitner");
        user.setEmail("209317@edu.p.lodz.pl");
        user.setAccountStatus(AccountStatus.NEW);
        userRepository.save(user);
    }

}
