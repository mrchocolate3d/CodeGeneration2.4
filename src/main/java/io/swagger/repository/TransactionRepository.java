package io.swagger.repository;

import io.swagger.model.dbTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<dbTransaction,Long> {

    Iterable<dbTransaction>getTransactionsByIBANfrom(String IBAN);
    @Query("SELECT COUNT(t) FROM dbTransaction t")
    Integer CountAllTransactions();
    @Query("SELECT t FROM dbTransaction t WHERE t.timestamp =?1 OR t.timestamp =?2 ORDER BY t.timestamp DESC")
    List<dbTransaction>getTimestampBetween(OffsetDateTime from, OffsetDateTime to, Integer limit);

}
