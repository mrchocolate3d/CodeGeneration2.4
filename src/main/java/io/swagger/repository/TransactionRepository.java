package io.swagger.repository;

import io.swagger.model.AccountType;
import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.threeten.bp.OffsetDateTime;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<dbTransaction,Long> {

    //Iterable<dbTransaction>getTransactionsByIBANfrom(String IBAN);
    //Iterable<dbTransaction>getTransactionsByIBANto(String IBAN);
    //Iterable<dbTransaction>getTransactionsByIBAN(String IBAN);

//    @Query("select t from dbTransaction t where t. =:IBAN OR t.ibanReceiver =:IBAN")
//    List<Transaction> getTransactionsFromIBAN(@Param("IBAN") String IBAN);

    @Query("select t from dbTransaction t where t.IBANfrom =:IBAN")
    List<dbTransaction> getTransactionsByIBANfrom(String IBAN);

    @Query("select t from dbTransaction t where t.IBANto =:IBAN")
    List<dbTransaction> getTransactionsByIBANto(String IBAN);

    @Transactional
    @Modifying
    @Query(value = "UPDATE db_Account SET balance = ?1 WHERE iban = ?2 AND account_type = ?3", nativeQuery = true)
    void updateAccountBalance(Double newBalance, String IBAN, AccountType accountType);


    @Query("SELECT COUNT(t) FROM dbTransaction t")
    Integer CountAllTransactions();
    @Query("SELECT t FROM dbTransaction t WHERE t.timestamp =?1 OR t.timestamp =?2 ORDER BY t.timestamp DESC")
    List<dbTransaction>getTimestampBetween(OffsetDateTime from, OffsetDateTime to, Integer limit);
    List<dbTransaction> getAllTransactionsByUserPerform(String user);
}
