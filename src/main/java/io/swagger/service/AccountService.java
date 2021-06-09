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

    @Autowired
    private BalanceRepository balanceRepository;

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

    public dbAccount closeAccount(dbAccount account){
        return accountRepository.deleteAccountByUserID(account.getUser().getId());
    }

    public dbAccount getBalance(dbAccount account){
        return accountRepository.getBalanceByIBAN(account.getIban());
    }
    public dbAccount withdraw(dbAccount account, Double amount) throws Exception {
        accountRepository.findAccountByIban(account.getIban());
        BigDecimal withdrawAmount = BigDecimal.valueOf(account.getBalance()).subtract(BigDecimal.valueOf(amount));
        if (withdrawAmount.compareTo(BigDecimal.ZERO) < 0){
            throw new Exception("The amount to be withdrawed is higher than the balance");
        }
        else {
            accountRepository.updateBalance(amount, account.getIban());
            return accountRepository.findAccountByIban(account.getIban());
        }
    }
}
