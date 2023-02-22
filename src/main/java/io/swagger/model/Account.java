package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Account banking information
 */
@Schema(description = "Account banking information")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")


public class Account   {

  @JsonProperty("IBAN")
  private String iban = null;

  public Account iban(String iban){
    this.iban = iban;
    return this;
  }

  public String getIban(){return iban;}

  public void setIban(String iban){this.iban = iban;}

  @JsonProperty("User")
  private User user = null;

  /**
   * Gets or Sets accountType
   */

  @JsonProperty("accountType")
  private AccountType accountType = null;

  public Account user(User user) {
    this.user = user;
    return this;
  }

  /**
   * Get userId
   * @return userId
   **/
  @Schema(example = "1", required = true, description = "")
  @NotNull

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Account accountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Get accountType
   * @return accountType
   **/
  @Schema(required = true, description = "")
  @NotNull

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }


  /**
   * Absolute limit
   * */
  @JsonProperty("absoluteLimit")
  private double absoluteLimit;
  public double getAbsoluteLimit() {return absoluteLimit;}

  public void setAbsoluteLimit(double absoluteLimit) {this.absoluteLimit = absoluteLimit;}

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(this.user, account.user) &&
            Objects.equals(this.accountType, account.accountType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, accountType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Account {\n");

    sb.append("    user: ").append(toIndentedString(user.toString())).append("\n");
    sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
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
