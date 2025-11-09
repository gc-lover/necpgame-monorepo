package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * GuildApplication
 */


public class GuildApplication {

  private @Nullable String applicationId;

  private @Nullable String applicant;

  private @Nullable String message;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    ACCEPTED("accepted"),
    
    DECLINED("declined");

    private final String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime submittedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime decisionAt;

  public GuildApplication applicationId(@Nullable String applicationId) {
    this.applicationId = applicationId;
    return this;
  }

  /**
   * Get applicationId
   * @return applicationId
   */
  
  @Schema(name = "applicationId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applicationId")
  public @Nullable String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(@Nullable String applicationId) {
    this.applicationId = applicationId;
  }

  public GuildApplication applicant(@Nullable String applicant) {
    this.applicant = applicant;
    return this;
  }

  /**
   * Get applicant
   * @return applicant
   */
  
  @Schema(name = "applicant", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("applicant")
  public @Nullable String getApplicant() {
    return applicant;
  }

  public void setApplicant(@Nullable String applicant) {
    this.applicant = applicant;
  }

  public GuildApplication message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  public GuildApplication status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public GuildApplication submittedAt(@Nullable OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
    return this;
  }

  /**
   * Get submittedAt
   * @return submittedAt
   */
  @Valid 
  @Schema(name = "submittedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submittedAt")
  public @Nullable OffsetDateTime getSubmittedAt() {
    return submittedAt;
  }

  public void setSubmittedAt(@Nullable OffsetDateTime submittedAt) {
    this.submittedAt = submittedAt;
  }

  public GuildApplication decisionAt(@Nullable OffsetDateTime decisionAt) {
    this.decisionAt = decisionAt;
    return this;
  }

  /**
   * Get decisionAt
   * @return decisionAt
   */
  @Valid 
  @Schema(name = "decisionAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("decisionAt")
  public @Nullable OffsetDateTime getDecisionAt() {
    return decisionAt;
  }

  public void setDecisionAt(@Nullable OffsetDateTime decisionAt) {
    this.decisionAt = decisionAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuildApplication guildApplication = (GuildApplication) o;
    return Objects.equals(this.applicationId, guildApplication.applicationId) &&
        Objects.equals(this.applicant, guildApplication.applicant) &&
        Objects.equals(this.message, guildApplication.message) &&
        Objects.equals(this.status, guildApplication.status) &&
        Objects.equals(this.submittedAt, guildApplication.submittedAt) &&
        Objects.equals(this.decisionAt, guildApplication.decisionAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(applicationId, applicant, message, status, submittedAt, decisionAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuildApplication {\n");
    sb.append("    applicationId: ").append(toIndentedString(applicationId)).append("\n");
    sb.append("    applicant: ").append(toIndentedString(applicant)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
    sb.append("    decisionAt: ").append(toIndentedString(decisionAt)).append("\n");
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

