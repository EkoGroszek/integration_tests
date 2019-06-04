package edu.iis.mto.blog.domain;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    UserRepository userRepository;

    @MockBean
    private LikePostRepository postRepository;

    @MockBean
    private BlogPostRepository blogRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository)
               .save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test
    public void userWithConfirmedAccoundShouldBeAbleToLikePost() {

        User owner = new User();
        owner.setEmail("owner@domain.com");
        owner.setId(0L);
        owner.setAccountStatus(AccountStatus.NEW);
        userRepository.save(owner);
        Mockito.when(userRepository.findById(0L))
               .thenReturn(Optional.of(owner));

        User thatWhoLikes = new User();
        thatWhoLikes.setEmail("thatWhoLikes@domain.com");
        thatWhoLikes.setId(1L);
        thatWhoLikes.setAccountStatus(AccountStatus.CONFIRMED);
        userRepository.save(thatWhoLikes);
        Mockito.when(userRepository.findById(1L))
               .thenReturn(Optional.of(thatWhoLikes));

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(owner);
        blogPost.setId(0L);
        Mockito.when(blogRepository.findById(0L))
               .thenReturn(Optional.of(blogPost));

        Mockito.when(postRepository.findByUserAndPost(thatWhoLikes, blogPost))
               .thenReturn(Optional.empty());
        blogService.addLikeToPost(thatWhoLikes.getId(), blogPost.getId());

        ArgumentCaptor<LikePost> argumentCaptors = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(postRepository)
               .save(argumentCaptors.capture());

        LikePost likePost = argumentCaptors.getValue();
        assertThat(likePost.getPost(), equalTo(blogPost));
        assertThat(likePost.getUser(), equalTo(thatWhoLikes));
    }

    @Test(expected = DomainError.class)
    public void userWithRemovedAccountShouldNotBeAbleToLikePost() {

        User owner = new User();
        owner.setEmail("owner@domain.com");
        owner.setId(0L);
        owner.setAccountStatus(AccountStatus.NEW);
        userRepository.save(owner);
        Mockito.when(userRepository.findById(0L))
               .thenReturn(Optional.of(owner));

        User thatWhoLikes = new User();
        thatWhoLikes.setEmail("thatWhoLikes@domain.com");
        thatWhoLikes.setId(1L);
        thatWhoLikes.setAccountStatus(AccountStatus.REMOVED);
        userRepository.save(thatWhoLikes);
        Mockito.when(userRepository.findById(1L))
               .thenReturn(Optional.of(thatWhoLikes));

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(owner);
        blogPost.setId(0L);
        Mockito.when(blogRepository.findById(0L))
               .thenReturn(Optional.of(blogPost));

        Mockito.when(postRepository.findByUserAndPost(thatWhoLikes, blogPost))
               .thenReturn(Optional.empty());
        blogService.addLikeToPost(thatWhoLikes.getId(), blogPost.getId());

        ArgumentCaptor<LikePost> argumentCaptors = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(postRepository)
               .save(argumentCaptors.capture());

    }

    @Test(expected = DomainError.class)
    public void shouldThrownDomainErrorWhenUserAddLikeToOwnPostTest() {
        User owner = new User();
        owner.setEmail("owner@domain.com");
        owner.setId(0L);
        owner.setAccountStatus(AccountStatus.NEW);
        userRepository.save(owner);
        Mockito.when(userRepository.findById(0L))
               .thenReturn(Optional.of(owner));

        BlogPost blogPost = new BlogPost();
        blogPost.setId(1L);
        blogPost.setUser(owner);
        Mockito.when(blogRepository.findById(0L))
               .thenReturn(Optional.of(blogPost));

        blogService.addLikeToPost(owner.getId(), blogPost.getId());
    }

}
