package io.swagger.service;

import io.swagger.model.AccountType;
import io.swagger.model.UserRole;
import io.swagger.model.dbAccount;
import io.swagger.model.dbUser;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String add(String firstName, String lastName, String username, String email, String phone, String password, List<UserRole> roles, double transactionLimit) {
        if (userRepository.findUserByUsername(username) == null) {
            dbUser user = new dbUser(
                    firstName, lastName, username, email, phone, passwordEncoder.encode(password), List.of(UserRole.ROLE_EMPLOYEE, UserRole.ROLE_CUSTOMER), transactionLimit
            );
            if (roles.size() == 0) {
                user.setRoles(List.of(UserRole.ROLE_EMPLOYEE, UserRole.ROLE_CUSTOMER));
            } else {
                user.setRoles(roles);
            }



            userRepository.save(user);



            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Username already in use");
    }




    public dbUser addUser(dbUser user) {
        userRepository.save(user);
        return user;
    }

    public dbUser getUserById(Long id){
        return userRepository.findUserById(id);
    }

    public dbUser getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<dbUser> getUsers() {
        return (List<dbUser>) userRepository.findAll();
    }

    public String login(String username, String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findUserByUsername(username).getRoles());
        }catch(AuthenticationException ae){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid credentials");
        }
    }
}

