package io.swagger.service;

import io.swagger.model.InsertUser;
import io.swagger.model.User;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConvertService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public List<User> convertUserFromInsert (List<InsertUser> insertUser){
        List<User> users = new ArrayList<>();
        for (InsertUser x : insertUser){
            User user = new User(x.getId(),x.getUsername(),x.getFirstName(),x.getLastName(),x.getEmail(),x.getPhone(),x.getTransactionLimit());
            users.add(user);
        }
        return users;
    }
}

