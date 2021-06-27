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
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int dayLimit){
        //implementing the limit //has to be pageable
        Integer page = 1;
        Pageable p = PageRequest.of(page, dayLimit);

        dbUser user = null;
        user = userRepository.findUserByUsername(user.getUsername());
        List<dbTransaction> transactions = new ArrayList<>();

//        List<dbAccount> accounts = findAccountByUserId(user);
//        dbAccount account = accountRepository.findAccountByIban(IBAN);
//        accounts.add(account);
//
//        for (dbAccount a : accounts){
//            transactions.addAll((Collection<? extends dbTransaction>) transactionRepository.getTransactionsByIBANfrom(a.getIban()));
//        }

        return transactions;
    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        if(transaction.getAmount() == null||transaction.getIBANto() == null||
                transaction.getIBANfrom() == null|| transaction.getUserPerform() == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Input field missing");
        }
        dbUser userPerforming = userRepository.findUserByUsername(transaction.getUserPerform());

        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANfrom());
        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANto());

        accountFrom.getAccountType().equals(accountTo.getAccountType());
        if(accountFrom.getUser().equals(accountTo.getUser())){
            if(accountFrom.getAccountType() == AccountType.TYPE_SAVING && accountTo.getAccountType() == AccountType.TYPE_SAVING){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"you cannot transfer from savings to another savings account");
            }
            else{
                if(accountFrom.getBalance() < transaction.getAmount()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"not enough money to transfer");
                }
                else{
                    accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
                    accountRepository.save(accountFrom);
                    accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
                    accountRepository.save(accountTo);
                }
            }

        }
        else{
            if(accountFrom.getAccountType() == AccountType.TYPE_CURRENT && accountTo.getAccountType() == AccountType.TYPE_CURRENT){
                accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
                accountRepository.save(accountFrom);
                accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
                accountRepository.save(accountTo);
            }
        }


//        if(userPerforming == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User does not exist");
//        }
//        //account for account
//        List<dbAccount> accounts = accountRepository.findAccountByUserId(userPerforming.getId());
//        if(accounts == null){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account does not exist");
//        }
//        // ....
//        transaction.setUserPerform(userPerforming.getUsername());
//        Boolean exists = false;
//        for(dbAccount account : accounts){
//            if(account.getIban().contains(transaction.getIBANfrom())){
//                exists = true;
//            }
//            if(!exists){
//                throw new ResponseStatusException(HttpStatus.NOT_FOUND,"You cannot make a transaction with this account");
//            }
//            dbUser userToTransferTo = null;
//            if(userRepository.findById(accountTo.getId()).isPresent()){
//                userToTransferTo = userRepository.findById(accountTo.getId()).get();
//            }
//            if(userToTransferTo == null){
//                if(accountTo.getAccountType() == AccountType.TYPE_SAVING && userToTransferTo.getId() !=userPerforming.getId()){
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You cannot transfer from the savings of another user");
//                }
//                if(account.getAccountType() == AccountType.TYPE_SAVING && userToTransferTo.getId() != userPerforming.getId()){
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You cannot transfer from your savings account to someone else's account");
//                }
//            }
//            else{
//
//            }
//            UpdateBalanceFrom(transaction);
//            UpdateBalanceTo(transaction);
//        }





        //check ibans and throw if its in incorrect format
        //check if iban exists

        //now checks
        //get ibans from accountrepo
        //make sure only the right people can make the transactioons
        //check if its savings or current
        //getting remaining balance of both from and to IBANS


        setTransactionsFromDb(transaction);
        transactionRepository.save(transaction); //saves to the db
        return transaction;
    }

    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setTime(dbTransaction.getTimestamp().toString());
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        transaction.setIbANTo(dbTransaction.getIBANto());
        transaction.setAmount(dbTransaction.getAmount());
        transaction.setUserPerform(dbTransaction.getUserPerform());
        return transaction;
    }


    public List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
        List<dbTransaction> transactions = new ArrayList<>();


//        dbTransaction transaction = transactionRepository.getTransactionsByIBAN(IBAN);
        dbTransaction transaction = null;
        transactions.add(transaction);
        return transactions;

    }
//    public List<dbAccount> findAccountByUserId(dbUser user){
//        return accountRepository.findAccountById(user.getId());
//    }
    public List<dbTransaction> getAllTransactions(){
        return (List<dbTransaction>)transactionRepository.findAll();
    }

    public List<dbTransaction> getTransactionByIBAN(String IBAN){
        Iterable<dbTransaction> transactions = transactionRepository.getTransactionsByIBANfrom(IBAN);

        if(IBAN.isEmpty() || IBAN==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"IBAN is not provided!");
        }
        return (List<dbTransaction>) transactions;
    }

    //check if its savings or current account
    //make sure no one can transfer from savings to another user's current account
    //saving the amount//not sure
    //check if user has the right to access accounts //not sure if its the employee or customer who dont have access to make a transaction
    public Integer CountAllTransactions(){
        return transactionRepository.CountAllTransactions();
    }

    public void UpdateBalanceFrom(dbTransaction transaction){
        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANfrom());
        if(account!=null){
            account.setBalance(account.getBalance() - transaction.getAmount());
            accountRepository.save(account);
        }
    }
    public void UpdateBalanceTo(dbTransaction transaction){
        dbAccount account = accountRepository.findAccountByIban(transaction.getIBANto());
        if(account!=null){
            account.setBalance(account.getBalance() + transaction.getAmount());
            accountRepository.save(account);
        }
    }

}
