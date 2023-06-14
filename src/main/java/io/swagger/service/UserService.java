package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private TransactionService transactionService;


    private dbUser addUser(dbUser user) {
        userRepository.save(user);
        return user;
    }

    public dbUser getUserById(Long id){
        Optional<dbUser> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User does not exist");
        }

    }

    public User CreateUser(InsertUser body){
        dbUser userFromDB = getLoginUser();
        List<User> userList = getUsers();
        if (userList.stream().anyMatch((user) -> user.getUsername().equals(body.getUsername()))) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        dbUser user;
        if(body.getRole().toUpperCase() == "ROLE_EMPLOYEE" && userFromDB.getRole() == UserRole.ROLE_EMPLOYEE){
            user = new dbUser(body.getFirstName(), body.getLastName(), body.getUsername(), body.getEmail(),body.getPhone(), passwordEncoder.encode(body.getPassword()), UserRole.ROLE_EMPLOYEE, body.getTransactionLimit(), body.getDayLimit());
        }
        else{
            user = new dbUser(body.getFirstName(), body.getLastName(), body.getUsername(), body.getEmail(),body.getPhone(), passwordEncoder.encode(body.getPassword()), UserRole.ROLE_CUSTOMER, 0, 0);
        }
        addUser(user);
        return convertDbUserToUser(user);
    }

    public List<ReturnLimitAndRemainingAmount> GetUserRemainingAmount(){
        dbUser userFromDB = getLoginUser();

        List<ReturnLimitAndRemainingAmount> responses = new ArrayList<ReturnLimitAndRemainingAmount>();

        if(userFromDB != null){
            for (dbAccount acc : userFromDB.getAccounts()){
                ReturnLimitAndRemainingAmount response = new ReturnLimitAndRemainingAmount();
                response.setIBAN(acc.getIban());
                response.setLimit(userFromDB.getDayLimit());
                System.out.println(transactionService.GetTotalTransactionAmountByAccountAndDate(LocalDateTime.now().toLocalDate(), acc.getIban()));
                response.setRemainAmount(userFromDB.getDayLimit() - transactionService.GetTotalTransactionAmountByAccountAndDate(LocalDateTime.now().toLocalDate(), acc.getIban()));
                response.setAccountType(acc.getAccountType());

                responses.add(response);
            }
        }
        return responses;
    }

    public User GetOwnedUserData(){
        dbUser userFromDB = getLoginUser();

        if (userFromDB != null) {
            User u = convertDbUserToUser(userFromDB);
            return u;
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed");

    }

    private dbUser getLoginUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = getdbUserByUserName(auth.getName());
        return user;
    }

    public List<dbUser> getAlldbUsers(){
        return (List<dbUser>) userRepository.findAll();
    }

    public User convertDbUserToUser(dbUser x){
        return new User(x.getId(), x.getUsername(), x.getFirstName(), x.getLastName(), x.getEmail(), x.getRole().toString(), x.getPhone(),x.getTransactionLimit(), x.getDayLimit());
    }


    public dbUser getdbUserByUserName(String username){
        return userRepository.findUserByUsername(username);
    }

    public List<User> getUsersWithParameters(String name, Integer limit ) {
        List <User> users = new ArrayList<>();
        int count;
        if (name != null){
            users.add(convertDbUserToUser(getdbUserByUserName(name)));
            return users;
        } else if (name == null && limit != null) {
            count = 0;
            List<dbUser> dbUsers = (List<dbUser>) userRepository.findAll();
            for (dbUser x : dbUsers) {
                users.add(convertDbUserToUser(x));
                count++;
                if (limit == count) {
                    break;
                }
            }
            return users;
        }
        else {
            List<dbUser>  dbUsers = (List<dbUser>) userRepository.findAll();
            for (dbUser x : dbUsers) {
                users.add(convertDbUserToUser(x));
            }
            return users;
        }

    }

    public List<User> getUsers() {
            List <User> users = new ArrayList<>();
            List<dbUser>  dbUsers = (List<dbUser>) userRepository.findAll();
            for (dbUser x : dbUsers) {
                users.add(convertDbUserToUser(x));
            }
            return users;

    }

    public List<dbUser> getUsersOfDb() {
        return (List<dbUser>) userRepository.findAll();
    }

    public User EditUser(long id, EditUser body){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = getdbUserByUserName(auth.getName());
        if(user.getRole() == UserRole.ROLE_EMPLOYEE){
            dbUser userToChange = getUserById(id);
            if(userToChange.getRole() == UserRole.ROLE_BANK)
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            editDbUser(userToChange, body);
        }
        if (user != null) {
            //not allow user to change limit
            body.setDayLimit(user.getDayLimit());
            body.setTransactionLimit(user.getTransactionLimit());
            editDbUser(user, body);
        }
        return convertDbUserToUser(user);
    }

    private dbUser editDbUser(dbUser oldUser, EditUser newUser){
        oldUser.setFirstName(newUser.getFirstName());
        oldUser.setLastName(newUser.getLastName());
        if(newUser.getPassword() != null){
            oldUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        }
        oldUser.setEmail(newUser.getEmail());
        oldUser.setPhone(newUser.getPhone());
        if(newUser.getTransactionLimit() != null){
            oldUser.setTransactionLimit(newUser.getTransactionLimit());
        }
        if(newUser.getDayLimit() != null){
            oldUser.setDayLimit(newUser.getDayLimit());
        }
        userRepository.save(oldUser);
        return oldUser;
    }

    public void deleteUser(long id) {
        dbUser userToDelete = getUserById(id);
        if(userToDelete != null && userToDelete.getRole() == UserRole.ROLE_BANK)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        if(userToDelete == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");

        userRepository.deleteById(id);

    }

    public String login(String username, String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, Collections.singletonList(userRepository.findUserByUsername(username).getRole()));
        }catch(AuthenticationException ae){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid credentials");
        }
    }

    public List<User> GetUserWithNoAccount() {
        dbUser userLoggedIn = getLoginUser();
        List<User> users = new ArrayList<>();
        if(userLoggedIn.getRole() == UserRole.ROLE_EMPLOYEE) {
            for (dbUser user : userRepository.GetUserWithNoAccount()) {
                User u = convertDbUserToUser(user);
                users.add(u);
            }
            return users;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed");
    }
}

