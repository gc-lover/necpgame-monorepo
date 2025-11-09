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
 * WarDeclarationResponse
 */


public class WarDeclarationResponse {

  private @Nullable String warId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    DECLARED("DECLARED"),
    
    AWAITING_CONFIRMATION("AWAITING_CONFIRMATION");

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
  private @Nullable OffsetDateTime preparationEndsAt;

  private @Nullable Integer cost;

  public WarDeclarationResponse warId(@Nullable String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warId")
  public @Nullable String getWarId() {
    return warId;
  }

  public void setWarId(@Nullable String warId) {
    this.warId = warId;
  }

  public WarDeclarationResponse status(@Nullable StatusEnum status) {
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

  public WarDeclarationResponse preparationEndsAt(@Nullable OffsetDateTime preparationEndsAt) {
    this.preparationEndsAt = preparationEndsAt;
    return this;
  }

  /**
   * Get preparationEndsAt
   * @return preparationEndsAt
   */
  @Valid 
  @Schema(name = "preparationEndsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preparationEndsAt")
  public @Nullable OffsetDateTime getPreparationEndsAt() {
    return preparationEndsAt;
  }

  public void setPreparationEndsAt(@Nullable OffsetDateTime preparationEndsAt) {
    this.preparationEndsAt = preparationEndsAt;
  }

  public WarDeclarationResponse cost(@Nullable Integer cost) {
    this.cost = cost;
    return this;
  }

  /**
   * Get cost
   * @return cost
   */
  
  @Schema(name = "cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cost")
  public @Nullable Integer getCost() {
    return cost;
  }

  public void setCost(@Nullable Integer cost) {
    this.cost = cost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WarDeclarationResponse warDeclarationResponse = (WarDeclarationResponse) o;
    return Objects.equals(this.warId, warDeclarationResponse.warId) &&
        Objects.equals(this.status, warDeclarationResponse.status) &&
        Objects.equals(this.preparationEndsAt, warDeclarationResponse.preparationEndsAt) &&
        Objects.equals(this.cost, warDeclarationResponse.cost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, status, preparationEndsAt, cost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WarDeclarationResponse {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    preparationEndsAt: ").append(toIndentedString(preparationEndsAt)).append("\n");
    sb.append("    cost: ").append(toIndentedString(cost)).append("\n");
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

