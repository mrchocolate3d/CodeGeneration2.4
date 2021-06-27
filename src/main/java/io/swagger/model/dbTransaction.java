package io.swagger.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.threeten.bp.OffsetDateTime;

import javax.persistence.*;

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

    private String userPerform;
    private String IBANfrom;
    private String IBANto;
    private Double amount;
    private OffsetDateTime timestamp;
    //private Integer dayLimit; //day limit //not sure entirely

    public dbTransaction(String userPerform,String IBANto,String IBANfrom, Double amount, OffsetDateTime timestamp) {
        this.userPerform = userPerform;
        this.IBANfrom = IBANfrom;
        this.IBANto = IBANto;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public dbTransaction() {

    }
}
