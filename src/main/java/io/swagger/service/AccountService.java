package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import lombok.With;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*import io.swagger.Repository.AccountRepository;*/

@Service
@Transactional()
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    public dbAccount add(dbUser user, AccountType type, Double absoluteLimit){
        dbUser userDb = userRepository.findUserByUsername(user.getUsername());
        if(userDb != null){
            dbAccount account;
            if(type == AccountType.TYPE_CURRENT){
                account = createCurrentAccount(userDb, absoluteLimit);
                account = accountRepository.save(account);
                return account;
            }else if(type == AccountType.TYPE_SAVING){
                account = createSavingAccount(userDb, absoluteLimit);
                account = accountRepository.save(account);
                return account;

            }
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect Input");
    }

    public dbAccount EditAccountAbsoluteLimit(EditAbsoluteLimit editAbsoluteLimit){
        dbAccount dbAccount = findAccountByIban(editAbsoluteLimit.getIban());
        if (dbAccount != null){
            dbAccount.setAbsoluteLimit(editAbsoluteLimit.getAbsoluteLimit());
            return dbAccount;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Absolute limit cannot be changed");
    }

    public dbAccount CreateAccount(InsertAccount account){
        dbUser user = userService.getUserById(account.getUserId());
        if(user != null){
            dbAccount dbAccount = new dbAccount();
            dbAccount.setUser(user);
            if(account.getAccountType().equals("TYPE_CURRENT")) {
                AccountType accountType = AccountType.TYPE_CURRENT;
                dbAccount.setAccountType(accountType);
            }else if(account.getAccountType().equals("TYPE_SAVING")){
                AccountType accountType = AccountType.TYPE_SAVING;
                dbAccount.setAccountType(accountType);
            }
            //dbAccount accountAdded = add(user, account.getAccountType(), account.getAbsoluteLimit());
            dbAccount = add(user, account.getAccountType(), account.getAbsoluteLimit());
            return dbAccount;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Account GetAccountByIban(String Iban){
        dbUser user = getUserAuthentication();
        dbAccount dbAccount = findAccountByIban(Iban);
        if (dbAccount != null){
            if (user.getRole() == UserRole.ROLE_EMPLOYEE || dbAccount.getUser() == user) {
                User userDto = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, userDto);
                return account;
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed");
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Account> GetAllAccountsByCustomer(){
        List<Account> response = new ArrayList<>();
        dbUser dbUser = getUserAuthentication();
        List<dbAccount> allAccounts = getAllAccountsByUser(dbUser);
        if (allAccounts.size() > 0) {
            for (dbAccount dbAccount : allAccounts){
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                response.add(account);
            }
            return response;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }


    public void CloseAccount(String IBAN){
        dbUser user = getUserAuthentication();
        dbAccount dbAccount = findAccountByIban(IBAN);
        if (user.getRole() == UserRole.ROLE_EMPLOYEE || dbAccount.getUser() == user) {
            deleteAccount(IBAN);
        }
       throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed");
    }

    public ReturnBalance GetBalanceByIban(String Iban){
        dbUser user = getUserAuthentication();
        dbAccount account = findAccountByIban(Iban);

        if (user.getRole() == UserRole.ROLE_EMPLOYEE || account.getUser() == user) {
            ReturnBalance balance = new ReturnBalance();
            balance.setIBAN(account.getIban());
            balance.setAccountType(account.getAccountType());
            balance.setBalance(account.getBalance());
            return balance;
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
    }

    public List<Account> GetAccounts(Integer limit, String username){
        List<dbAccount> dbAccounts = employeeGetAllAccounts();
        List<Account> accounts = new ArrayList<>();

        for (dbAccount dbAccount : dbAccounts) {
            User user = setUserFromDTO(dbAccount);
            Account account = setAccountFromDb(dbAccount, user);

            if (username != null && !dbAccount.getUser().getUsername().equals(username)) {
                continue;
            }

            accounts.add(account);

            if (limit != null && accounts.size() >= limit) {
                break;
            }
        }

        return accounts;
    }

    public Deposit DepositMoney(String Iban, Double amount){
        dbUser user = getUserAuthentication();
        dbAccount account = findAccountByIban(Iban);
        if (account.getUser().getUsername().equals(user.getUsername())){
            Deposit updatedAccount = deposit(Iban, amount);
            return updatedAccount;
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
    }


    public Withdrawal WithdrawMoney(String Iban, Double amount) {
        dbUser user = getUserAuthentication();
        dbAccount account = findAccountByIban(Iban);

        if (!account.getUser().getUsername().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        if (account.getBalance() < amount) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance too low");
        }

        return withdraw(Iban, amount);
    }

    public Account setAccountFromDb(dbAccount dbAccount, User user){
        Account account = new Account();
        account.setAccountType(dbAccount.getAccountType());
        account.setUser(user);
        account.setIban(dbAccount.getIban());
        account.setAbsoluteLimit(dbAccount.getAbsoluteLimit());
        account.setBalance(dbAccount.getBalance());
        return account;
    }

    public User setUserFromDTO(dbAccount dbAccount){
        User user = new User();
        dbUser dbUser = dbAccount.getUser();
        user.setEmail(dbUser.getEmail());
        user.setFirstName(dbUser.getFirstName());
        user.setId(dbUser.getId());
        user.setLastName(dbUser.getLastName());
        user.setPhone(dbUser.getPhone());
        user.setTransactionLimit(dbUser.getTransactionLimit());
        user.setUsername(dbUser.getUsername());
        user.setDayLimit(dbUser.getDayLimit());
        user.setRole(dbUser.getRole().toString());

        return user;
    }

    public dbUser getUserAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getdbUserByUserName(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }
        return user;
    }


    public dbAccount addBankDefault(dbUser user){
        dbUser userDb = userRepository.findUserByUsername(user.getUsername());
        dbAccount account = createAccount(userDb, 0.0);
        account.setIban("NL01INHO0000000001");
        account.setAccountType(AccountType.TYPE_BANK);
        accountRepository.save(account);

        return account;
    }
    public dbAccount findAccountByIban(String iban){
        return accountRepository.findAccountByIban(iban);
    }


    public String generateIban() {
        Random rand = new Random();
        final String PREFIX = "NL";
        final String MIDFIX = "INHO";
        StringBuilder iban = new StringBuilder(PREFIX);

        for (int i = 0; i < 2; i++) {
            int x = rand.nextInt(10);
            iban.append(x);
        }

        iban.append(MIDFIX);

        for (int i = 0; i < 10; i++) {
            int x = rand.nextInt(10);
            iban.append(x);
        }

        return iban.toString();
    }

    public List<dbAccount> getAllAccountsByUser(dbUser dbUser){
        return accountRepository.getAccountsByUser(dbUser);
    }

    public dbAccount createCurrentAccount(dbUser user, Double limit){
        dbAccount currentAccount = createAccount(user, limit);
        currentAccount.setAccountType(AccountType.TYPE_CURRENT);

        return currentAccount;
    }

    public dbAccount createSavingAccount(dbUser user, Double limit){
        dbAccount savingAccount =  createAccount(user, limit);
        savingAccount.setAccountType(AccountType.TYPE_SAVING);
        return savingAccount;
    }

    public dbAccount createAccount(dbUser user, Double limit) {
        dbAccount account = new dbAccount();
        account.setBalance(0);
        account.setUser(user);
        account.setAbsoluteLimit(limit);

        String iban = generateUniqueIban();
        account.setIban(iban);

        return account;
    }

    private String generateUniqueIban() {
        String iban;
        boolean duplicatedIban;

        do {
            iban = generateIban();
            duplicatedIban = accountRepository.getAllIban().contains(iban);
        } while (duplicatedIban);

        return iban;
    }

    public List<dbAccount> getAllAccounts() {
        return (List<dbAccount>) accountRepository.findAll();
    }

    public List<dbAccount> employeeGetAllAccounts(){
        return (List<dbAccount>) accountRepository.getAllAccounts();
    }

    public void deleteAccount(String IBAN){
        dbAccount dbAccount = accountRepository.findAccountByIban(IBAN);
        accountRepository.delete(dbAccount);
    }

    public dbAccount getBalance(String IBAN){
        return accountRepository.getBalanceByIban(IBAN);
    }

    private Withdrawal withdraw(String IBAN, double amount) {
        dbAccount account = findAccountByIban(IBAN);
        double newBalance = account.getBalance() - amount;

        if (newBalance < account.getAbsoluteLimit()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient balance");
        }
        account.setBalance(newBalance);
        accountRepository.updateBalance(newBalance, IBAN);
        return new Withdrawal(IBAN, newBalance);
    }


    private Deposit deposit(String IBAN, double amount) {
        dbAccount account = findAccountByIban(IBAN);

        if (account.getAccountType() != AccountType.TYPE_CURRENT) {
           throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account must be of type current");
        }
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountRepository.updateBalance(newBalance, IBAN);
        return new Deposit(IBAN, newBalance);
    }

}
