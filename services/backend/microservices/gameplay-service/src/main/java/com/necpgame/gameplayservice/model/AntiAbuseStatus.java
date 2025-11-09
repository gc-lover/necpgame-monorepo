package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * AntiAbuseStatus
 */


public class AntiAbuseStatus {

  /**
   * Gets or Sets flag
   */
  public enum FlagEnum {
    NONE("none"),
    
    SUSPICIOUS_ACTIVITY("suspicious_activity"),
    
    COOLDOWN_ENFORCED("cooldown_enforced"),
    
    SUSPENDED("suspended");

    private final String value;

    FlagEnum(String value) {
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
    public static FlagEnum fromValue(String value) {
      for (FlagEnum b : FlagEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable FlagEnum flag;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime cooldownEndsAt;

  @Valid
  private List<String> violations = new ArrayList<>();

  public AntiAbuseStatus flag(@Nullable FlagEnum flag) {
    this.flag = flag;
    return this;
  }

  /**
   * Get flag
   * @return flag
   */
  
  @Schema(name = "flag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flag")
  public @Nullable FlagEnum getFlag() {
    return flag;
  }

  public void setFlag(@Nullable FlagEnum flag) {
    this.flag = flag;
  }

  public AntiAbuseStatus cooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
    return this;
  }

  /**
   * Get cooldownEndsAt
   * @return cooldownEndsAt
   */
  @Valid 
  @Schema(name = "cooldownEndsAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownEndsAt")
  public @Nullable OffsetDateTime getCooldownEndsAt() {
    return cooldownEndsAt;
  }

  public void setCooldownEndsAt(@Nullable OffsetDateTime cooldownEndsAt) {
    this.cooldownEndsAt = cooldownEndsAt;
  }

  public AntiAbuseStatus violations(List<String> violations) {
    this.violations = violations;
    return this;
  }

  public AntiAbuseStatus addViolationsItem(String violationsItem) {
    if (this.violations == null) {
      this.violations = new ArrayList<>();
    }
    this.violations.add(violationsItem);
    return this;
  }

  /**
   * Get violations
   * @return violations
   */
  
  @Schema(name = "violations", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("violations")
  public List<String> getViolations() {
    return violations;
  }

  public void setViolations(List<String> violations) {
    this.violations = violations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AntiAbuseStatus antiAbuseStatus = (AntiAbuseStatus) o;
    return Objects.equals(this.flag, antiAbuseStatus.flag) &&
        Objects.equals(this.cooldownEndsAt, antiAbuseStatus.cooldownEndsAt) &&
        Objects.equals(this.violations, antiAbuseStatus.violations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flag, cooldownEndsAt, violations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AntiAbuseStatus {\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    cooldownEndsAt: ").append(toIndentedString(cooldownEndsAt)).append("\n");
    sb.append("    violations: ").append(toIndentedString(violations)).append("\n");
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

