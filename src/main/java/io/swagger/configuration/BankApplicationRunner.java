package io.swagger.configuration;

import io.swagger.dto.UserDTO;
import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
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
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //User user = new User("test", "test", "test", "test", "test", "test", List.of(UserRole.ROLE_EMPLOYEE), 2500);
        //String token = userService.add(user.getFirstName(), user.getLastName(),
              //  user.getUsername(),user.getEmail(),user.getPhone(),user.getPassword(),user.getRoles(),user.getTransactionLimit());



        //System.out.println("Token: " + token);
        //System.out.println(token.length());

       // String tokenLogin = userService.login(user.getUsername(), user.getPassword());
       // System.out.println("Token login: " + tokenLogin);

      //  String = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        userRepository.findAll().forEach(System.out::println);

    }
}
