package io.swagger.configuration;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
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

import javax.transaction.Transactional;
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
    PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
//        dbUser user = userRepository.save(new dbUser("test", "test", "test", "test", "test", , List.of(UserRole.ROLE_EMPLOYEE), 2500));

        dbUser user = userService.addUser(new dbUser("test", "test", "test", "test",
                "test", passwordEncoder.encode("test"), List.of(UserRole.ROLE_EMPLOYEE),
                2500));
        dbAccount account = accountService.add(user, AccountType.TYPE_CURRENT);


        userRepository.findAll().forEach(System.out::println);
        accountRepository.findAll().forEach(System.out::println);
//
//        userService.getUsers().forEach(System.out::println);
//        accountService.getAllAccounts().forEach(System.out::println);

        dbUser userDb = userService.getUserById((long) 1);
//
        System.out.println(userDb);

        dbAccount account1 = accountService.getAccountByIban(account.getIban());
        System.out.println(account1.getIban());


//        dbUser user1 = userService.getUserByUsername("test");
//        System.out.println("User test: "+ user1);



//        System.out.println(user.getUsername()+ " " + user.getPassword());
        String token = userService.login(userDb.getUsername(), "test");
        System.out.println(token);

//        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = userDetails.getUsername();
//        System.out.println(username);

        dbUser user1 = userService.getUserByUsername("test");
        System.out.println(user1);

        String iban = account1.getIban();

        dbAccount account2 = accountService.getAccountByIban(iban);
        System.out.println(account2);
    }
}
