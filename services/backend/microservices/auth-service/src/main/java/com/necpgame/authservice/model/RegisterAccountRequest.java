package com.necpgame.authservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RegisterAccountRequest
 */

@JsonTypeName("registerAccount_request")

public class RegisterAccountRequest {

  private String email;

  private String password;

  private @Nullable String username;

  private @Nullable Boolean agreeToTerms;

  public RegisterAccountRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RegisterAccountRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public RegisterAccountRequest email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  @NotNull @jakarta.validation.constraints.Email 
  @Schema(name = "email", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RegisterAccountRequest password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Минимум 8 символов
   * @return password
   */
  @NotNull @Size(min = 8) 
  @Schema(name = "password", description = "Минимум 8 символов", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public RegisterAccountRequest username(@Nullable String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  @Size(min = 3, max = 20) 
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public @Nullable String getUsername() {
    return username;
  }

  public void setUsername(@Nullable String username) {
    this.username = username;
  }

  public RegisterAccountRequest agreeToTerms(@Nullable Boolean agreeToTerms) {
    this.agreeToTerms = agreeToTerms;
    return this;
  }

  /**
   * Согласие с условиями использования
   * @return agreeToTerms
   */
  
  @Schema(name = "agree_to_terms", description = "Согласие с условиями использования", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("agree_to_terms")
  public @Nullable Boolean getAgreeToTerms() {
    return agreeToTerms;
  }

  public void setAgreeToTerms(@Nullable Boolean agreeToTerms) {
    this.agreeToTerms = agreeToTerms;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterAccountRequest registerAccountRequest = (RegisterAccountRequest) o;
    return Objects.equals(this.email, registerAccountRequest.email) &&
        Objects.equals(this.password, registerAccountRequest.password) &&
        Objects.equals(this.username, registerAccountRequest.username) &&
        Objects.equals(this.agreeToTerms, registerAccountRequest.agreeToTerms);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, password, username, agreeToTerms);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterAccountRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    agreeToTerms: ").append(toIndentedString(agreeToTerms)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

