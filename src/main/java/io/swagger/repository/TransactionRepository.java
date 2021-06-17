package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

@Repository
public interface TransactionRepository extends CrudRepository<dbTransaction,Long> {

//    dbTransaction getTransactionsByIBAN(String IBANto);
//    Iterable<dbTransaction>getTransactionsByID(long id);
//    @Query("SELECT COUNT(*) FROM dbTransactions")
//    Integer CountAllTransactions();


}
