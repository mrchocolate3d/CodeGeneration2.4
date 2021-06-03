package io.swagger.service;

import io.swagger.model.Transaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.threeten.bp.OffsetDateTime;
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
    public List<Transaction>getTransactionByIBAN(String IBAN){
        return (List<Transaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }


    //TODO: complete this
    //getting all transactions
    public List<Transaction> getTransactions(String IBAN, OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer limit){

        return (List<Transaction>)transactionRepository.findAll();

    }
    //post transaction
    public Transaction createTransaction(Transaction transaction){
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    //getting transactions by id
    public Transaction getTransactionById(long id){
        return transactionRepository.findById(id).get();
    }


}
