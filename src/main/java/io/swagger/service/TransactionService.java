package io.swagger.service;

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
import org.springframework.web.client.RestClientException;
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
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int limit){

        //implementing the limit
        List<dbTransaction> transactions = new ArrayList<>();
        List<dbAccount> accounts = new ArrayList<>();

        dbAccount account = accountRepository.findAccountByIban(IBAN);
        accounts.add(account);

        for (dbAccount a : accounts){
            transactions.addAll(getAllTransactionsFromAnAccount(IBAN,from,to));
        }
        return transactions;
    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        // TODO: 17/06/2021
        //getting the right user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        dbUser user = userRepository.findUserByUsername(authentication.getName());

        //check if user has the right to access accounts //not sure if its the employee or customer who dont have access to make a transaction
        if(!user.getRoles().contains(UserRole.ROLE_EMPLOYEE) || !user.getRoles().contains(UserRole.ROLE_CUSTOMER)){
            return (dbTransaction) ResponseEntity.status(HttpStatus.UNAUTHORIZED);
        }
        //check daily or transaction limit

        //getting IBANS from and to

        Optional<dbAccount> IBANfrom = Optional.ofNullable(accountRepository.getBalanceByIban(transaction.getIBANfrom()));
        Optional<dbAccount> IBANto = Optional.ofNullable(accountRepository.getBalanceByIban(transaction.getIBANto()));

        dbAccount from = null;
        dbAccount to = null;
        if(IBANfrom.isPresent()){
            from = IBANfrom.get();
        }
        if(IBANto.isPresent()){
            to = IBANto.get();
        }

        //check if its savings or current account
        //make sure no one can transfer from savings to another user's current account
        boolean isSavings = false;
        boolean isCurrent;

        if(from.getAccountType() == AccountType.TYPE_SAVING){
            isSavings = true;
        }
        else if(to.getAccountType() == AccountType.TYPE_SAVING){
            isSavings = false;
        }

        if(isSavings == true){
            if(from.getUser().getId() != to.getUser().getId()){
                throw new RestClientException("You cannot transfer from this account because you are not the owner");
            }
        }
        else{
            throw new RestClientException("You cannot transfer from savings to savings");
        }
        //saving the amount
        accountRepository.save(from); //not sure

        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }








    public Integer CountAllTransactions(){
//        return transactionRepository.CountAllTransactions();
        return 0;
    }
    List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
        List<dbTransaction> transactions = new ArrayList<>();

        if(from == null){
            from = OffsetDateTime.MIN;
        }
        else{
            from = OffsetDateTime.parse("T00:00:00.001+02:00");
        }
        if(to == null){
            to = OffsetDateTime.MAX;
        }
        else{
            to = OffsetDateTime.parse("T00:00:00.001+02:00");
        }
//        dbTransaction transaction = transactionRepository.getTransactionsByIBAN(IBAN);
        dbTransaction transaction = null;
        transactions.add(transaction);
        return transactions;

    }




}
