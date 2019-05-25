package edu.iis.mto.blog.domain;

import static org.mockito.Mockito.never;

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

import edu.iis.mto.blog.api.request.PostRequest;
import edu.iis.mto.blog.api.request.UserRequest;
import edu.iis.mto.blog.domain.errors.DomainError;
import edu.iis.mto.blog.domain.model.AccountStatus;
import edu.iis.mto.blog.domain.model.LikePost;
import edu.iis.mto.blog.domain.model.User;
import edu.iis.mto.blog.domain.repository.LikePostRepository;
import edu.iis.mto.blog.domain.repository.UserRepository;
import edu.iis.mto.blog.mapper.BlogDataMapper;
import edu.iis.mto.blog.services.BlogService;

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
    LikePostRepository likePostRepo;

    @Test
    public void creatingNewUserShouldSetAccountStatusToNEW() {
        blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        ArgumentCaptor<User> userParam = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository)
               .save(userParam.capture());
        User user = userParam.getValue();
        Assert.assertThat(user.getAccountStatus(), Matchers.equalTo(AccountStatus.NEW));
    }

    @Test(expected = DomainError.class)
    public void shouldThrowErrorIfUserAddLikeWithStatusDifferentThenConfirmed() {
        Long userId = blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        Long postId = blogService.createPost(userId, new PostRequest());

        blogService.addLikeToPost(userId, postId);

        ArgumentCaptor<LikePost> likeParam = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRepo, never())
               .save(likeParam.capture());
    }

    public void shouldAllowConfirmedUserAddLikeToPost() {
        UserRequest userData = new UserRequest("John", "Steward", "john@domain.com");

        Long userId = blogService.createUser(userData);
        Long postId = blogService.createPost(userId, new PostRequest());

        User user = dataMapper.mapToEntity(userData);
        user.setId(userId);
        user.setAccountStatus(AccountStatus.CONFIRMED);

        blogService.addLikeToPost(userId, postId);

        ArgumentCaptor<LikePost> likeParam = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRepo)
               .save(likeParam.capture());

        LikePost like = likeParam.getValue();
        Assert.assertThat(like.getUser(), Matchers.equalTo(user));
    }
}
