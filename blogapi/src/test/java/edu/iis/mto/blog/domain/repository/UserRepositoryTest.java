package edu.iis.mto.blog.domain.repository;

import static org.hamcrest.Matchers.equalTo;
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
        user.setLastName("Kowalski");
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
        Assert.assertThat(users.get(0)
                               .getEmail(),
                Matchers.equalTo(persistedUser.getEmail()));
    }

    @Test
    public void shouldStoreANewUser() {

        User persistedUser = repository.save(user);

        Assert.assertThat(persistedUser.getId(), Matchers.notNullValue());
    }

    @Test
    public void shouldFindUserByFirstNameIgnoringCaseIfFullFirstNameIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("jAn", "x", "x");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldNotFindUserByFirstNameIgnoringCaseIfFullFirstNameIsGivenWrong() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("johN", "x", "x");
        assertThat(users.contains(user), equalTo(false));
    }

    @Test
    public void shouldFindUserByFirstNameIgnoringCaseIfPartOfFirstNameIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("j", "x", "x");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByLastNameIgnoringCaseIfFullLastNameIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "kOwALSkI", "x");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldNotFindUserByLastNameIgnoringCaseIfLastNameIsGivenWrong() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "nOwAk", "x");
        assertThat(users.contains(user), equalTo(false));
    }

    @Test
    public void shouldFindUserByLastNameIgnoringCaseIfPartOfLastNameIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "LSkI", "x");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByEmailIgnoringCaseIfFullEmailIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "x",
                "john@domain.com");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByEmailIgnoringCaseIfWrongEmailIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "x",
                "wrong@email.com");
        assertThat(users.contains(user), equalTo(false));
    }

    @Test
    public void shouldFindUserByEmailIgnoringCaseIfPartOfEmailIsGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("x", "x",
                "Ohn@domain.com");
        assertThat(users.contains(user), equalTo(true));
    }

    @Test
    public void shouldFindUserByAllCorrectDataGiven() {
        repository.save(user);
        List<User> users = repository.findByFirstNameContainingOrLastNameContainingOrEmailContainingAllIgnoreCase("JaN", "KOWaLsKI",
                "john@domain.com");
        assertThat(users.contains(user), equalTo(true));
    }
}
