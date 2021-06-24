package io.swagger.repository;

import io.swagger.model.dbAccount;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<dbAccount, Long> {

    @Query("SELECT iban FROM dbAccount ")
    List<String> getAllIban();
    dbAccount findAccountByIban(String IBAN);
    dbAccount deleteAccountByUserId(Long id);
    dbAccount getBalanceByIban(String IBAN);

    @Transactional
    @Modifying
    @Query("update dbAccount a set dbAccount.balance = ?1 where dbAccount.iban = ?2")
    void updateBalance(Double amount, String IBAN);
}
