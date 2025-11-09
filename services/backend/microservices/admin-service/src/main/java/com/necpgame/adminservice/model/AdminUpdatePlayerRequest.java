package com.necpgame.adminservice.model;

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
 * AdminUpdatePlayerRequest
 */

@JsonTypeName("adminUpdatePlayer_request")

public class AdminUpdatePlayerRequest {

  private @Nullable String email;

  private @Nullable String status;

  private @Nullable Integer premiumCurrency;

  private @Nullable String notes;

  public AdminUpdatePlayerRequest email(@Nullable String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public @Nullable String getEmail() {
    return email;
  }

  public void setEmail(@Nullable String email) {
    this.email = email;
  }

  public AdminUpdatePlayerRequest status(@Nullable String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable String getStatus() {
    return status;
  }

  public void setStatus(@Nullable String status) {
    this.status = status;
  }

  public AdminUpdatePlayerRequest premiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
    return this;
  }

  /**
   * Get premiumCurrency
   * @return premiumCurrency
   */
  
  @Schema(name = "premium_currency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("premium_currency")
  public @Nullable Integer getPremiumCurrency() {
    return premiumCurrency;
  }

  public void setPremiumCurrency(@Nullable Integer premiumCurrency) {
    this.premiumCurrency = premiumCurrency;
  }

  public AdminUpdatePlayerRequest notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AdminUpdatePlayerRequest adminUpdatePlayerRequest = (AdminUpdatePlayerRequest) o;
    return Objects.equals(this.email, adminUpdatePlayerRequest.email) &&
        Objects.equals(this.status, adminUpdatePlayerRequest.status) &&
        Objects.equals(this.premiumCurrency, adminUpdatePlayerRequest.premiumCurrency) &&
        Objects.equals(this.notes, adminUpdatePlayerRequest.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(email, status, premiumCurrency, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AdminUpdatePlayerRequest {\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    premiumCurrency: ").append(toIndentedString(premiumCurrency)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

