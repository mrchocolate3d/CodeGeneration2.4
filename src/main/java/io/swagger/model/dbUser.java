package io.swagger.model;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Table(name = "DB_USER")
public class dbUser {
    @Id
    @GeneratedValue
    @Column(name="userId")
    private long id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phone;
    private String password;

    public dbUser(String firstName, String lastName, String username, String email, String phone, String password, List<UserRole> roles, double transactionLimit) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roles = roles;
        this.transactionLimit = transactionLimit;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    List<UserRole> roles;
    private double transactionLimit;

  //  @OneToMany(mappedBy = "user")
    //@JsonManagedReference
   // private Set<dbAccount> accounts;




}
