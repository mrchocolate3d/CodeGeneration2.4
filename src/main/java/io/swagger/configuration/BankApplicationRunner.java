package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
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
import java.util.ArrayList;
import java.util.List;

@Log
@Component
public class BankApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;
   // @Autowired
   // private AccountService accountService;

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

        dbUser user = userService.addUser(new dbUser("test", "test", "test", "test",
                "02348789347", passwordEncoder.encode("test"), List.of(UserRole.ROLE_EMPLOYEE),
                2500));

       // dbAccount account = accountService.add(user, AccountType.TYPE_CURRENT);

        dbTransaction dbTransaction = new dbTransaction("Test","NL10INH0000000000","NL20INH0000000000",700.00, transactionService.getDateToString());
        dbTransaction dbTransaction2 = new dbTransaction("Test","NL30INH0000000000","NL20INH0000000000",600.00, transactionService.getDateToString());
        transactionRepository.save(dbTransaction);
        transactionRepository.save(dbTransaction2);








    }
}
