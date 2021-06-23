package io.swagger.configuration;

import io.swagger.model.InsertUser;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
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
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        InsertUser user = new InsertUser("test", "test", "test", "test", "test", "test", 2500.00,List.of(UserRole.ROLE_EMPLOYEE));
        InsertUser t = userService.addUser(user);
            //userRepository.save(user);

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        System.out.println("Token: " + token);
        System.out.println(token.length());

        //String tokenLogin = userService.login(user.getUsername(), user.getPassword());
        //System.out.println("Token login: " + tokenLogin);

        //String = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        userRepository.findAll().forEach(System.out::println);

    }
}
