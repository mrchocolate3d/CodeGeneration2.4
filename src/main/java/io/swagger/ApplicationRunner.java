package io.swagger;

import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;

@Component
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {
    @Autowired
    TransactionRepository repository;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        dbTransaction dbTransaction = new dbTransaction("Test","NL10INH0000000000","NL20INH0000000000",700.00, OffsetDateTime.now());
        repository.save(dbTransaction);
    }
}
