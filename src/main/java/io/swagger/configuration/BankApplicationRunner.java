package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.threeten.bp.OffsetDateTime;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log
@Component
public class BankApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;


    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        dbUser bank = userRepository.save(new dbUser("Bank", "", "bank", "bank@bank.com", "098324567", passwordEncoder.encode("bank"), UserRole.ROLE_BANK, 50000, 5000));
        dbAccount dbBank = accountService.addBankDefault(bank);

        dbUser user = userRepository.save(new dbUser("test", "test", "test", "test",
                "test", passwordEncoder.encode("test"), UserRole.ROLE_EMPLOYEE,
                2500, 10000));


        dbAccount account = accountService.add(user, AccountType.TYPE_CURRENT, 0.0, 1000.0, 2500.0);

        accountRepository.save(account);

        dbTransaction dbTransaction = new dbTransaction(2,"NL10INH0000000000","NL20INH0000000000",700.00, LocalDateTime.now(), false);
        dbTransaction dbTransaction2 = new dbTransaction(2,"NL30INH0000000000","NL20INH0000000000",600.00, LocalDateTime.now(), false);
        dbTransaction dbTransaction3 = new dbTransaction(2,"NL10INH0000000000",account.getIban(),700.00, LocalDateTime.now(), false);


        dbTransaction dbTransaction4 = new dbTransaction(2,"NL10INH0000000000",account.getIban(),700.00, LocalDateTime.of(2023, 1, 20, 19, 0,0), false);
        dbTransaction dbTransaction5 = new dbTransaction(2,"NL10INH0000000000",account.getIban(),700.00, LocalDateTime.of(2023, 1, 23, 20,0,0),false);

        transactionRepository.save(dbTransaction);
        transactionRepository.save(dbTransaction2);
        transactionRepository.save(dbTransaction3);
        transactionRepository.save(dbTransaction4);
        transactionRepository.save(dbTransaction5);








    }
}
