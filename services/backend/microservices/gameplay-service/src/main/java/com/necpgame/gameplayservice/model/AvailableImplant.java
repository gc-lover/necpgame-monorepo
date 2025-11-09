package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
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
 * AvailableImplant
 */


public class AvailableImplant {

  private @Nullable String implantId;

  private @Nullable String name;

  private @Nullable String rarity;

  private @Nullable Integer levelRequired;

  @Valid
  private List<String> acquisitionMethods = new ArrayList<>();

  private @Nullable BigDecimal estimatedCost;

  public AvailableImplant implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public AvailableImplant name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public AvailableImplant rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public AvailableImplant levelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
    return this;
  }

  /**
   * Get levelRequired
   * @return levelRequired
   */
  
  @Schema(name = "level_required", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("level_required")
  public @Nullable Integer getLevelRequired() {
    return levelRequired;
  }

  public void setLevelRequired(@Nullable Integer levelRequired) {
    this.levelRequired = levelRequired;
  }

  public AvailableImplant acquisitionMethods(List<String> acquisitionMethods) {
    this.acquisitionMethods = acquisitionMethods;
    return this;
  }

  public AvailableImplant addAcquisitionMethodsItem(String acquisitionMethodsItem) {
    if (this.acquisitionMethods == null) {
      this.acquisitionMethods = new ArrayList<>();
    }
    this.acquisitionMethods.add(acquisitionMethodsItem);
    return this;
  }

  /**
   * Get acquisitionMethods
   * @return acquisitionMethods
   */
  
  @Schema(name = "acquisition_methods", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("acquisition_methods")
  public List<String> getAcquisitionMethods() {
    return acquisitionMethods;
  }

  public void setAcquisitionMethods(List<String> acquisitionMethods) {
    this.acquisitionMethods = acquisitionMethods;
  }

  public AvailableImplant estimatedCost(@Nullable BigDecimal estimatedCost) {
    this.estimatedCost = estimatedCost;
    return this;
  }

  /**
   * Get estimatedCost
   * @return estimatedCost
   */
  @Valid 
  @Schema(name = "estimated_cost", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_cost")
  public @Nullable BigDecimal getEstimatedCost() {
    return estimatedCost;
  }

  public void setEstimatedCost(@Nullable BigDecimal estimatedCost) {
    this.estimatedCost = estimatedCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AvailableImplant availableImplant = (AvailableImplant) o;
    return Objects.equals(this.implantId, availableImplant.implantId) &&
        Objects.equals(this.name, availableImplant.name) &&
        Objects.equals(this.rarity, availableImplant.rarity) &&
        Objects.equals(this.levelRequired, availableImplant.levelRequired) &&
        Objects.equals(this.acquisitionMethods, availableImplant.acquisitionMethods) &&
        Objects.equals(this.estimatedCost, availableImplant.estimatedCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, name, rarity, levelRequired, acquisitionMethods, estimatedCost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AvailableImplant {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    levelRequired: ").append(toIndentedString(levelRequired)).append("\n");
    sb.append("    acquisitionMethods: ").append(toIndentedString(acquisitionMethods)).append("\n");
    sb.append("    estimatedCost: ").append(toIndentedString(estimatedCost)).append("\n");
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

