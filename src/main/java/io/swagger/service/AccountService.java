package io.swagger.service;

import io.swagger.model.Account;
/*import io.swagger.Repository.AccountRepository;*/
import org.springframework.stereotype.Service;

import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts {
        return (List<Account>) accountRepository.findAll();
    }
}
