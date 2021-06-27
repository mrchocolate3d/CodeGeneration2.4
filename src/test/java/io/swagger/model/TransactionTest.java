package io.swagger.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {
    private dbTransaction dbTransaction;

    @BeforeEach
    public void setup(){
        dbTransaction = new dbTransaction();
    }

    @Test
    public void CreatingTransactionShouldNotBeNull(){
        assertNotNull(dbTransaction);
    }
}
