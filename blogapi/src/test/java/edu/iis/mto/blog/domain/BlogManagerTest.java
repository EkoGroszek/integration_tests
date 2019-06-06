package edu.iis.mto.blog.domain;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.BlogPost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.BlogPostRepository;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.TestEntityBuilder;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogManagerTest
    {

    @MockBean
    UserRepository userRepository;

    @MockBean
    BlogPostRepository blogPostRepository;

    @MockBean
    LikePostRepository likePostRepository;

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Captor
    ArgumentCaptor<User> userParam;

    @Before
    public void setUp() throws Exception
        {
        when(userRepository.findById(any())).thenReturn(Optional.of(TestEntityBuilder.getUser()));
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(TestEntityBuilder.getBlogPost()));
        when(likePostRepository.findById(any())).thenReturn(Optional.of(TestEntityBuilder.getLikePost()));
        }

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW()
        {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        Mockito.verify(userRepository).save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
        }


    @Test(expected = DomainError.class)
    public void givenUserWithNewAccountStatus_whenUserLikeToPost_thenThrowDomainError()
        {
        //given
        prepareNewUser(AccountStatus.NEW);
        preparePost();
        //when
        blogService.addLikeToPost(1L, 1L);
        //then throw
        }

    @Test
    public void givenUserWithConfirmedAccountStatus_whenUserLikeToPost_thenCorrectAddedPost()
        {
        //given
        prepareNewUser(AccountStatus.CONFIRMED);
        preparePost();
        //when
        Boolean isAdded = blogService.addLikeToPost(1L, 1L);
        //then
        Assert.assertTrue(isAdded);
        }

    private void preparePost()
        {
        BlogPost blogPost = TestEntityBuilder.getBlogPost();
        User user2 = getUserWithId(2L);
        blogPost.setUser(user2);
        when(blogPostRepository.findById(any())).thenReturn(Optional.of(blogPost));
        }

    private void prepareNewUser(AccountStatus status)
        {
        User user = getUserWithId(1L);
        user.setAccountStatus(status);
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        }

    private User getUserWithId(Long id)
        {
        User user = TestEntityBuilder.getUser();
        user.setAccountStatus(AccountStatus.NEW);
        user.setId(id);
        return user;
        }

    }
