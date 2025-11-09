package com.necpgame.lootservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.lootservice.model.LootConfigResponseDropCapsInner;
import com.necpgame.lootservice.model.LootConfigResponseSeasonalRulesInner;
import com.necpgame.lootservice.model.LootModifier;
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
 * LootConfigResponse
 */


public class LootConfigResponse {

  @Valid
  private List<@Valid LootModifier> globalModifiers = new ArrayList<>();

  @Valid
  private List<@Valid LootConfigResponseSeasonalRulesInner> seasonalRules = new ArrayList<>();

  @Valid
  private List<@Valid LootConfigResponseDropCapsInner> dropCaps = new ArrayList<>();

  public LootConfigResponse globalModifiers(List<@Valid LootModifier> globalModifiers) {
    this.globalModifiers = globalModifiers;
    return this;
  }

  public LootConfigResponse addGlobalModifiersItem(LootModifier globalModifiersItem) {
    if (this.globalModifiers == null) {
      this.globalModifiers = new ArrayList<>();
    }
    this.globalModifiers.add(globalModifiersItem);
    return this;
  }

  /**
   * Get globalModifiers
   * @return globalModifiers
   */
  @Valid 
  @Schema(name = "globalModifiers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("globalModifiers")
  public List<@Valid LootModifier> getGlobalModifiers() {
    return globalModifiers;
  }

  public void setGlobalModifiers(List<@Valid LootModifier> globalModifiers) {
    this.globalModifiers = globalModifiers;
  }

  public LootConfigResponse seasonalRules(List<@Valid LootConfigResponseSeasonalRulesInner> seasonalRules) {
    this.seasonalRules = seasonalRules;
    return this;
  }

  public LootConfigResponse addSeasonalRulesItem(LootConfigResponseSeasonalRulesInner seasonalRulesItem) {
    if (this.seasonalRules == null) {
      this.seasonalRules = new ArrayList<>();
    }
    this.seasonalRules.add(seasonalRulesItem);
    return this;
  }

  /**
   * Get seasonalRules
   * @return seasonalRules
   */
  @Valid 
  @Schema(name = "seasonalRules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seasonalRules")
  public List<@Valid LootConfigResponseSeasonalRulesInner> getSeasonalRules() {
    return seasonalRules;
  }

  public void setSeasonalRules(List<@Valid LootConfigResponseSeasonalRulesInner> seasonalRules) {
    this.seasonalRules = seasonalRules;
  }

  public LootConfigResponse dropCaps(List<@Valid LootConfigResponseDropCapsInner> dropCaps) {
    this.dropCaps = dropCaps;
    return this;
  }

  public LootConfigResponse addDropCapsItem(LootConfigResponseDropCapsInner dropCapsItem) {
    if (this.dropCaps == null) {
      this.dropCaps = new ArrayList<>();
    }
    this.dropCaps.add(dropCapsItem);
    return this;
  }

  /**
   * Get dropCaps
   * @return dropCaps
   */
  @Valid 
  @Schema(name = "dropCaps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dropCaps")
  public List<@Valid LootConfigResponseDropCapsInner> getDropCaps() {
    return dropCaps;
  }

  public void setDropCaps(List<@Valid LootConfigResponseDropCapsInner> dropCaps) {
    this.dropCaps = dropCaps;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootConfigResponse lootConfigResponse = (LootConfigResponse) o;
    return Objects.equals(this.globalModifiers, lootConfigResponse.globalModifiers) &&
        Objects.equals(this.seasonalRules, lootConfigResponse.seasonalRules) &&
        Objects.equals(this.dropCaps, lootConfigResponse.dropCaps);
  }

  @Override
  public int hashCode() {
    return Objects.hash(globalModifiers, seasonalRules, dropCaps);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootConfigResponse {\n");
    sb.append("    globalModifiers: ").append(toIndentedString(globalModifiers)).append("\n");
    sb.append("    seasonalRules: ").append(toIndentedString(seasonalRules)).append("\n");
    sb.append("    dropCaps: ").append(toIndentedString(dropCaps)).append("\n");
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

