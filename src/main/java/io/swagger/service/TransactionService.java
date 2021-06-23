package io.swagger.service;

import com.sun.xml.bind.v2.TODO;
import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //TODO: complete this
    //getting all transactions
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int limit){

        //do all checks here
        return (List<dbTransaction>) transactionRepository.findAll();

    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        // TODO: 17/06/2021
        //getting the right user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userRepository.findUserByUsername(authentication.getName());

        //check if user has the right to access accounts
        if(user.getRoles().contains(UserRole.ROLE_EMPLOYEE) ){
            return (dbTransaction) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        //check daily or transaction limit



        //check if its savings or current account and
        AccountType accountType;
        //make sure no one can transfer from savings to another user' current account
        dbAccount accontfrom;
        dbAccount accountTo;


        //getting IBANS from and to
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



}
