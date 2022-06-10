package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Transaction setTransactionsFromDb(dbTransaction dbT) {
        return new Transaction(dbT.getId(), dbT.getUserPerform(), dbT.getIBANto(), dbT.getIBANfrom(), dbT.getAmount(), dbT.getTimestamp());
    }

    public dbTransaction addTransaction(dbTransaction transaction) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userRepository.findUserByUsername(auth.getName());

        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANto());
        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANfrom());

        //checking if iban equals admin iban
        if (accountTo.getIban().equals("NL01INHO0000000001") || accountFrom.getIban().equals("NL01INHO0000000001")) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "You cannot make a transaction with this IBAN");
        }
        //checking is iban exists
        if (accountTo.getIban() == null || accountFrom.getIban() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "IBAN does not exist");
        }
        //checking validity of transaction
        if (isValidTransaction(accountFrom.getIban(), accountTo.getIban())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction is not valid");
        }

        //checking if account balance is 0 or less
        dbAccount accFrom = accountRepository.getBalanceByIban(transaction.getIBANfrom());
        double balanceOfAccountFrom = accFrom.getBalance();
        if(balanceOfAccountFrom <= 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Balance is low");
        }
        if(transaction.getAmount() > balanceOfAccountFrom){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount is higher than your balance. Try again");
        }

        //get the username using the userid from acc and check if it matches userPerforming string
        if(!user.getUsername().equals(transaction.getUserPerform())){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User does not exist");
        }
        if(!accountTo.getUser().equals(accountFrom.getUser())){
            if(accountFrom.getAccountType().equals(AccountType.TYPE_CURRENT) && accountTo.getAccountType().equals(AccountType.TYPE_SAVING)){
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Cannot transfer from your account to another customers savings account");
            }
        }


        // getting transaction limit
        dbUser transactionLimitOfUser = userRepository.getTransactionLimitByUsername(transaction.getUserPerform());
        double transactionLimit =  transactionLimitOfUser.getTransactionLimit();
        if(transaction.getAmount() >= transactionLimit){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction limit has been reached.");
        }
        System.out.println(transactionLimit);

        updateAccountBalance(accountTo.getIban(), accountFrom.getIban(), transaction.getAmount());
        transactionRepository.save(transaction);
        return transaction;
    }


    public java.sql.Date getDateToString() {
        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }

    public List<dbTransaction> getTransactionByIBANfrom(String IBAN) {
        List<dbTransaction> getTransactionsByIBANfrom = transactionRepository.getTransactionsByIBANfrom(IBAN);
        if (IBAN.isEmpty() || IBAN == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IBAN is not provided!");
        }
        return getTransactionsByIBANfrom;
    }


    public List<dbTransaction> getTransactionByIBANto(String iban) {
        List<dbTransaction> transactionsOfIBANTo = transactionRepository.getTransactionsByIBANto(iban);
        if (iban.isEmpty() || iban == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "IBAN is not provided!");
        }
        return transactionsOfIBANTo;
    }



    //updating balance of accounts after transactions
    public void updateAccountBalance(String IBANfrom, String IBANTo, Double amount) {

        dbAccount accountFrom = accountRepository.getBalanceByIban(IBANfrom);
        dbAccount accountTo = accountRepository.getBalanceByIban(IBANTo);
        double newBalanceForAccountFrom = accountFrom.getBalance() + amount;
        double newBalanceForAccountTo = accountTo.getBalance() - amount;
        accountRepository.updateBalance(newBalanceForAccountFrom, accountFrom.getIban());
        accountRepository.updateBalance(newBalanceForAccountTo, accountTo.getIban());
    }

    //checking validity of transaction
    private boolean isValidTransaction(String IBANfrom, String IBANto) {
        boolean isValid = true;
        //if iban is not the same
        if (IBANfrom.equals(IBANto)) {
            isValid = false;
        }
        //checking if both are current accounts
        //if iban is the same and there is current and savings // isvalid is true
        dbAccount accFrom = accountRepository.findAccountByIban(IBANfrom);
        dbAccount accTo = accountRepository.findAccountByIban(IBANto);

        if(accFrom.getIban().equals(accTo.getIban())){
            if((accFrom.getAccountType().equals(AccountType.TYPE_CURRENT) && accTo.getAccountType().equals(AccountType.TYPE_SAVING)) ||
                    (accFrom.getAccountType().equals(AccountType.TYPE_SAVING) && accTo.equals(AccountType.TYPE_CURRENT))){
                isValid = true;
            }
        }
        return !isValid;
    }


}




