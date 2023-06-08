package io.swagger.service;

import io.swagger.model.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static io.swagger.repository.TransactionRepository.*;
import static org.springframework.data.jpa.domain.Specification.where;

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
    public List<Transaction> getTransactions(String IBAN, LocalDate fromDate, LocalDate toDate, Integer limit){
        dbUser user = checkAuthAndReturnUser();

        List<Transaction> transactionList = new ArrayList<>();
        List<dbTransaction> dbTransactions;
        dbAccount accountCheck = accountRepository.findAccountByIban(IBAN);
        if(user.getRole() == UserRole.ROLE_EMPLOYEE || (user.getRole() == UserRole.ROLE_CUSTOMER && accountCheck.getUser() == user)) {
            if (fromDate != null && toDate == null)
                dbTransactions = transactionRepository.findAll(where(afterDate(fromDate)).and(hasIbanFrom(IBAN)));
            else if (fromDate == null && toDate != null)
                dbTransactions = transactionRepository.findAll(where(beforeDate(toDate)).and(hasIbanFrom(IBAN)));
            else if (fromDate != null && toDate != null)
                dbTransactions = transactionRepository.findAll(where(beforeDate(toDate)).and(afterDate(fromDate)).and(hasIbanFrom(IBAN)));
            else
                dbTransactions = transactionRepository.findAll(hasIbanFrom(IBAN));

            for (dbTransaction dbTransaction : dbTransactions) {
                Transaction transaction = setTransactionsFromDb(dbTransaction);
                transactionList.add(transaction);
            }
            if (limit != null && limit < transactionList.size() && limit > 0){
                List<Transaction> limitList = transactionList.subList(0, limit);
                return limitList;
            } else if (limit != null && limit <= 0)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit has to be bigger than 0");
            return transactionList;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    //post transaction
    public Transaction createTransaction(Transaction transaction){
        dbUser user = checkAuthAndReturnUser();
        LocalDateTime timeCreated = LocalDateTime.now();
        if(transaction.getAmount() == null||transaction.getIBANTo() == null|| transaction.getIBANFrom() == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Input field missing");

        dbAccount accountFrom = accountRepository.findAccountByIban(transaction.getIBANFrom());
        dbAccount accountTo = accountRepository.findAccountByIban(transaction.getIBANTo());

        if(accountFrom == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");


        if(accountTo == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"This Account does not exist");

        if(user.getRole() == UserRole.ROLE_EMPLOYEE || (user.getRole() == UserRole.ROLE_CUSTOMER && accountFrom.getUser() == user)) {
            checkTransactionCondition(user, accountFrom, accountTo, transaction, timeCreated.toLocalDate());

            dbTransaction validTransaction = new dbTransaction(user.getId(), transaction.getIBANTo(), transaction.getIBANFrom(), transaction.getAmount(), timeCreated);
            transactionRepository.save(validTransaction);

            accountFrom.setBalance(accountFrom.getBalance() - transaction.getAmount());
            accountRepository.save(accountFrom);

            accountTo.setBalance(accountTo.getBalance() + transaction.getAmount());
            accountRepository.save(accountTo);

            return setTransactionsFromDb(validTransaction);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    private dbUser checkAuthAndReturnUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        dbUser user = userService.getdbUserByUserName(username);

        if(user == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"No authentication token was given");
        }

        return user;
    }

    private void checkTransactionCondition(dbUser user, dbAccount accountFrom, dbAccount accountTo, Transaction transaction, LocalDate timeCreated){
        dbUser userTo = userRepository.findById(accountTo.getUser().getId()).get();
        if((accountFrom.getAccountType() == AccountType.TYPE_SAVING && userTo.getId() != user.getId()) ||
                (accountTo.getAccountType() == AccountType.TYPE_SAVING && userTo.getId() != user.getId())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can not transfer to a saving's account of another user");
        }
        if(accountFrom.getBalance() - transaction.getAmount() < accountFrom.getAbsoluteLimit()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Limit reach. Please change your transaction amount");
        }
        //refactored
        if (user.getRole() == UserRole.ROLE_EMPLOYEE || accountFrom.getUser() == user){
            if(accountFrom.getAbsoluteLimit() > accountFrom.getBalance() - transaction.getAmount())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You don't have enough credit to make the transaction");
            }
            if(accountFrom.getUser().getDayLimit() < GetTotalTransactionAmountByAccountAndDate(timeCreated, accountFrom.getIban()) + transaction.getAmount()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day limit reached");
            }
            if(accountFrom.getUser().getTransactionLimit() < transaction.getAmount()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction limit reached");
            }
        }
        else{
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to make this transaction");
        }
    }

    private Transaction setTransactionsFromDb(dbTransaction dbTransaction){
        Transaction transaction = new Transaction();
        transaction.setIbANFrom(dbTransaction.getIBANfrom());
        transaction.setIbANTo(dbTransaction.getIBANto());
        transaction.setAmount(dbTransaction.getAmount());
        transaction.setTimestamp(dbTransaction.getTimestamp());
        return transaction;
    }


    public Double GetTotalTransactionAmountByAccountAndDate(LocalDate date, String IbanFrom){
        List<Double> transactionAmounts = transactionRepository.getTransactionAmountByAccountAndDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay(), IbanFrom);
        double totalAmountInDay = 0;
        for(double a : transactionAmounts){
            totalAmountInDay += a;
        }

        return totalAmountInDay;
    }
}
