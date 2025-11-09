package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompatibilityResultSetBonusesInner
 */

@JsonTypeName("CompatibilityResult_set_bonuses_inner")

public class CompatibilityResultSetBonusesInner {

  private @Nullable String setName;

  private @Nullable Integer pieces;

  @Valid
  private List<String> activeBonuses = new ArrayList<>();

  public CompatibilityResultSetBonusesInner setName(@Nullable String setName) {
    this.setName = setName;
    return this;
  }

  /**
   * Get setName
   * @return setName
   */
  
  @Schema(name = "set_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("set_name")
  public @Nullable String getSetName() {
    return setName;
  }

  public void setSetName(@Nullable String setName) {
    this.setName = setName;
  }

  public CompatibilityResultSetBonusesInner pieces(@Nullable Integer pieces) {
    this.pieces = pieces;
    return this;
  }

  /**
   * Get pieces
   * @return pieces
   */
  
  @Schema(name = "pieces", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pieces")
  public @Nullable Integer getPieces() {
    return pieces;
  }

  public void setPieces(@Nullable Integer pieces) {
    this.pieces = pieces;
  }

  public CompatibilityResultSetBonusesInner activeBonuses(List<String> activeBonuses) {
    this.activeBonuses = activeBonuses;
    return this;
  }

  public CompatibilityResultSetBonusesInner addActiveBonusesItem(String activeBonusesItem) {
    if (this.activeBonuses == null) {
      this.activeBonuses = new ArrayList<>();
    }
    this.activeBonuses.add(activeBonusesItem);
    return this;
  }

  /**
   * Get activeBonuses
   * @return activeBonuses
   */
  
  @Schema(name = "active_bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_bonuses")
  public List<String> getActiveBonuses() {
    return activeBonuses;
  }

  public void setActiveBonuses(List<String> activeBonuses) {
    this.activeBonuses = activeBonuses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompatibilityResultSetBonusesInner compatibilityResultSetBonusesInner = (CompatibilityResultSetBonusesInner) o;
    return Objects.equals(this.setName, compatibilityResultSetBonusesInner.setName) &&
        Objects.equals(this.pieces, compatibilityResultSetBonusesInner.pieces) &&
        Objects.equals(this.activeBonuses, compatibilityResultSetBonusesInner.activeBonuses);
  }

  @Override
  public int hashCode() {
    return Objects.hash(setName, pieces, activeBonuses);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompatibilityResultSetBonusesInner {\n");
    sb.append("    setName: ").append(toIndentedString(setName)).append("\n");
    sb.append("    pieces: ").append(toIndentedString(pieces)).append("\n");
    sb.append("    activeBonuses: ").append(toIndentedString(activeBonuses)).append("\n");
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

