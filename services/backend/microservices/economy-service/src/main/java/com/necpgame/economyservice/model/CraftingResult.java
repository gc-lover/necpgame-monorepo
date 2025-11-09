package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.economyservice.model.ComponentRequirementAlternativesInner;
import com.necpgame.economyservice.model.CraftingResultItemsCraftedInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftingResult
 */


public class CraftingResult {

  private @Nullable UUID sessionId;

  private @Nullable Boolean success;

  @Valid
  private List<@Valid CraftingResultItemsCraftedInner> itemsCrafted = new ArrayList<>();

  private @Nullable Integer experienceGained;

  @Valid
  private List<@Valid ComponentRequirementAlternativesInner> componentsConsumed = new ArrayList<>();

  public CraftingResult sessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @Valid 
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable UUID getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable UUID sessionId) {
    this.sessionId = sessionId;
  }

  public CraftingResult success(@Nullable Boolean success) {
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

  public CraftingResult itemsCrafted(List<@Valid CraftingResultItemsCraftedInner> itemsCrafted) {
    this.itemsCrafted = itemsCrafted;
    return this;
  }

  public CraftingResult addItemsCraftedItem(CraftingResultItemsCraftedInner itemsCraftedItem) {
    if (this.itemsCrafted == null) {
      this.itemsCrafted = new ArrayList<>();
    }
    this.itemsCrafted.add(itemsCraftedItem);
    return this;
  }

  /**
   * Get itemsCrafted
   * @return itemsCrafted
   */
  @Valid 
  @Schema(name = "items_crafted", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("items_crafted")
  public List<@Valid CraftingResultItemsCraftedInner> getItemsCrafted() {
    return itemsCrafted;
  }

  public void setItemsCrafted(List<@Valid CraftingResultItemsCraftedInner> itemsCrafted) {
    this.itemsCrafted = itemsCrafted;
  }

  public CraftingResult experienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
    return this;
  }

  /**
   * Get experienceGained
   * @return experienceGained
   */
  
  @Schema(name = "experience_gained", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("experience_gained")
  public @Nullable Integer getExperienceGained() {
    return experienceGained;
  }

  public void setExperienceGained(@Nullable Integer experienceGained) {
    this.experienceGained = experienceGained;
  }

  public CraftingResult componentsConsumed(List<@Valid ComponentRequirementAlternativesInner> componentsConsumed) {
    this.componentsConsumed = componentsConsumed;
    return this;
  }

  public CraftingResult addComponentsConsumedItem(ComponentRequirementAlternativesInner componentsConsumedItem) {
    if (this.componentsConsumed == null) {
      this.componentsConsumed = new ArrayList<>();
    }
    this.componentsConsumed.add(componentsConsumedItem);
    return this;
  }

  /**
   * Get componentsConsumed
   * @return componentsConsumed
   */
  @Valid 
  @Schema(name = "components_consumed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("components_consumed")
  public List<@Valid ComponentRequirementAlternativesInner> getComponentsConsumed() {
    return componentsConsumed;
  }

  public void setComponentsConsumed(List<@Valid ComponentRequirementAlternativesInner> componentsConsumed) {
    this.componentsConsumed = componentsConsumed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftingResult craftingResult = (CraftingResult) o;
    return Objects.equals(this.sessionId, craftingResult.sessionId) &&
        Objects.equals(this.success, craftingResult.success) &&
        Objects.equals(this.itemsCrafted, craftingResult.itemsCrafted) &&
        Objects.equals(this.experienceGained, craftingResult.experienceGained) &&
        Objects.equals(this.componentsConsumed, craftingResult.componentsConsumed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, success, itemsCrafted, experienceGained, componentsConsumed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftingResult {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    itemsCrafted: ").append(toIndentedString(itemsCrafted)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
    sb.append("    componentsConsumed: ").append(toIndentedString(componentsConsumed)).append("\n");
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

