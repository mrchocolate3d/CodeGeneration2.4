package io.swagger.service;

import io.swagger.model.*;
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
import java.util.*;

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

    public List<User> getUsers() {
        List<dbUser>  dbUsers = (List<dbUser>) userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        for (dbUser x : dbUsers) {
            User u = new User(x.getId(),x.getUsername(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getPhone(),x.getTransactionLimit());
            allUsers.add(u);
        }

        return allUsers;
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

