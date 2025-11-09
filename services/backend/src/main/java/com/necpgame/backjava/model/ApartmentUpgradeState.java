package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ApartmentUpgradeStateActiveConstruction;
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
 * ApartmentUpgradeState
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ApartmentUpgradeState {

  /**
   * Gets or Sets upgradeType
   */
  public enum UpgradeTypeEnum {
    ROOM("ROOM"),
    
    STORAGE("STORAGE"),
    
    FUNCTIONAL("FUNCTIONAL");

    private final String value;

    UpgradeTypeEnum(String value) {
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
    public static UpgradeTypeEnum fromValue(String value) {
      for (UpgradeTypeEnum b : UpgradeTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable UpgradeTypeEnum upgradeType;

  private @Nullable Integer level;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime completedAt;

  private @Nullable ApartmentUpgradeStateActiveConstruction activeConstruction;

  public ApartmentUpgradeState upgradeType(@Nullable UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
    return this;
  }

  /**
   * Get upgradeType
   * @return upgradeType
   */
  
  @Schema(name = "upgradeType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("upgradeType")
  public @Nullable UpgradeTypeEnum getUpgradeType() {
    return upgradeType;
  }

  public void setUpgradeType(@Nullable UpgradeTypeEnum upgradeType) {
    this.upgradeType = upgradeType;
  }

  public ApartmentUpgradeState level(@Nullable Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * @return level
   */
  
  @Schema(name = "level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level")
  public @Nullable Integer getLevel() {
    return level;
  }

  public void setLevel(@Nullable Integer level) {
    this.level = level;
  }

  public ApartmentUpgradeState completedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
    return this;
  }

  /**
   * Get completedAt
   * @return completedAt
   */
  @Valid 
  @Schema(name = "completedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("completedAt")
  public @Nullable OffsetDateTime getCompletedAt() {
    return completedAt;
  }

  public void setCompletedAt(@Nullable OffsetDateTime completedAt) {
    this.completedAt = completedAt;
  }

  public ApartmentUpgradeState activeConstruction(@Nullable ApartmentUpgradeStateActiveConstruction activeConstruction) {
    this.activeConstruction = activeConstruction;
    return this;
  }

  /**
   * Get activeConstruction
   * @return activeConstruction
   */
  @Valid 
  @Schema(name = "activeConstruction", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeConstruction")
  public @Nullable ApartmentUpgradeStateActiveConstruction getActiveConstruction() {
    return activeConstruction;
  }

  public void setActiveConstruction(@Nullable ApartmentUpgradeStateActiveConstruction activeConstruction) {
    this.activeConstruction = activeConstruction;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApartmentUpgradeState apartmentUpgradeState = (ApartmentUpgradeState) o;
    return Objects.equals(this.upgradeType, apartmentUpgradeState.upgradeType) &&
        Objects.equals(this.level, apartmentUpgradeState.level) &&
        Objects.equals(this.completedAt, apartmentUpgradeState.completedAt) &&
        Objects.equals(this.activeConstruction, apartmentUpgradeState.activeConstruction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(upgradeType, level, completedAt, activeConstruction);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApartmentUpgradeState {\n");
    sb.append("    upgradeType: ").append(toIndentedString(upgradeType)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    completedAt: ").append(toIndentedString(completedAt)).append("\n");
    sb.append("    activeConstruction: ").append(toIndentedString(activeConstruction)).append("\n");
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

