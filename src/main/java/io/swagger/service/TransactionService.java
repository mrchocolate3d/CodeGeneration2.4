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
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.OffsetDateTime;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.math.BigDecimal;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository,AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }






    //check if its savings or not
    //check if account exists
    //transaction limit
    //check user
    //check origin of account
//        UpdateBalanceFrom(transaction);
//        UpdateBalanceTo(transaction);


    public Transaction setTransactionsFromDb(dbTransaction dbT){
        return new Transaction(dbT.getId(),dbT.getUserPerform(),dbT.getIBANto(),dbT.getIBANfrom(),dbT.getAmount(),dbT.getTimestamp());
    }
    public dbTransaction addTransaction(dbTransaction transaction){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        dbUser userPerforming = userRepository.findUserByUsername(auth.getName());
        //get account of logged in user
        //dbAccount loggedInUser = accountRepository.getAccountTypeByUserId(userPerforming.getId(), AccountType.TYPE_CURRENT);

        dbAccount fromAccount = accountRepository.getAccountTypeByiban(transaction.getIBANfrom(),AccountType.TYPE_CURRENT);
        dbAccount toAccount = accountRepository.getAccountTypeByiban(transaction.getIBANto(),AccountType.TYPE_CURRENT);



        //get account used for performing transactions
        //get receiver account
        //check if used account is the same
        //check if user has admin rights
        //check if iban equals admin iban

        //check if account exists

        //check if amount if nott below zero
        if(accountAmountCheck(transaction.getAmount(),transaction.getIBANto())){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Amount is below zero. Try again");
        }
        //if(isValidTransaction())
        //check if transaction stays below transaction limit

        //check if its a valid tranaction
        //check if performer's balance is not below limit
        //check transaction limit of user
        if(transaction.getAmount().compareTo(userPerforming.getTransactionLimit()) > 0){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Transaction cannot be completed because amount is higher than the transaction limit");
        }

        transactionRepository.save(transaction);
        return transaction;
    }



    public java.sql.Date getDateToString(){
        return new java.sql.Date(Calendar.getInstance().getTime().getTime());

    }

//
    public List<dbTransaction> getTransactionByIBANfrom(String IBAN){
        List<dbTransaction> getTransactionsByIBANfrom = transactionRepository.getTransactionsByIBANfrom(IBAN);
        if(IBAN.isEmpty() || IBAN == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
        }
        return getTransactionsByIBANfrom;
    }


    public List<dbTransaction> getTransactionByIBANto(String IBAN){
      List<dbTransaction> transactionsOfIBANTo = transactionRepository.getTransactionsByIBANto(IBAN);
            if(IBAN.isEmpty() || IBAN == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
            }
            return transactionsOfIBANTo;
    }


    public Integer CountAllTransactions(){
        return transactionRepository.CountAllTransactions();
    }


    //updating balance of accounts after transactions
    public void UpdateAccountBalance(Account fromAccount, Account toAccount, Double amount){
        Double accountFrom = accountRepository.getBalanceByIban(fromAccount.getIban(),fromAccount.getAccountType());
        Double accountTo = accountRepository.getBalanceByIban(toAccount.getIban(), toAccount.getAccountType());

        Double newBalanceForAccountFrom = (amount - accountFrom);
        Double newBalanceForAccountTo = (amount + accountTo);

        transactionRepository.updateAccountBalance(newBalanceForAccountFrom,fromAccount.getIban(),fromAccount.getAccountType());
        transactionRepository.updateAccountBalance(newBalanceForAccountTo,toAccount.getIban(),toAccount.getAccountType());
    }

    //checking validity of transaction
    private boolean isValidTransaction(Account fromAccount, Account toAccount){
        boolean isValid = true;

        //if iban is not the same
        if(fromAccount.getIban().equals(toAccount.getIban())){
            isValid = false;
        }
        //checking if both are current accounts
        if(fromAccount.getAccountType().equals(AccountType.TYPE_CURRENT) && toAccount.getAccountType().equals(AccountType.TYPE_CURRENT)){
            isValid = true;
        }
        return !isValid;
    }

    private void validWithdrawal(){}
    private boolean accountAmountCheck(Double amount, String fromAccount){
        boolean isAmountLessThanZero;
        if(accountRepository.findAccountByIban(fromAccount) == null){
            return true;
        }
        if(amount >= 0){
            return true;
        }
        return false;
    }

    //getting transaction limit



//
//    public void UpdateBalanceFrom(dbTransaction transaction){
//        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANfrom());
//        if(account!=null){
//            account.setBalance(account.getBalance() - transaction.getAmount());
//            accountRepository.save(account);
//        }
//    }
//    public void UpdateBalanceTo(dbTransaction transaction){
//        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANto());
//        if(account!=null){
//            account.setBalance(account.getBalance() + transaction.getAmount());
//            accountRepository.save(account);
//        }
//    }

}
//        if(transaction.getAmount() == null||transaction.getIBANto() == null||
//                transaction.getIBANfrom() == null|| transaction.getUserPerform() == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Input field missing");
//        }
//        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANfrom());
//        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANto());
//        dbUser user = userRepository.findUserByUsername(transaction.getUserPerform());
//        //...
//        if(accountFrom == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
//        }
//        else if(accountTo == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
//        }
//        dbUser userTo = userRepository.findById(accountTo.getId()).get();
//        if(accountFrom.getAccountType() == AccountType.TYPE_SAVING && userTo.getId()!=user.getId()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to a saving's account of another user");
//        }
//        else{
//            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
//            accountRepository.save(accountFrom);
//
//            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
//            accountRepository.save(accountTo);
//
//
//        }
