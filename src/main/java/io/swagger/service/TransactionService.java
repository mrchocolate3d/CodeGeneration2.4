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
//    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int dayLimit){
//        //implementing the limit //has to be pageable
//        Integer page = 1;
//        Pageable p = PageRequest.of(page, dayLimit);
//
//        dbUser user = null;
//        user = userRepository.findUserByUsername(user.getUsername());
//        List<dbTransaction> transactions = new ArrayList<>();
//        return transactions;
//    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){



//        if(transaction.getAmount() == null||transaction.getIBANto() == null||
//                transaction.getIBANfrom() == null|| transaction.getUserPerform() == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Input field missing");
//        }
//        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANfrom());
//        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANto());
//        dbUser user = userRepository.findUserByUsername(transaction.getUserPerform());
//        //...
//        if(accountFrom == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
//        }
//        else if(accountTo == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
//        }
//        dbUser userTo = userRepository.findById(accountTo.getId()).get();
//        if(accountFrom.getAccountType() == AccountType.TYPE_SAVING && userTo.getId()!=user.getId()){
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to a saving's account of another user");
//        }
//        else{
//            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
//            accountRepository.save(accountFrom);
//
//            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
//            accountRepository.save(accountTo);
//
//
//        }
        //check if its savings or not
        //check if account exists
        //transaction limit
        //check user
        //check origin of account
//        UpdateBalanceFrom(transaction);
//        UpdateBalanceTo(transaction);

//        setTransactionsFromDb(transaction);
        transactionRepository.save(transaction);
        return transaction;
    }

    //converting dbtransaction to transaction
    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setTime(dbTransaction.getTimestamp().toString());
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        transaction.setIbANTo(dbTransaction.getIBANto());
        transaction.setAmount(dbTransaction.getAmount());
        transaction.setUserPerform(dbTransaction.getUserPerform());
        return transaction;
    }

    //getAllTransactions
    //createTransaction





//    public List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
//        List<dbTransaction> transactions = new ArrayList<>();
//        dbTransaction transaction = null;
//        transactions.add(transaction);
//        return transactions;
//
//    }
//
    public List<dbTransaction> getTransactionByIBANfrom(String IBAN){
        Iterable<dbTransaction> transactions = transactionRepository.getTransactionsByIBANfrom(IBAN);

        if(IBAN.isEmpty() || IBAN == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
        }
        return (List<dbTransaction>) transactions;
    }
    public List<dbTransaction> getTransactionByIBANto(String IBAN){
      Iterable<dbTransaction> transactions = transactionRepository.getTransactionsByIBANto(IBAN);

            if(IBAN.isEmpty() || IBAN == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
            }
            return (List<dbTransaction>) transactions;
        }

    public Integer CountAllTransactions(){
        return transactionRepository.CountAllTransactions();
    }
//
//    public void UpdateBalanceFrom(dbTransaction transaction){
//        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANfrom());
//        if(account!=null){
//            account.setBalance(account.getBalance() - transaction.getAmount());
//            accountRepository.save(account);
//        }
//    }
//    public void UpdateBalanceTo(dbTransaction transaction){
//        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANto());
//        if(account!=null){
//            account.setBalance(account.getBalance() + transaction.getAmount());
//            accountRepository.save(account);
//        }
//    }

}
