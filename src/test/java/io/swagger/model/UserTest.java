package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Account account;

    @BeforeEach
    public void Setup(){
        user = new User();
    }
    @Test
    public void createUserShouldNotBeNull(){
        assertNotNull(user);
    }

}
