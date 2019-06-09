package edu.iis.mto.blog.domain;

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
    @Autowired
    public LikePostRepository likePostRespository;

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
    public void isThorwErrorIfUserLikedPostDifferentThenConfirmed() {
        Long thisUserId = blogService.createUser(new UserRequest("John", "Steward", "john@domain.com"));
        Long thisPostId = blogService.createPost(thisUserId, new PostRequest());

        blogService.addLikeToPost(thisUserId, thisPostId);

        ArgumentCaptor<LikePost> likedPost = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRespository, never())
               .save(likedPost.capture());
    }

    public void isAllowConfirmedForUserLikedPost() {

        UserRequest userData = new UserRequest("John", "Steward", "john@domain.com");
        Long thisUserId = blogService.createUser(userData);
        Long thisPostId = blogService.createPost(thisUserId, new PostRequest());

        User user = dataMapper.mapToEntity(userData);
        user.setId(thisUserId);
        user.setAccountStatus(AccountStatus.CONFIRMED);

        blogService.addLikeToPost(thisUserId, thisPostId);
        ArgumentCaptor<LikePost> likedPost = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRespository)
               .save(likedPost.capture());
        LikePost liked = likedPost.getValue();
        Assert.assertThat(liked.getUser(), Matchers.equalTo(user));

    }

}
