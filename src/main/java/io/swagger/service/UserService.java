package io.swagger.service;

import io.swagger.model.dbUser;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public dbUser addUser(dbUser user){
        userRepository.save(user);
        return user;
    }

    public List<dbUser> getUsers(){
        return (List<dbUser>) userRepository.findAll();
    }
}
