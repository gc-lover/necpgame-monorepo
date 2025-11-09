package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.LootItem;
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
 * ExtractionResult
 */


public class ExtractionResult {

  private @Nullable Boolean success;

  @Valid
  private List<@Valid LootItem> lootSecured = new ArrayList<>();

  private @Nullable BigDecimal experienceGained;

  private @Nullable BigDecimal timeInZone;

  public ExtractionResult success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public ExtractionResult lootSecured(List<@Valid LootItem> lootSecured) {
    this.lootSecured = lootSecured;
    return this;
  }

  public ExtractionResult addLootSecuredItem(LootItem lootSecuredItem) {
    if (this.lootSecured == null) {
      this.lootSecured = new ArrayList<>();
    }
    this.lootSecured.add(lootSecuredItem);
    return this;
  }

  /**
   * Сохраненный лут
   * @return lootSecured
   */
  @Valid 
  @Schema(name = "loot_secured", description = "Сохраненный лут", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loot_secured")
  public List<@Valid LootItem> getLootSecured() {
    return lootSecured;
  }

  public void setLootSecured(List<@Valid LootItem> lootSecured) {
    this.lootSecured = lootSecured;
  }

  public ExtractionResult experienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  @Valid 
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable BigDecimal getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable BigDecimal experienceGained) {
    this.experienceGained = experienceGained;
  }

  public ExtractionResult timeInZone(@Nullable BigDecimal timeInZone) {
    this.timeInZone = timeInZone;
    return this;
  }

  /**
   * Get timeInZone
   * @return timeInZone
   */
  @Valid 
  @Schema(name = "time_in_zone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time_in_zone")
  public @Nullable BigDecimal getTimeInZone() {
    return timeInZone;
  }

  public void setTimeInZone(@Nullable BigDecimal timeInZone) {
    this.timeInZone = timeInZone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtractionResult extractionResult = (ExtractionResult) o;
    return Objects.equals(this.success, extractionResult.success) &&
        Objects.equals(this.lootSecured, extractionResult.lootSecured) &&
        Objects.equals(this.experienceGained, extractionResult.experienceGained) &&
        Objects.equals(this.timeInZone, extractionResult.timeInZone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, lootSecured, experienceGained, timeInZone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtractionResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    lootSecured: ").append(toIndentedString(lootSecured)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
    sb.append("    timeInZone: ").append(toIndentedString(timeInZone)).append("\n");
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

