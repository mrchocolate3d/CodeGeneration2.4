package io.swagger.model;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User user;
    private Account account;
    private User x = null;


    @BeforeEach
    public void createUser() {
        user = new User();

        user.setFirstName("Seyi");
        user.setLastName("Seyi");
        user.setUsername("Seyi");
        user.setPhone("0873893873838");
        user.setEmail("Seyi@email.com");
        user.setTransactionLimit(102.90);
        user.setId(399333333L);

    }



    @Test
    public void createUserShouldNotBeNull(){
        assertNotNull(user);

        System.out.println(user.toString());
    }


}
