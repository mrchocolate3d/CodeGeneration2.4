package io.swagger.repository;

import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;

import java.util.Iterator;
import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<dbTransaction,Long> {

    //dbTransaction getTransactionsByIBAN(String IBANfrom);


//    Iterable<dbTransaction>getTransactionsByID(long id);
//    @Query("SELECT COUNT(*) FROM dbTransactions")
//    Integer CountAllTransactions();

//    @Query("SELECT count(*) FROM DB_User u, DB_Transaction t JOIN u.accounts ")
//    Integer CountAllTransactions();

}
