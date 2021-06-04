package io.swagger.repository;

import io.swagger.model.dbAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface AccountRepository extends CrudRepository<dbAccount,Long> {
    dbAccount findAccountByIban(String iban);
}
