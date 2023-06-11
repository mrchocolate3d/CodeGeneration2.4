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

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<dbTransaction,Long>, JpaSpecificationExecutor<dbTransaction> {

    @Query("SELECT amount FROM dbTransaction t WHERE t.IBANfrom =?3 AND  t.timestamp BETWEEN ?1 AND ?2")
    List<Double> getTransactionAmountByAccountAndDate(LocalDateTime startOfDay, LocalDateTime endOfDay, String IBANFrom);
    static Specification<dbTransaction> beforeDate(LocalDate toDate){
        return (trans, cq, cb) -> cb.lessThanOrEqualTo(trans.get("timestamp"), toDate.plusDays(1).atStartOfDay());
    }
    static Specification<dbTransaction> afterDate(LocalDate fromDate){
        return (trans, cq, cb) -> cb.greaterThanOrEqualTo(trans.get("timestamp"), fromDate.atStartOfDay());
    }
    static Specification<dbTransaction> hasIbanFrom(String Iban){
        return (trans, cq, cb) -> cb.equal(trans.get("IBANfrom"), Iban);
    }
    static Specification<dbTransaction> hasIbanTo(String Iban){
        return (trans, cq, cb) -> cb.equal(trans.get("IBANto"), Iban);
    }
}
