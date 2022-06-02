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

import java.math.BigDecimal;
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




    //check if its savings or not
    //check if account exists
    //transaction limit
    //check user
    //check origin of account
//        UpdateBalanceFrom(transaction);
//        UpdateBalanceTo(transaction);

    //post transaction
//    public dbTransaction createTransaction(dbTransaction transaction){
//
//        transactionRepository.save(transaction);
//        return transaction;
//    }

    public Transaction setTransactionsFromDb(dbTransaction dbT){
        return new Transaction(dbT.getId(),dbT.getUserPerform(),dbT.getIBANto(),dbT.getIBANfrom(),dbT.getAmount(),dbT.getTimestamp());
    }
    public dbTransaction addTransaction(dbTransaction transaction){

        transactionRepository.save(transaction);
        return transaction;
    }


    //    ----------------------------------------------------
//    public dbTransaction testing2(Transaction transaction){
//        dbTransaction dbTransaction = new dbTransaction();
//        dbTransaction.setAmount(transaction.getAmount());
//        dbTransaction.setUserPerform(transaction.getUserPerform());
//        dbTransaction.setIBANfrom(transaction.getIbANFrom());
//        dbTransaction.setIBANto(transaction.getIbANTo());
//        return dbTransaction;
//    }

    public java.sql.Date getDateToString(){
        return new java.sql.Date(Calendar.getInstance().getTime().getTime());
    }

//
//    //converting dbtransaction to transaction







//    public List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
//        List<dbTransaction> transactions = new ArrayList<>();
//        dbTransaction transaction = null;
//        transactions.add(transaction);
//        return transactions;
//
//    }
//
    public List<dbTransaction> getTransactionByIBANfrom(String IBAN){
        List<dbTransaction> getTransactionsByIBANfrom = transactionRepository.getTransactionsByIBANfrom(IBAN);
        if(IBAN.isEmpty() || IBAN == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
        }
        return getTransactionsByIBANfrom;
    }


    public List<dbTransaction> getTransactionByIBANto(String IBAN){
      List<dbTransaction> transactionsOfIBANTo = transactionRepository.getTransactionsByIBANto(IBAN);
            if(IBAN.isEmpty() || IBAN == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
            }
            return transactionsOfIBANTo;
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

//    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
//        Transaction transaction = new Transaction();
//        transaction.setTime(dbTransaction.getTimestamp().toString());
//        transaction.setIbANFrom(dbTransaction.getIBANfrom());
//        transaction.setIbANTo(dbTransaction.getIBANto());
//        transaction.setAmount(dbTransaction.getAmount());
//        transaction.setUserPerform(dbTransaction.getUserPerform());
//        return transaction;
//    }