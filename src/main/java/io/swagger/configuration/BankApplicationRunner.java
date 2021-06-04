package io.swagger.configuration;

import io.swagger.model.UserRole;
import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@ConditionalOnProperty(prefix = "swagger.autorun", name = "enabled", havingValue = "true", matchIfMissing = true)
@Transactional
public class BankApplicationRunner implements ApplicationRunner {

    //private UserService userServiceImplement;
    private UserRepository userRepository;

    public BankApplicationRunner(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*dbUser user = new dbUser("test", "test", "test", "test", "test", "test", List.of(UserRole.ROLE_EMPLOYEE), 2500);
        String token = userServiceImplement.add(user.getFirstName(), user.getLastName(),
                user.getUsername(),user.getEmail(),user.getPhone(),user.getPassword(),user.getRoles(),user.getTransactionLimit());

        System.out.println("Token: " + token);
        System.out.println(token.length());*/

        dbUser user = new dbUser("test", "test", "test", "test", "test", "test", List.of(UserRole.ROLE_EMPLOYEE), 2500);
        userRepository.save(user);
        //System.out.println("Quantity: " + user);

        userRepository.findAll().forEach(System.out::println);


    }
}
