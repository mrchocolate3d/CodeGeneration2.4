package io.swagger.service;

import io.swagger.api.ApiException;
import io.swagger.api.NotFoundException;
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
        Optional<dbUser> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No user in the db");
        }

    }

    public User convertDbUserToUser(dbUser x){
        return new User(x.getId(),x.getUsername(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getPhone(),x.getTransactionLimit());
    }

    public dbUser getUserByUsername(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<User> getUsers() {
        List<dbUser>  dbUsers = (List<dbUser>) userRepository.findAll();
        List<User> allUsers = new ArrayList<>();
        for (dbUser x : dbUsers) {
            allUsers.add(convertDbUserToUser(x));
        }

        return allUsers;
    }

    public void editUser(dbUser oldUser ,InsertUser newUser){
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPhone(newUser.getPhone());
        oldUser.setTransactionLimit(newUser.getTransactionLimit());

        userRepository.save(oldUser);

    }

    public void deleteUser(long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "No User with that id in the database");
        }

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

