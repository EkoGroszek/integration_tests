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

import edu.iis.mto.blog.api.request.UserRequest;
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

    @Autowired
    BlogDataMapper dataMapper;

    @Autowired
    BlogService blogService;

    @Autowired
    LikePostRepository likePostRepo;

    @Autowired
    BlogPostRepository blogPostRepository;

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
    public void shouldAllowConfirmedUserAddLikeToPost() {
        User user = dataMapper.mapToEntity(new UserRequest("Jan", "Kowalski", "jan@domain.com"));
        user.setAccountStatus(AccountStatus.CONFIRMED);
        User persistedUser = userRepository.save(user);

        BlogPost post = new BlogPost();
        post.setEntry("My first post");
        post.setUser(persistedUser);
        BlogPost persistedPost = blogPostRepository.save(post);

        blogService.addLikeToPost(persistedUser.getId(), persistedPost.getId());

        ArgumentCaptor<LikePost> like = ArgumentCaptor.forClass(LikePost.class);
        Mockito.verify(likePostRepo)
               .save(like.capture());
    }
}
