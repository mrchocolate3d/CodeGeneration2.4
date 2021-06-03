package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="DB_ACCOUNT")
public class dbAccount {
    @Id
    @GeneratedValue
    private long id;
//    @ElementCollection(fetch = FetchType.EAGER)
=======
    @ElementCollection(fetch = FetchType.LAZY)
    private List<AccountType> accountTypes;
    private double balance;
    private String iban;


<<<<<<< Updated upstream
    @ManyToOne
    @JoinColumn(name="userId")
=======
    @JoinColumn(name="userId")
    @JsonBackReference
>>>>>>> Stashed changes
    private dbUser user;

}
