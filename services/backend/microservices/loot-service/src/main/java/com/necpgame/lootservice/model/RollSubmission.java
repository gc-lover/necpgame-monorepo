package com.necpgame.lootservice.model;

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
 * RollSubmission
 */


public class RollSubmission {

  /**
   * Gets or Sets rollType
   */
  public enum RollTypeEnum {
    NEED("NEED"),
    
    GREED("GREED"),
    
    PASS("PASS"),
    
    AUTO("AUTO");

    private final String value;

    RollTypeEnum(String value) {
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
    public static RollTypeEnum fromValue(String value) {
      for (RollTypeEnum b : RollTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RollTypeEnum rollType;

  private @Nullable Integer value;

  private @Nullable Integer bonus;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime submittedAt;

  public RollSubmission rollType(@Nullable RollTypeEnum rollType) {
    this.rollType = rollType;
    return this;
  }

  /**
   * Get rollType
   * @return rollType
   */
  
  @Schema(name = "rollType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rollType")
  public @Nullable RollTypeEnum getRollType() {
    return rollType;
  }

  public void setRollType(@Nullable RollTypeEnum rollType) {
    this.rollType = rollType;
  }

  public RollSubmission value(@Nullable Integer value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Integer getValue() {
    return value;
  }

  public void setValue(@Nullable Integer value) {
    this.value = value;
  }

  public RollSubmission bonus(@Nullable Integer bonus) {
    this.bonus = bonus;
    return this;
  }

  /**
   * Get bonus
   * @return bonus
   */
  
  @Schema(name = "bonus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonus")
  public @Nullable Integer getBonus() {
    return bonus;
  }

  public void setBonus(@Nullable Integer bonus) {
    this.bonus = bonus;
  }

  public RollSubmission submittedAt(@Nullable OffsetDateTime submittedAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RollSubmission rollSubmission = (RollSubmission) o;
    return Objects.equals(this.rollType, rollSubmission.rollType) &&
        Objects.equals(this.value, rollSubmission.value) &&
        Objects.equals(this.bonus, rollSubmission.bonus) &&
        Objects.equals(this.submittedAt, rollSubmission.submittedAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rollType, value, bonus, submittedAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RollSubmission {\n");
    sb.append("    rollType: ").append(toIndentedString(rollType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    bonus: ").append(toIndentedString(bonus)).append("\n");
    sb.append("    submittedAt: ").append(toIndentedString(submittedAt)).append("\n");
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

