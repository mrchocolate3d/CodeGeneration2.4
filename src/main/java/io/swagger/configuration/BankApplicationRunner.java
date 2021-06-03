package io.swagger.configuration;

import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import io.swagger.service.UserServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BankApplicationRunner implements ApplicationRunner {
    @Autowired
    private UserServiceImplement userServiceImplement;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        dbUser user = new dbUser("test", "test", "test", "test", "test", "test", List.of(UserRole.ROLE_EMPLOYEE), 2500);
        String token = userServiceImplement.add(user.getFirstName(), user.getLastName(),
                user.getUsername(),user.getEmail(),user.getPhone(),user.getPassword(),user.getRoles(),user.getTransactionLimit());

        System.out.println("Token: " + token);
        System.out.println(token.length());

    }
}