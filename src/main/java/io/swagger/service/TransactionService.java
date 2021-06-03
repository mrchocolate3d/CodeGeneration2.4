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

    public List<dbTransaction>getTransactionByIBAN(String IBAN){
        return (List<dbTransaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }

    public List<dbTransaction> getTransactions(String IBAN, OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer limit){
        return (List<dbTransaction>)transactionRepository.findAll();
        //not complete
    }
    public dbTransaction getTransactionsByID(long ID){
        return transactionRepository.findById(ID).get();
    }

}
