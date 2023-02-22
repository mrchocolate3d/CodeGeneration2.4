package io.swagger.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

/**
 * Remain Amount and Limit of the IBAN account
 */
@Schema(description = "Remain Amount and Limit of the IBAN account")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
public class ReturnLimitAndRemainingAmount {
    @JsonProperty("IBAN")
    private String IBAN = null;


    @JsonProperty("accountType")
    private AccountType accountType = null;

    @JsonProperty("limit")
    private Double limit = null;

    @JsonProperty("remainAmount")
    private Double remainAmount = null;

    public ReturnLimitAndRemainingAmount IBAN(String IBAN) {
        this.IBAN = IBAN;
        return this;
    }

    /**
     * Get IBAN
     * @return IBAN
     **/
    @Schema(example = "NL90RABO34", description = "")

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public ReturnLimitAndRemainingAmount accountType(AccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    /**
     * Get accountType
     * @return accountType
     **/
    @Schema(description = "")

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public ReturnLimitAndRemainingAmount balance(Double limit) {
        this.limit = limit;
        return this;
    }

    /**
     * Get limit
     * @return limit
     **/
    @Schema(example = "500", description = "")

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    /**
     * Get remainAmount
     * @return remainAmount
     **/
    @Schema(example = "500", description = "")

    public Double getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(Double remainAmount) {
        this.remainAmount = remainAmount;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReturnLimitAndRemainingAmount returnLimitAndRemainingAmount = (ReturnLimitAndRemainingAmount) o;
        return Objects.equals(this.IBAN, returnLimitAndRemainingAmount.IBAN) &&
                Objects.equals(this.accountType, returnLimitAndRemainingAmount.accountType) &&
                Objects.equals(this.limit, returnLimitAndRemainingAmount.limit) &&
                Objects.equals(this.remainAmount, returnLimitAndRemainingAmount.remainAmount);

    }

    @Override
    public int hashCode() {
        return Objects.hash(IBAN, accountType, limit, remainAmount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ReturnBalance {\n");

        sb.append("    IBAN: ").append(toIndentedString(IBAN)).append("\n");
        sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
        sb.append("    limit: ").append(toIndentedString(limit)).append("\n");
        sb.append("    remainAmount: ").append(toIndentedString(remainAmount)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
