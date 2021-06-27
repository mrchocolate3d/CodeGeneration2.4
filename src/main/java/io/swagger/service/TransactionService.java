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

    //TODO: complete this
    //getting all transactions
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int dayLimit){
        //implementing the limit //has to be pageable
        Integer page = 1;
        Pageable p = PageRequest.of(page, dayLimit);

        dbUser user = null;
        user = userRepository.findUserByUsername(user.getUsername());
        List<dbTransaction> transactions = new ArrayList<>();

//        List<dbAccount> accounts = findAccountByUserId(user);
//        dbAccount account = accountRepository.findAccountByIban(IBAN);
//        accounts.add(account);
//
//        for (dbAccount a : accounts){
//            transactions.addAll((Collection<? extends dbTransaction>) transactionRepository.getTransactionsByIBANfrom(a.getIban()));
//        }

        return transactions;
    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        setTransactionsFromDb(transaction);
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }

    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setTime(dbTransaction.getTimestamp());
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        transaction.setIbANTo(dbTransaction.getIBANto());
        transaction.setAmount(dbTransaction.getAmount());
        transaction.setUserPerform(dbTransaction.getUserPerform());
        return transaction;
    }


    public List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
        List<dbTransaction> transactions = new ArrayList<>();


//        dbTransaction transaction = transactionRepository.getTransactionsByIBAN(IBAN);
        dbTransaction transaction = null;
        transactions.add(transaction);
        return transactions;

    }
//    public List<dbAccount> findAccountByUserId(dbUser user){
//        return accountRepository.findAccountById(user.getId());
//    }
    public List<dbTransaction> getAllTransactions(){
        return (List<dbTransaction>)transactionRepository.findAll();
    }

    public List<dbTransaction> getTransactionByIBAN(String IBAN){
        Iterable<dbTransaction> transactions = transactionRepository.getTransactionsByIBANfrom(IBAN);

        if(IBAN.isEmpty() || IBAN==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
        }
        return (List<dbTransaction>) transactions;
    }

    //check if its savings or current account
    //make sure no one can transfer from savings to another user's current account
    //saving the amount//not sure
    //check if user has the right to access accounts //not sure if its the employee or customer who dont have access to make a transaction
    public Integer CountAllTransactions(){
        return transactionRepository.CountAllTransactions();
    }

}
