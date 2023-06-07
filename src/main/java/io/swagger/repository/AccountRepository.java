package io.swagger.repository;

import io.swagger.model.dbAccount;
import io.swagger.model.dbUser;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<dbAccount, Long>, JpaSpecificationExecutor<dbAccount> {
    @Query("SELECT iban FROM dbAccount ")
    List<String> getAllIban();
    @Query("SELECT a FROM dbAccount a WHERE NOT (ID = 1)")
    List<dbAccount> getAllAccounts();
    List<dbAccount> getAccountsByUser(dbUser user);
    dbAccount getBalanceByIban(String IBAN);
    dbAccount findAccountByIban(String IBAN);
    @Transactional
    @Modifying
    @Query("update dbAccount a set a.balance = ?1 where a.iban = ?2")
    void updateBalance(Double amount, String IBAN);
}
