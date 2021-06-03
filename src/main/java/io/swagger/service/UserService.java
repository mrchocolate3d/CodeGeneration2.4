package io.swagger.service;

import io.swagger.model.User;
import io.swagger.model.UserRole;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Log
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String add(String username, String password, List<UserRole> roles) {
        if(userRepository.findUserByUsername(username) == null){
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder().encode(password));
            if(roles.size() == 0){
                user.setRoles(List.of(UserRole.ROLE_CUSTOMER,UserRole.ROLE_EMPLOYEE, UserRole.ROLE_BANK));
            }
            else{
                user.setRoles(roles);
            }
            log.info(user.toString());
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(),user.getRoles());
        }
        throw  new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,"Username is already in use");
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }
}
