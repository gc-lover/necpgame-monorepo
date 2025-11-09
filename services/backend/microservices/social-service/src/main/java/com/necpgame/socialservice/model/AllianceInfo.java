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
 * AllianceInfo
 */


public class AllianceInfo {

  private @Nullable String allianceId;

  private @Nullable String allyGuildId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    ACTIVE("active"),
    
    REVOKED("revoked");

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
  private @Nullable OffsetDateTime createdAt;

  public AllianceInfo allianceId(@Nullable String allianceId) {
    this.allianceId = allianceId;
    return this;
  }

  /**
   * Get allianceId
   * @return allianceId
   */
  
  @Schema(name = "allianceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allianceId")
  public @Nullable String getAllianceId() {
    return allianceId;
  }

  public void setAllianceId(@Nullable String allianceId) {
    this.allianceId = allianceId;
  }

  public AllianceInfo allyGuildId(@Nullable String allyGuildId) {
    this.allyGuildId = allyGuildId;
    return this;
  }

  /**
   * Get allyGuildId
   * @return allyGuildId
   */
  
  @Schema(name = "allyGuildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allyGuildId")
  public @Nullable String getAllyGuildId() {
    return allyGuildId;
  }

  public void setAllyGuildId(@Nullable String allyGuildId) {
    this.allyGuildId = allyGuildId;
  }

  public AllianceInfo status(@Nullable StatusEnum status) {
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

  public AllianceInfo createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AllianceInfo allianceInfo = (AllianceInfo) o;
    return Objects.equals(this.allianceId, allianceInfo.allianceId) &&
        Objects.equals(this.allyGuildId, allianceInfo.allyGuildId) &&
        Objects.equals(this.status, allianceInfo.status) &&
        Objects.equals(this.createdAt, allianceInfo.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(allianceId, allyGuildId, status, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AllianceInfo {\n");
    sb.append("    allianceId: ").append(toIndentedString(allianceId)).append("\n");
    sb.append("    allyGuildId: ").append(toIndentedString(allyGuildId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
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

