package io.swagger.repository;

import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<dbTransaction,Long> {

//    @Query("Select t from db_transaction where t.ibanfrom =:iban OR t.ibanto =:iban ORDER BY t.timestamp DESC")
//    List<dbTransaction> getTransactionByIban(String iban);
    Iterable<dbTransaction>getTransactionsByIBANto(String IBAN);

//    List<dbTransaction> getTransactionsByLimitAndDate(OffsetDateTime from, OffsetDateTime to, Pageable page);

//    List<dbTransaction> getdbTransaction(String iban);


    //count//send to service
    //second transaction list
    //move one to another list
    //once limit is reached
    //send old transaction back
}
