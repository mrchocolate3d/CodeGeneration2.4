package io.swagger.service;

import com.sun.xml.bind.v2.TODO;
import io.swagger.model.Account;
import io.swagger.model.Transaction;
import io.swagger.model.dbAccount;
import io.swagger.model.dbTransaction;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import javax.validation.constraints.Null;
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
//    public List<dbTransaction>getTransactionByIBAN(String IBAN){
//        return (List<dbTransaction>)transactionRepository.getTransactionsByIBAN(IBAN);
//    }


    //TODO: complete this
    //getting all transactions
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int limit){

        //do all checks here




        return (List<dbTransaction>) transactionRepository.findAll();

    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        // TODO: 17/06/2021
        //checking if its the right user
        //check if user has the right to access accounts
        //check daily or transaction limit
        //check if its savings or current account
        //make sure no one can transfer from savings to another user' current account


        //getting account from and account to
        dbAccount IBANfrom = accountRepository.getBalanceByIban(transaction.getIBANfrom());
        dbAccount IBANto = accountRepository.getBalanceByIban(transaction.getIBANto());

        //getting amount







        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }
    public Integer CountAllTransactions(){
//        return transactionRepository.CountAllTransactions();
        return 0;
    }

    // transaction is dto and object is passed to the dbtransaction..

    //all these checks are in the post transactions
    //check the right user
    //check if the user has the right permission to access accounts
    //check daily limit or transaction limit
    //check if its a savings or current account
    //make sure no one can transfer from savings account to someone' elses account


}
