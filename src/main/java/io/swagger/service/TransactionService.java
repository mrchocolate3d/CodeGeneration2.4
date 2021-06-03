package io.swagger.service;

import io.swagger.model.dbTransaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.List;

public class TransactionService {
    TransactionRepository transactionRepository;
    AccountRepository accountRepository;
    UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    //getting transaction by iban
    public List<dbTransaction>getTransactionByIBAN(String IBAN){
        return (List<dbTransaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }

    //getting all transactions

    public List<dbTransaction> getTransactions(String IBAN, OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer limit){


        return (List<dbTransaction>)transactionRepository.findAll();

    }
    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    //getting transactions by id
    public dbTransaction getTransactionById(long id){
        return transactionRepository.findById(id).get();
    }

    //public dbTransaction getTransactionsByID(long ID){
        //return transactionRepository.findById(ID).get();
    //}

}
