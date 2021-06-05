package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.dbAccount;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    @Autowired
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<dbAccount> getAllAccounts() {
        return (List<dbAccount>) accountRepository.findAll();
    }

    public dbAccount getAccountByIBAN(String IBAN){
        dbAccount account = accountRepository.findAccountByIban(IBAN);
        if (account == null) {
            throw new NullPointerException();
        }
        return account;
    }

    // create account
    public void createAccounts(dbAccount account){
        accountRepository.save(account);
    }

    // close account
    @Modifying
    public void closeAccount(dbAccount account){
        accountRepository.save(account);
    }
}
