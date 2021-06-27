package io.swagger.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AccountTest {

    private dbAccount dbAccount;

    @BeforeEach
    public void init(){
        dbAccount = new dbAccount();
    }


    @Test
    public void creatingAccountShouldNotBeNull(){
        Assertions.assertNotNull(dbAccount);
    }


    @Test
    public void accountBalanceLessThanZeroThrowsIllegalArgumentException(){
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, ()-> dbAccount.setBalance(-2));

        assertEquals("Balance cannot be below zero", exception.getMessage());

    }

    
}
