package usj.genielogiciel.investingapp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionSystemException;
import usj.genielogiciel.investingapp.exceptions.UserNotFound;
import usj.genielogiciel.investingapp.model.AppUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class userServiceTest
{
    private final UserService userService;

    @Autowired
    public userServiceTest(UserService userService)
    {
        this.userService = userService;
    }

    @BeforeEach
    public void CleanUp()
    {
        List<AppUser> users = userService.getUsers();

        for(AppUser user : users)
        {
            userService.deleteUser(user.getId());
        }
    }

    @Test
    public void getInvalidUser()
    {
        AppUser user = userService.getUser("blalbla");
        assertNull(user);
    }

    @Test
    public void getUser()
    {
        AppUser user = new AppUser(0, "teo", "chikhany", "12345", new ArrayList<>());
        userService.saveUser(user);

        List<AppUser> users = userService.getUsers();
        long size = users.size();
        AppUser new_user = users.get(0);

        assertEquals(1, size);
        assertEquals("teo", new_user.getUsername());
    }

    @Test
    public void addUser()
    {
        long oldSize = userService.getUsers().size();
        AppUser user = new AppUser(0, "teo", "chikhany", "12345", new ArrayList<>());
        userService.saveUser(user);

        long newSize = userService.getUsers().size();

        assertEquals(oldSize + 1, newSize);
    }

    @Test
    public void addInvalidUserEmptyUsername()
    {
        AppUser user = new AppUser(0, "", "chikhany", "12345", new ArrayList<>());

        TransactionSystemException er = Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.saveUser(user);
        });

        assertTrue(er.getOriginalException().getCause().toString().contains("Username cannot be empty"));
    }

    @Test
    public void addInvalidUserEmptyName()
    {
        AppUser user = new AppUser(0, "asd", "", "12345", new ArrayList<>());

        TransactionSystemException er = Assertions.assertThrows(TransactionSystemException.class, () -> {
            userService.saveUser(user);
        });

        assertTrue(er.getOriginalException().getCause().toString().contains("Name cannot be empty"));
    }


    @Test
    public void deleteUser()
    {
        AppUser user = new AppUser(0, "asd", "asdf", "12345", new ArrayList<>());
        userService.saveUser(user);
        long oldSize = userService.getUsers().size();

        AppUser newUser = userService.getUsers().get(0);

        userService.deleteUser(newUser.getId());
        long newSize = userService.getUsers().size();

        assertEquals(oldSize - 1, newSize);
    }


    @Test
    public void deleteInvalidStock()
    {
        UserNotFound thrown = Assertions.assertThrows(UserNotFound.class, () -> {
            userService.deleteUser(99);
        });

        assertEquals("No User with this id: 99", thrown.getMessage());
    }
}
