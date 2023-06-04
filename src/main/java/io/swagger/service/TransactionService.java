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

import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;


    //getting all transactions
    public List<dbTransaction> getTransactions(String IBAN,OffsetDateTime from,OffsetDateTime to,int dayLimit){
        //implementing the limit //has to be pageable
        Integer page = 1;
        Pageable p = PageRequest.of(page, dayLimit);

        dbUser user = null;
        user = userRepository.findUserByUsername(user.getUsername());
        List<dbTransaction> transactions = new ArrayList<>();
        return transactions;
    }


    //post transaction
    public dbTransaction createTransaction(dbTransaction transaction){
        if(transaction.getAmount() == null||transaction.getIBANto() == null||
                transaction.getIBANfrom() == null|| Objects.isNull(transaction.getUserPerform())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Input field missing");
        }
        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANfrom());
        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANto());
        dbUser user = userService.getUserById(transaction.getUserPerform());
        //...
        if(accountFrom == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
        }
        else if(accountTo == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");
        }
        dbUser userTo = userRepository.findById(accountTo.getUser().getId()).get();
        if((accountFrom.getAccountType() == AccountType.TYPE_SAVING && userTo.getId()!=user.getId()) || (accountTo.getAccountType() == AccountType.TYPE_SAVING && userTo.getId() != user.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to a saving's account of another user");
        }
        else{

            if(accountFrom.getBalance() - transaction.getAmount() < accountFrom.getAbsoluteLimit()){
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Limit reach. Please change your transaction amount");
            }
            //refactored
            if (user.getRole() == UserRole.ROLE_EMPLOYEE || accountFrom.getUser() == user){
                if(accountFrom.getAbsoluteLimit() > accountFrom.getBalance() - transaction.getAmount())
                {
                    throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "You don't have enough credit to make the transaction");
                }
                if(accountFrom.getUser().getDayLimit() < getTotalTransactionAmountByAccountAndDate(LocalDate.now(), accountFrom.getIban()) + transaction.getAmount()){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day limit reached");
                }
                if(accountFrom.getUser().getTransactionLimit() < transaction.getAmount()){
                    throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Transaction limit reached");
                }
            }
            else{
                throw new ResponseStatusException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, "You are not allowed to make this transaction");
            }
            //end of refactoring
            //end of refactoring

            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
            accountRepository.save(accountFrom);

            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountRepository.save(accountTo);


        }
//        UpdateBalanceFrom(transaction);
//        UpdateBalanceTo(transaction);

        setTransactionsFromDb(transaction);
        transactionRepository.save(transaction);
        return transaction;
    }

    public Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        transaction.setIbANTo(dbTransaction.getIBANto());
        transaction.setAmount(dbTransaction.getAmount());
        return transaction;
    }


    public List<dbTransaction> getAllTransactionsFromAnAccount(String IBAN, OffsetDateTime from, OffsetDateTime to){
        List<dbTransaction> transactions = new ArrayList<>();
        dbTransaction transaction = null;
        transactions.add(transaction);
        return transactions;

    }

    public Double getTotalTransactionAmountByAccountAndDate(LocalDate date, String IbanFrom){
        List<Double> transactionAmounts = transactionRepository.getTransactionAmountByAccountAndDate(date, IbanFrom);
        double totalAmountInDay = 0;
        for(double a : transactionAmounts){
            totalAmountInDay += a;
        }

        return totalAmountInDay;
    }
    public List<dbTransaction> getAllTransactions(){
        return (List<dbTransaction>)transactionRepository.findAll();
    }

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
