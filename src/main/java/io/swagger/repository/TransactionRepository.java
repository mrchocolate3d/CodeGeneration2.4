package io.swagger.repository;

import io.swagger.model.dbTransaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<dbTransaction, Long> {
    dbTransaction findTransactionById (long id);
}
