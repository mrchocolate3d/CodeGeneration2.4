package io.swagger.repository;

import io.swagger.model.Account;
import io.swagger.model.dbTransaction;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.threeten.bp.OffsetDateTime;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<dbTransaction,Long>, JpaSpecificationExecutor<dbTransaction> {

    @Query("SELECT amount FROM dbTransaction t WHERE t.timestamp =?1 AND t.IBANfrom =?2")
    List<Double> getTransactionAmountByAccountAndDate(LocalDate timestamp, String IBANFrom);
    Iterable<dbTransaction>getTransactionsByIBANfrom(String IBAN);
    Iterable<dbTransaction>getTransactionsByIBANto(String IBAN);
    @Query("SELECT COUNT(t) FROM dbTransaction t")
    Integer CountAllTransactions();
    @Query("SELECT t FROM dbTransaction t WHERE t.timestamp =?1 OR t.timestamp =?2 ORDER BY t.timestamp DESC")
    List<dbTransaction>getTimestampBetween(OffsetDateTime from, OffsetDateTime to, Integer limit);

    static Specification<dbTransaction> beforeDate(LocalDate toDate){
        return (trans, cq, cb) -> cb.lessThanOrEqualTo(trans.get("timestamp"), toDate);
    }

    static Specification<dbTransaction> afterDate(LocalDate fromDate){
        return (trans, cq, cb) -> cb.greaterThanOrEqualTo(trans.get("timestamp"), fromDate);
    }

    static Specification<dbTransaction> hasIbanFrom(String Iban){
        return (trans, cq, cb) -> cb.greaterThanOrEqualTo(trans.get("IBANfrom"), Iban);
    }
    static Specification<dbTransaction> hasIbanTo(String Iban){
        return (trans, cq, cb) -> cb.greaterThanOrEqualTo(trans.get("IBANto"), Iban);
    }
}
