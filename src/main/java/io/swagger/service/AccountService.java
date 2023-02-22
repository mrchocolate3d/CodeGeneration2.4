package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

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


    public dbAccount addBankDefault(dbUser user){
        dbUser userDb = userRepository.findUserByUsername(user.getUsername());
        dbAccount account = createAccount(userDb);
        account.setIban("NL01INHO0000000001");
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

    public Withdrawal withdraw(String IBAN, double amount) throws Exception {
        dbAccount account = getAccountByIban(IBAN);
        double newBalance = account.getBalance() - amount;
        if (newBalance < account.getAbsoluteLimit()){
            throw new Exception("Insufficient balance");
        }
        else{
            account.setIban(IBAN);
            account.setBalance(newBalance);
            accountRepository.updateBalance(newBalance, IBAN);
            return new Withdrawal(IBAN, newBalance);
        }
    }

    public Deposit deposit(String IBAN, double amount) throws Exception{
        dbAccount account = getAccountByIban(IBAN);
        double newBalance = account.getBalance() + amount;
        if (account.getAccountType() != AccountType.TYPE_CURRENT){
            throw new Exception("Account must be type current");
        }
        else{
            account.setIban(IBAN);
            account.setBalance(newBalance);
            accountRepository.updateBalance(newBalance, IBAN);
            return new Deposit(IBAN, newBalance);
        }
    }

}
