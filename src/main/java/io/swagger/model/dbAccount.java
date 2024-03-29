package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="DB_ACCOUNT")
public class dbAccount {
    @Id
    @SequenceGenerator(initialValue = 1, name="account_seq")
    @GeneratedValue(generator = "account_seq", strategy = GenerationType.SEQUENCE)
    private long id;
    AccountType accountType;
    private double balance;
    private String iban;

    private double absoluteLimit;


    @ManyToOne
    @JsonBackReference
    private dbUser user;

    public void setBalance(double balance){
        if(balance < absoluteLimit){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Balance cannot be lower than absolute limit!");
        }
        this.balance = balance;
    }

//    public void setActive(Boolean active){
//        this.active = active;
//    }
    public dbAccount(AccountType accountType, double balance, dbUser user, double absoluteLimit) {
        this.accountType = accountType;
        this.balance = balance;
        this.user = user;
        this.absoluteLimit = absoluteLimit;
    }
}
