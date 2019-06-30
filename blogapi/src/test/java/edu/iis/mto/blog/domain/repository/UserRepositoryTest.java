package edu.iis.mto.blog.domain.repository;


import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

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
import edu.iis.mto.blog.domain.model.User;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    private User user;

    @Before
    public void setUp() {
        user = new User();
        user.setFirstName("Jan");
        user.setLastName("Jakistam");
        user.setEmail("john@domain.com");
        user.setAccountStatus(AccountStatus.NEW);
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {

        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(0));
    }

    @Test
    public void shouldFindOneUsersIfRepositoryContainsOneUserEntity() {
        User persistedUser = entityManager.persist(user);
        List<User> users = repository.findAll();

        Assert.assertThat(users, Matchers.hasSize(1));
        Assert.assertThat(users.get(0).getEmail(), Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserUsingFirstnameWithIgnoreCaseUsingLowerCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jan", "", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserUsingFirstnameWithIgnoreCaseUsingUpperCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JAN", "", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserUsingFirstnameWithIgnoreCaseUsingRandomCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JaN", "", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserUsingLastNameWithIgnoreCaseUsingLowerCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "jakistam", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }
    @Test
    public void shouldFindUserUsingLastNameWithIgnoreCaseUsingUpperCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "JAKISTAM", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserUsingLastNameWithIgnoreCaseUsingRandomCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "JaKiStAm", "");
        assertThat(users.contains(user), is(equalTo(true)));
    }
    @Test
    public void shouldFindUserUsingEmailWithIgnoreCaseUsingUpperCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "", "JOHN@DOMAIN.COM");
        assertThat(users.contains(user), is(equalTo(true)));
    }

    @Test
    public void shouldFindUserUsingEmailWithIgnoreCaseUsingLowerCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "", "john@domain.com");
        assertThat(users.contains(user), is(equalTo(true)));
    }
    @Test
    public void shouldFindUserUsingEmailWithIgnoreCaseUsingRandomCase(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "", "JoHn@DoMaIn.CoM");
        assertThat(users.contains(user), is(equalTo(true)));
    }
    @Test
    public void shouldNotFindUserUsingBadEmailWithIgnoreCaseUsingRandomCas(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", " ", "bademail@com");
        assertThat(users.contains(user), is(equalTo(false)));
    }
    @Test
    public void shouldNotFindUserUsingBadLastNameWithIgnoreCaseUsingRandomCas(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase(" ", "BasLastName", " ");
        assertThat(users.contains(user), is(equalTo(false)));
    }
    @Test
    public void shouldNotFindUserUsingBadFirstNameWithIgnoreCaseUsingRandomCas(){
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("BadFirstName", " ", " ");
        assertThat(users.contains(user), is(equalTo(false)));
    }

}
