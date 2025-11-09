package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Tier;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TierChange
 */


public class TierChange {

  private @Nullable Tier previousTier;

  private @Nullable Integer previousDivision;

  private @Nullable Tier newTier;

  private @Nullable Integer newDivision;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    PROMOTION("PROMOTION"),
    
    DEMOTION("DEMOTION"),
    
    STAY("STAY");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  public TierChange previousTier(@Nullable Tier previousTier) {
    this.previousTier = previousTier;
    return this;
  }

  /**
   * Get previousTier
   * @return previousTier
   */
  @Valid 
  @Schema(name = "previousTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousTier")
  public @Nullable Tier getPreviousTier() {
    return previousTier;
  }

  public void setPreviousTier(@Nullable Tier previousTier) {
    this.previousTier = previousTier;
  }

  public TierChange previousDivision(@Nullable Integer previousDivision) {
    this.previousDivision = previousDivision;
    return this;
  }

  /**
   * Get previousDivision
   * minimum: 1
   * maximum: 5
   * @return previousDivision
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "previousDivision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousDivision")
  public @Nullable Integer getPreviousDivision() {
    return previousDivision;
  }

  public void setPreviousDivision(@Nullable Integer previousDivision) {
    this.previousDivision = previousDivision;
  }

  public TierChange newTier(@Nullable Tier newTier) {
    this.newTier = newTier;
    return this;
  }

  /**
   * Get newTier
   * @return newTier
   */
  @Valid 
  @Schema(name = "newTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newTier")
  public @Nullable Tier getNewTier() {
    return newTier;
  }

  public void setNewTier(@Nullable Tier newTier) {
    this.newTier = newTier;
  }

  public TierChange newDivision(@Nullable Integer newDivision) {
    this.newDivision = newDivision;
    return this;
  }

  /**
   * Get newDivision
   * minimum: 1
   * maximum: 5
   * @return newDivision
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "newDivision", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newDivision")
  public @Nullable Integer getNewDivision() {
    return newDivision;
  }

  public void setNewDivision(@Nullable Integer newDivision) {
    this.newDivision = newDivision;
  }

  public TierChange type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TierChange tierChange = (TierChange) o;
    return Objects.equals(this.previousTier, tierChange.previousTier) &&
        Objects.equals(this.previousDivision, tierChange.previousDivision) &&
        Objects.equals(this.newTier, tierChange.newTier) &&
        Objects.equals(this.newDivision, tierChange.newDivision) &&
        Objects.equals(this.type, tierChange.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previousTier, previousDivision, newTier, newDivision, type);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TierChange {\n");
    sb.append("    previousTier: ").append(toIndentedString(previousTier)).append("\n");
    sb.append("    previousDivision: ").append(toIndentedString(previousDivision)).append("\n");
    sb.append("    newTier: ").append(toIndentedString(newTier)).append("\n");
    sb.append("    newDivision: ").append(toIndentedString(newDivision)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

