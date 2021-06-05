package io.swagger.service;

import io.swagger.model.Account;
import io.swagger.model.dbAccount;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return accountRepository.findAccountByIban(IBAN);
    }

    public dbAccount addAccounts(dbAccount account){
        return accountRepository.save(account);
    }



}
