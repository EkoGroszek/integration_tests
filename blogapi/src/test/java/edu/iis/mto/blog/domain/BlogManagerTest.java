package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @MockBean
    LikePostRepository likePostRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    private User user;

    private User userWhoLikesPost;

    private BlogPost blogPost;

    @Before
    public void setUp() {
        initializeUserData();
        initializeBlogPostData();
    }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void shouldThrowExceptionIfUserWithAnotherStatusThanConfirmedTriesToLikePost() {
        // given
        List<BlogPost> blogPostsToReturnedByRepository = new ArrayList<>();
        blogPostsToReturnedByRepository.add(blogPost);

        // when
        Mockito.when(userRepository.findByLastName("Gitner")).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findByLastName("Kowalski")).thenReturn(Optional.of(userWhoLikesPost));
        Mockito.when(blogPostRepository.findByUser(user)).thenReturn(blogPostsToReturnedByRepository);
        Mockito.when(likePostRepository.findByUserAndPost(userWhoLikesPost, blogPost)).thenReturn(Optional.empty());

        // then
        blogService.addLikeToPost(user.getId(), userWhoLikesPost.getId());
    }

    @Test(expected = DomainError.class)
    public void shouldThrowExceptionWhenUserTriesToLikeHisOwnPost() {
        // given
        List<BlogPost> blogPostsToReturnedByRepository = new ArrayList<>();
        blogPostsToReturnedByRepository.add(blogPost);

        // when
        Mockito.when(userRepository.findByLastName("Gitner")).thenReturn(Optional.of(user));
        Mockito.when(blogPostRepository.findByUser(user)).thenReturn(blogPostsToReturnedByRepository);

        // then
        blogService.addLikeToPost(user.getId(), blogPost.getUser().getId());
    }

    @Test
    public void shouldSaveLikeToPostIfUserHaveConfirmedAccountStatus() {
        // given
        userWhoLikesPost.setAccountStatus(AccountStatus.CONFIRMED);
        ArgumentCaptor<LikePost> captor = ArgumentCaptor.forClass(LikePost.class);

        // when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(userWhoLikesPost));
        Mockito.when(blogPostRepository.findById(1L)).thenReturn(Optional.of(blogPost));
        Mockito.when(likePostRepository.findByUserAndPost(userWhoLikesPost, blogPost)).thenReturn(Optional.empty());
        blogService.addLikeToPost(userWhoLikesPost.getId(), blogPost.getId());

        // then
        Mockito.verify(likePostRepository).save(captor.capture());
        Assert.assertThat(captor.getValue().getPost(), is(blogPost));
        Assert.assertThat(captor.getValue().getUser(), is(userWhoLikesPost));
    }

    private void initializeUserData() {
        user = new User();
        user.setId(1L);
        user.setFirstName("Patryk");
        user.setLastName("Gitner");
        user.setEmail("209317@edu.p.lodz.pl");
        user.setAccountStatus(AccountStatus.NEW);

        userWhoLikesPost = new User();
        userWhoLikesPost.setId(2L);
        userWhoLikesPost.setFirstName("Jan");
        userWhoLikesPost.setLastName("Kowalski");
        userWhoLikesPost.setEmail("jkowalski@poczta.pl");
        userWhoLikesPost.setAccountStatus(AccountStatus.NEW);
    }

    private void initializeBlogPostData() {
        blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setEntry("stubEntry");
        blogPost.setUser(user);
    }

}
