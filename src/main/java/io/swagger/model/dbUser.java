package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.java.Log;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DB_USER")
public class dbUser {
    @Id
    @GeneratedValue
    @SequenceGenerator(name = "userId", initialValue = 1)
    @Column(name="userId")
    private long id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String password;
    private double dayLimit;

    public dbUser(String firstName, String lastName, String username, String email, String phone, String password, UserRole role, double transactionLimit, double dayLimit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.transactionLimit = transactionLimit;
        this.dayLimit = dayLimit;
        accounts = new ArrayList<>();
    }
    UserRole role;
    private double transactionLimit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<dbAccount> accounts;



}
