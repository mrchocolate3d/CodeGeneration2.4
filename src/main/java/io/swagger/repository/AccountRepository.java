package io.swagger.repository;

import io.swagger.model.dbAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends CrudRepository<dbAccount, Long> {

    @Query("SELECT iban FROM dbAccount ")
    List<String> getAllIban();

    dbAccount findAccountByIban(String iban);

}
