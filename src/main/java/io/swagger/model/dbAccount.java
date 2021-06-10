package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="DB_ACCOUNT")
public class dbAccount {
    @Id
    @GeneratedValue
    private long id;
    AccountType accountType;
    private Double balance;
    private String iban;


    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="userId")
    @JsonBackReference
    private dbUser user;

    public dbAccount(long id, AccountType accountType, Double balance, String iban){
        this.id = id;
        this.accountType = accountType;
        this.balance = balance;
        this.iban = iban;
    }
}
