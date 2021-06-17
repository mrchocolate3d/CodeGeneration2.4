package io.swagger.service;

import io.swagger.model.Transaction;
import io.swagger.model.dbTransaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

//    getting transaction by iban
    public List<dbTransaction>getTransactionByIBAN(String IBAN){
        return (List<dbTransaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }


    //TODO: complete this
    //getting all transactions
    public List<dbTransaction> getTransactions(){
        return (List<dbTransaction>) transactionRepository.findAll();

    }









    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    public Integer CountAllTransactions(){
//        return transactionRepository.CountAllTransactions();
        return 0;
    }

    // transaction is dto and object is passed to the dbtransaction..


}
