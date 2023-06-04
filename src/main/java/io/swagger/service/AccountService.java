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


    public dbAccount add(dbUser user, AccountType type){
        dbUser userDb = userRepository.findUserByUsername(user.getUsername());
        if(userDb != null){
            dbAccount account;
            if(type == AccountType.TYPE_CURRENT){
                account = createCurrentAccount(userDb);
                account = accountRepository.save(account);
                return account;
            }else if(type == AccountType.TYPE_SAVING){
                account = createSavingAccount(userDb);
                account = accountRepository.save(account);
                return account;

            }
        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "something is wrong idk");
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
            dbAccount accountAdded = add(user, account.getAccountType());
            return accountAdded;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public Account GetAccountByIban(String Iban){
        dbUser user = getDbUserAuthenticationInfor();
        dbAccount dbAccount = getAccountByIban(Iban);
        if (dbAccount != null){
            if (user.getRole() == UserRole.ROLE_EMPLOYEE || dbAccount.getUser() == user) {
                User userDto = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, userDto);
                return account;
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public List<Account> GetAllAccountsByCustomer(){
        List<Account> response = new ArrayList<>();
        dbUser dbUser = getDbUserAuthenticationInfor();
        List<dbAccount> dbAccounts = getAllAccountsByCustomer(dbUser);
        if (dbAccounts.size() > 0) {
            for (dbAccount dbAccount : dbAccounts){
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                response.add(account);
            }
            return response;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void CloseAccount(String IBAN){
        dbUser user = getDbUserAuthenticationInfor();
        dbAccount dbAccount = getAccountByIban(IBAN);
        if (user.getRole() == UserRole.ROLE_EMPLOYEE || dbAccount.getUser() == user) {
            closeAccount(IBAN);
        }
       throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    public ReturnBalance GetBalanceByIban(String Iban){
        dbUser user = getDbUserAuthenticationInfor();
        dbAccount account = getAccountByIban(Iban);

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
        if (limit == null && username == null) {
            for (dbAccount dbAccount : dbAccounts) {
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                accounts.add(account);
            }
        }
        else if (limit != null && username == null){
            int count = 0;
            for (dbAccount dbAccount : dbAccounts) {
                if (count >= limit){
                    break;
                }
                User user = setUserFromDTO(dbAccount);
                Account account = setAccountFromDb(dbAccount, user);
                accounts.add(account);
                count++;
            }
        }
        else if (username != null && limit == null){
            for(dbAccount dbAccount : dbAccounts){
                if(dbAccount.getUser().getUsername().equals(username)){
                    User user = setUserFromDTO(dbAccount);
                    Account account = setAccountFromDb(dbAccount, user);
                    accounts.add(account);
                }
                continue;
            }
        }
        else if (username != null && limit != null){
            int count = 0;

            for (dbAccount dbAccount : dbAccounts) {
                if (count >= limit){
                    break;
                }
                if(dbAccount.getUser().getUsername().equals(username)){
                    User user = setUserFromDTO(dbAccount);
                    Account account = setAccountFromDb(dbAccount, user);
                    accounts.add(account);
                }
                count++;
            }
        }

        return accounts;
    }

    public Deposit DepositMoney(String Iban, Double amount){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userService.getdbUserByUserName(auth.getName());
        dbAccount account = getAccountByIban(Iban);
        if (account.getUser().getUsername().equals(user.getUsername())){
            Deposit updatedAccount = deposit(Iban, amount);
            return updatedAccount;
        }

        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
    }

    public Withdrawal WithdrawMoney(String Iban, Double amount){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userService.getdbUserByUserName(auth.getName());
        dbAccount account = getAccountByIban(Iban);
        if (account.getUser().getUsername().equals(user.getUsername())) {
            if(account.getBalance() < amount){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Balance too low");
            }
            Withdrawal updatedAccount = withdraw(Iban, amount);
            return updatedAccount;
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
    }

    public Account setAccountFromDb(dbAccount dbAccount, User user){
        Account account = new Account();
        account.setAccountType(dbAccount.getAccountType());
        account.setUser(user);
        account.setIban(dbAccount.getIban());
        account.setAbsoluteLimit(dbAccount.getAbsoluteLimit());
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

    public dbUser getDbUserAuthenticationInfor() {
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
        dbAccount account = createAccount(userDb);
        account.setIban("NL01INHO0000000001");
        account.setAccountType(AccountType.TYPE_BANK);
        accountRepository.save(account);

        return account;
    }
    public dbAccount getAccountByIban(String iban){
        return accountRepository.findAccountByIban(iban);
    }



    public String generateIban(){
        Random rand = new Random();
        String PREFIX  = "NL";
        String MIDFIX  = "INHO";
        String iban = PREFIX;


        for(int i = 0; i < 2; i++){
            int x = rand.nextInt(10);
            iban += Integer.toString(x);
        }
        iban += MIDFIX;
        for(int i = 0; i < 10; i ++){
            int x = rand.nextInt(10);
            iban += x;
        }


        return iban;
    }

    public List<dbAccount> getAllAccountsByCustomer(dbUser dbUser){
        return accountRepository.getAccountsByUser(dbUser);
    }

    public dbAccount createCurrentAccount(dbUser user){
        dbAccount currentAccount = createAccount(user);
        currentAccount.setAccountType(AccountType.TYPE_CURRENT);

        return currentAccount;
    }

    public dbAccount createSavingAccount(dbUser user){
        dbAccount savingAccount =  createAccount(user);
        savingAccount.setAccountType(AccountType.TYPE_SAVING);
        return savingAccount;
    }

    public dbAccount createAccount(dbUser user){
        dbAccount account = new dbAccount();
        account.setBalance(0);
        account.setUser(user);
        String iban = generateIban();
        Boolean duplicatedIban = true;
        while(duplicatedIban){
            List<String> allIbans = accountRepository.getAllIban();
            if(allIbans.contains(iban)){
                iban = generateIban();
            }else{
                duplicatedIban = false;
            }
        }

        account.setIban(iban);
        return account;
    }
    public List<dbAccount> getAllAccounts() {
        return (List<dbAccount>) accountRepository.findAll();
    }

    public List<dbAccount> employeeGetAllAccounts(){
        return (List<dbAccount>) accountRepository.getAllAccounts();
    }

    public void closeAccount(String IBAN){
        dbAccount dbAccount = accountRepository.findAccountByIban(IBAN);
        accountRepository.delete(dbAccount);
    }

    public dbAccount getBalance(String IBAN){
        return accountRepository.getBalanceByIban(IBAN);
    }

    private Withdrawal withdraw(String IBAN, double amount) {
        dbAccount account = getAccountByIban(IBAN);
        double newBalance = account.getBalance() - amount;
        if (newBalance < account.getAbsoluteLimit()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient balance");
        }
        else{
            account.setIban(IBAN);
            account.setBalance(newBalance);
            accountRepository.updateBalance(newBalance, IBAN);
            return new Withdrawal(IBAN, newBalance);
        }
    }

    private Deposit deposit(String IBAN, double amount){
        dbAccount account = getAccountByIban(IBAN);
        double newBalance = account.getBalance() + amount;
        if (account.getAccountType() != AccountType.TYPE_CURRENT){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Account must be type current");
        }
        else{
            account.setIban(IBAN);
            account.setBalance(newBalance);
            accountRepository.updateBalance(newBalance, IBAN);
            return new Deposit(IBAN, newBalance);
        }
    }

}
