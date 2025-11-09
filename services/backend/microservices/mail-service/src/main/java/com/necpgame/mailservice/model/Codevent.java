package com.necpgame.mailservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.mailservice.model.CODInfo;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Codevent
 */


public class Codevent {

  private @Nullable String mailId;

  private @Nullable String recipientId;

  private @Nullable CODInfo codInfo;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime occurredAt;

  public Codevent mailId(@Nullable String mailId) {
    this.mailId = mailId;
    return this;
  }

  /**
   * Get mailId
   * @return mailId
   */
  
  @Schema(name = "mailId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mailId")
  public @Nullable String getMailId() {
    return mailId;
  }

  public void setMailId(@Nullable String mailId) {
    this.mailId = mailId;
  }

  public Codevent recipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Get recipientId
   * @return recipientId
   */
  
  @Schema(name = "recipientId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipientId")
  public @Nullable String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
  }

  public Codevent codInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
    return this;
  }

  /**
   * Get codInfo
   * @return codInfo
   */
  @Valid 
  @Schema(name = "codInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("codInfo")
  public @Nullable CODInfo getCodInfo() {
    return codInfo;
  }

  public void setCodInfo(@Nullable CODInfo codInfo) {
    this.codInfo = codInfo;
  }

  public Codevent occurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
    return this;
  }

  /**
   * Get occurredAt
   * @return occurredAt
   */
  @Valid 
  @Schema(name = "occurredAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("occurredAt")
  public @Nullable OffsetDateTime getOccurredAt() {
    return occurredAt;
  }

  public void setOccurredAt(@Nullable OffsetDateTime occurredAt) {
    this.occurredAt = occurredAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Codevent codevent = (Codevent) o;
    return Objects.equals(this.mailId, codevent.mailId) &&
        Objects.equals(this.recipientId, codevent.recipientId) &&
        Objects.equals(this.codInfo, codevent.codInfo) &&
        Objects.equals(this.occurredAt, codevent.occurredAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mailId, recipientId, codInfo, occurredAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Codevent {\n");
    sb.append("    mailId: ").append(toIndentedString(mailId)).append("\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    codInfo: ").append(toIndentedString(codInfo)).append("\n");
    sb.append("    occurredAt: ").append(toIndentedString(occurredAt)).append("\n");
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

