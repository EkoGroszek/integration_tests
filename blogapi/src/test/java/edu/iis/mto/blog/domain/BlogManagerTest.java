package edu.iis.mto.blog.domain;

import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.BlogPost;
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

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest {

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    LikePostRepository likedPostRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;


    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class )
    public void shouldThrowExceptionAfterNewUserAddLikeToOwnPost() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
        BlogPost blogPost = new BlogPost();
        blogPost.setUser(user);
        when(blogPostRepository.findById(1L)).thenReturn(java.util.Optional.of(blogPost));
        blogService.addLikeToPost(user.getId(), blogPost.getId());
    }

    @Test(expected = DomainError.class)
    public void shouldThrowExceptionAfterNewUserAddLikeToOtherUserPost(){
        User oldUser = new User();
        oldUser.setId(1L);
        oldUser.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(oldUser));

        User newUser = new User();
        newUser.setId(2L);
        newUser.setAccountStatus(AccountStatus.CONFIRMED);
        when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(newUser));

        BlogPost blogPost = new BlogPost();
        blogPost.setUser(oldUser);
        when(blogPostRepository.findById(1L)).thenReturn(java.util.Optional.of(blogPost));
        blogService.addLikeToPost(newUser.getId(), blogPost.getId());
    }
}
