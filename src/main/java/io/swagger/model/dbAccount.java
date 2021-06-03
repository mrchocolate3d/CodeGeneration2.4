package io.swagger.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
@Table(name="DB_ACCOUNT")
public class dbAccount {
    @Id
    @GeneratedValue
    private long id;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<AccountType> accountTypes;
    private double balance;
    private String iban;

    public dbAccount(long id, List<AccountType> accountTypes, double balance, String iban) {
        this.id = id;
        this.accountTypes = accountTypes;
        this.balance = balance;
        this.iban = iban;
    }



    //    @ManyToOne
//    private dbUser user;

}
