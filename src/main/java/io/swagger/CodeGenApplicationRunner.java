package io.swagger;

import io.swagger.model.Account;
import io.swagger.model.AccountType;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.service.AccountService;
import io.swagger.service.LoginService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log
@Component
public class CodeGenApplicationRunner implements ApplicationRunner {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private LoginService loginService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {

        // user
        User user = new User();
        user.setUsername("test-user");
        user.setPassword("testpasswd");
        user.setRoles(List.of(UserRole.ROLE_CUSTOMER));
        String token = userService.add(user.getUsername(), user.getPassword(), user.getRoles());
        log.info("Token: " + token);

        // account
        //Account current = new Account(Account.AccountTypeEnum.CURRENTACCOUNT);
        //Account savings = new Account(Account.AccountTypeEnum.SAVINGSACCOUNT);

    }
}
