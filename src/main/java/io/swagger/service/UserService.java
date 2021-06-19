package io.swagger.service;

import io.swagger.dto.UserDTO;
import io.swagger.model.User;
import io.swagger.model.dbUser;
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

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public User addUser(User user) {
        userRepository.save(user);
        return user;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }

    public Optional<UserDTO> getUser(long id){
        return userRepository.findById(id);
    }


    public List<dbUser> getUsers() {
        return (List<dbUser>) userRepository.findAll();
    }

    public String login(String username, String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findUserByUsername(username).getRoles());
        }catch(AuthenticationException ae){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invallid credentials");
        }
    }
}

