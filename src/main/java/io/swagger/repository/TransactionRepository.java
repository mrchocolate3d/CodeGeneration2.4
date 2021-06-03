package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    Iterable<Transaction>getTransactionsByIBAN(String IBAN);
    Iterable<Transaction>getTransactionsByID(long id);

}
