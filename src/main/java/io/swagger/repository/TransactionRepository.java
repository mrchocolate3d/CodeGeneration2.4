package io.swagger.repository;

import io.swagger.model.dbTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<dbTransaction,Long> {
    //dbTransaction findTransactionByID(Long id);
    Iterable<dbTransaction> getTransactionsByIBAN(String iban);
}
