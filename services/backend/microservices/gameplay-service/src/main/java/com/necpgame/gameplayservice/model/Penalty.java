package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Penalty
 */


public class Penalty {

  /**
   * Gets or Sets penaltyType
   */
  public enum PenaltyTypeEnum {
    ECONOMIC("ECONOMIC"),
    
    COOLDOWN("COOLDOWN"),
    
    RESTRICTION("RESTRICTION");

    private final String value;

    PenaltyTypeEnum(String value) {
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
    public static PenaltyTypeEnum fromValue(String value) {
      for (PenaltyTypeEnum b : PenaltyTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PenaltyTypeEnum penaltyType;

  private @Nullable Integer value;

  private @Nullable Integer durationHours;

  /**
   * Gets or Sets appliesTo
   */
  public enum AppliesToEnum {
    ATTACKER("attacker"),
    
    DEFENDER("defender"),
    
    ALLIES("allies");

    private final String value;

    AppliesToEnum(String value) {
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
    public static AppliesToEnum fromValue(String value) {
      for (AppliesToEnum b : AppliesToEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AppliesToEnum appliesTo;

  public Penalty() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Penalty(PenaltyTypeEnum penaltyType) {
    this.penaltyType = penaltyType;
  }

  public Penalty penaltyType(PenaltyTypeEnum penaltyType) {
    this.penaltyType = penaltyType;
    return this;
  }

  /**
   * Get penaltyType
   * @return penaltyType
   */
  @NotNull 
  @Schema(name = "penaltyType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("penaltyType")
  public PenaltyTypeEnum getPenaltyType() {
    return penaltyType;
  }

  public void setPenaltyType(PenaltyTypeEnum penaltyType) {
    this.penaltyType = penaltyType;
  }

  public Penalty value(@Nullable Integer value) {
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

  public Penalty durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * @return durationHours
   */
  
  @Schema(name = "durationHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationHours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
  }

  public Penalty appliesTo(@Nullable AppliesToEnum appliesTo) {
    this.appliesTo = appliesTo;
    return this;
  }

  /**
   * Get appliesTo
   * @return appliesTo
   */
  
  @Schema(name = "appliesTo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appliesTo")
  public @Nullable AppliesToEnum getAppliesTo() {
    return appliesTo;
  }

  public void setAppliesTo(@Nullable AppliesToEnum appliesTo) {
    this.appliesTo = appliesTo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Penalty penalty = (Penalty) o;
    return Objects.equals(this.penaltyType, penalty.penaltyType) &&
        Objects.equals(this.value, penalty.value) &&
        Objects.equals(this.durationHours, penalty.durationHours) &&
        Objects.equals(this.appliesTo, penalty.appliesTo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(penaltyType, value, durationHours, appliesTo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Penalty {\n");
    sb.append("    penaltyType: ").append(toIndentedString(penaltyType)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    appliesTo: ").append(toIndentedString(appliesTo)).append("\n");
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

