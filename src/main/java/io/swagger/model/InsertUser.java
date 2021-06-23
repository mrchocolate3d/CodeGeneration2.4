package io.swagger.model;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;

/**
 * Creating a new user
 */
@Entity
@Schema(description = "Creating a new user")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-06-01T11:41:56.516Z[GMT]")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name= "users")
public class InsertUser   {
  @JsonProperty("id")
  @Id
  @GeneratedValue
  private Long id = null;
  @JsonProperty("username")
  private String username = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("phone")
  private String phone = null;

  @JsonProperty("transactionLimit")
  private Double transactionLimit = null;

  public InsertUser(String username, String password, String firstName, String lastName, String email, String phone, Double transactionLimit, List<UserRole> roles) {
    this.username = username;
    this.password = password;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.transactionLimit = transactionLimit;
    this.roles = roles;
  }

  @Schema(example = "1", required = true, description = "")
  @NotNull

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets or Sets role
   */
  public enum RoleEnum {
    ROLE_EMPLOYEE("ROLE_EMPLOYEE"),

    ROLE_CUSTOMER("ROLE_CUSTOMER");

    private String value;

    RoleEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RoleEnum fromValue(String text) {
      for (RoleEnum b : RoleEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("role")
  @ElementCollection(fetch = FetchType.EAGER)
  private List<UserRole> roles = null;

  public InsertUser username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   **/
  @Schema(example = "jamesB", description = "")
  
    public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public InsertUser password(String password) {
    this.password = password;
    return this;
  }

  @Schema(example = "3000", description = "")
  public Double getTransactionLimit(){
    return transactionLimit;
  }

  public InsertUser transactionLimit(Double transactionLimit){
    this.transactionLimit = transactionLimit;
    return this;
  }
  public void setTransactionLimit(Double transactionLimit){
    this.transactionLimit = transactionLimit;
  }

  /**
   * Get password
   * @return password
   **/
  @Schema(example = "doglover123", description = "")
  
    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public InsertUser firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   **/
  @Schema(example = "James", description = "")
  
    public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public InsertUser lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   **/
  @Schema(example = "Brown", description = "")
  
    public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public InsertUser email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   **/
  @Schema(example = "jamesBrown120@outlook.com", description = "")
  
    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public InsertUser phone(String phone) {
    this.phone = phone;
    return this;
  }

  /**
   * Get phone
   * @return phone
   **/
  @Schema(example = "3138348173799", description = "")
  
    public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public InsertUser roles(List<UserRole> roles) {
    this.roles = roles;
    return this;
  }

  /**
   * Get role
   * @return role
   **/
  @Schema(example = "Customer", description = "")
  
    public List<UserRole> getRoles() {
    return roles;
  }

  public void setRole(UserRole role) {
    this.roles = roles;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InsertUser insertUser = (InsertUser) o;
    return  Objects.equals(this.id, insertUser.id) &&
        Objects.equals(this.username, insertUser.username) &&
        Objects.equals(this.password, insertUser.password) &&
        Objects.equals(this.firstName, insertUser.firstName) &&
        Objects.equals(this.lastName, insertUser.lastName) &&
        Objects.equals(this.email, insertUser.email) &&
        Objects.equals(this.phone, insertUser.phone) &&
        Objects.equals(this.roles, insertUser.roles) &&
        Objects.equals(this.transactionLimit, insertUser.transactionLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username, password, firstName, lastName, email, phone, roles, transactionLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InsertUser {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
    sb.append("    role: ").append(toIndentedString(roles)).append("\n");
    sb.append("    transactionLimit:").append(toIndentedString(transactionLimit)).append("\n");
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
