package io.swagger.service;

import io.swagger.model.*;
/*import io.swagger.Repository.AccountRepository;*/
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@Transactional()
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(propagation=Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public dbAccount add(dbUser user, AccountType type){
        if(userRepository.findUserByUsername(user.getUsername()) != null){
            dbAccount account;
            if(type == AccountType.TYPE_CURRENT){
                account = createCurrentAccount(user);
                accountRepository.save(account);
                return account;
            }else if(type == AccountType.TYPE_SAVING){
                account = createSavingAccount(user);
                accountRepository.save(account);
                return account;

            }


        }
        throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "something is wrong idk");
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
        account.setBalance(0.00);
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

    public dbAccount getSpecificAccountByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    public dbAccount closeAccount(dbAccount dbAccount){
        return accountRepository.deleteAccountByUserId(dbAccount.getUser().getId());
    }

    public dbAccount getBalance(dbAccount dbAccount){
        return accountRepository.getBalanceByIban(dbAccount.getIban());
    }

    public void withdraw(String iban, double amount) throws Exception {
        dbAccount dbAccount = getSpecificAccountByIban(iban);
        double newBalance;
        if (dbAccount.getBalance() > amount){
            newBalance = dbAccount.getBalance() - amount;
            accountRepository.updateBalance(newBalance, iban);
        }
        else{
            throw new Exception("Insufficient balance");
        }
    }

    public void deposit(String iban, double amount) throws Exception{
        dbAccount dbAccount = getSpecificAccountByIban(iban);
        double newBalance = dbAccount.getBalance() + amount;
        accountRepository.updateBalance(newBalance, dbAccount.getIban());
    }
}
