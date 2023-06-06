package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
//@NoArgsConstructor
@Data
@Table(name = "DB_TRANSACTION")
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class dbTransaction {
    @Id
    @GeneratedValue
    @Column(name="ID" , updatable = false, nullable = false)
    private long id;

    private long userPerform;
    private String IBANfrom;
    private String IBANto;
    private Double amount;
    private LocalDateTime timestamp;
    public dbTransaction(long userPerform,String IBANto,String IBANfrom, Double amount, LocalDateTime timestamp) {
        this.userPerform = userPerform;
        this.IBANfrom = IBANfrom;
        this.IBANto = IBANto;
        this.amount = amount;
        this.timestamp = timestamp;

        if (this.amount <=0){
            throw new IllegalArgumentException("Invalid amount");
        }
    }


}
