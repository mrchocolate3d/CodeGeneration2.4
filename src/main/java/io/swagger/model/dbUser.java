package io.swagger.model;

import lombok.*;
import lombok.extern.java.Log;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class dbUser {
    @Id
    @GeneratedValue
    private long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    @ElementCollection(fetch = FetchType.EAGER)
    List<UserRole> roles;
    private double transactionLimit;
    private String password;


//    @OneToMany(cascade = CascadeType.ALL,mappedBy="user")
//    private Set<dbAccount> accounts = new HashSet<>();


}
