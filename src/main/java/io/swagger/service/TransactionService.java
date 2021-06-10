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
    public List<Transaction>getTransactionByIBAN(String IBAN){
        return (List<Transaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }


    //TODO: complete this
    //getting all transactions
    public List<Transaction> getTransactions(String IBAN, OffsetDateTime fromDate, OffsetDateTime toDate, int limit){
        List<Transaction> transactions = new ArrayList<>();
        List<dbTransaction> dbTransactions = new ArrayList<>();

        transactionRepository.getTransactionsByIBAN(IBAN);
        transactionRepository.CountAllTransactions();


        for (Transaction t : transactions){

           if(fromDate == null){
               fromDate = OffsetDateTime.parse(fromDate + "T00:00:00.001+02:00");
           }
           if(toDate == null){
               toDate =  OffsetDateTime.parse(toDate + "T23:59:59.999+02:00");
           }

        }
        return transactions;

    }









    //post transaction
    public Transaction createTransaction(Transaction transaction){
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    public Integer CountAllTransactions(){
//        return transactionRepository.CountAllTransactions();
        return 0;
    }

    // transaction is dto and object is passed to the dbtransaction..


}
