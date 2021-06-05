package io.swagger.service;

import io.swagger.model.dbTransaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.OffsetDateTime;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        //this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

//    getting transaction by iban
    public List<dbTransaction>getTransactionByIBAN(String IBAN){
        return (List<dbTransaction>)transactionRepository.getTransactionsByIBAN(IBAN);
    }


    //TODO: complete this
    //getting all transactions
    public List<dbTransaction> getTransactions(String IBAN, OffsetDateTime dateFrom, OffsetDateTime dateTo, Integer limit){

        return (List<dbTransaction>)transactionRepository.findAll();

    }

    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    public Integer CountAllTransactions(){
        //return transactionRepository.CountAllTransactions();

        return 0;
    }




}
