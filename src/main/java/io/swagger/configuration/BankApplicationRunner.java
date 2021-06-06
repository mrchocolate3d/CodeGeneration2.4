package io.swagger.configuration;

import io.swagger.model.AccountType;
import io.swagger.model.UserRole;
import io.swagger.model.dbAccount;
import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dbUser user = new dbUser("test", "test", "test", "test", "test", "test", List.of(UserRole.ROLE_EMPLOYEE), 2500);
        String token = userService.add(user.getFirstName(), user.getLastName(),
                user.getUsername(),user.getEmail(),user.getPhone(),user.getPassword(),user.getRoles(),user.getTransactionLimit());

        System.out.println("Token: " + token);
        System.out.println(token.length());

        String tokenLogin = userService.login(user.getUsername(), user.getPassword());
        System.out.println("Token login: " + tokenLogin);


        dbUser testUser = userService.findUserByUsername(user.getUsername());

        dbAccount account = accountService.add(testUser, AccountType.TYPE_CURRENT);


        System.out.println(testUser);




    }
}
