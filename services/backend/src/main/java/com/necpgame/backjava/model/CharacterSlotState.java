package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSlotStateNextTierCost;
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
 * CharacterSlotState
 */


public class CharacterSlotState {

  private Integer totalSlots;

  private Integer usedSlots;

  private Integer premiumSlotsPurchased;

  private Integer maxSlots;

  private JsonNullable<CharacterSlotStateNextTierCost> nextTierCost = JsonNullable.<CharacterSlotStateNextTierCost>undefined();

  @Valid
  private List<String> restrictions = new ArrayList<>();

  public CharacterSlotState() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSlotState(Integer totalSlots, Integer usedSlots, Integer premiumSlotsPurchased, Integer maxSlots, CharacterSlotStateNextTierCost nextTierCost) {
    this.totalSlots = totalSlots;
    this.usedSlots = usedSlots;
    this.premiumSlotsPurchased = premiumSlotsPurchased;
    this.maxSlots = maxSlots;
    this.nextTierCost = JsonNullable.of(nextTierCost);
  }

  public CharacterSlotState totalSlots(Integer totalSlots) {
    this.totalSlots = totalSlots;
    return this;
  }

  /**
   * Get totalSlots
   * minimum: 0
   * @return totalSlots
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "totalSlots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalSlots")
  public Integer getTotalSlots() {
    return totalSlots;
  }

  public void setTotalSlots(Integer totalSlots) {
    this.totalSlots = totalSlots;
  }

  public CharacterSlotState usedSlots(Integer usedSlots) {
    this.usedSlots = usedSlots;
    return this;
  }

  /**
   * Get usedSlots
   * minimum: 0
   * @return usedSlots
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "usedSlots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("usedSlots")
  public Integer getUsedSlots() {
    return usedSlots;
  }

  public void setUsedSlots(Integer usedSlots) {
    this.usedSlots = usedSlots;
  }

  public CharacterSlotState premiumSlotsPurchased(Integer premiumSlotsPurchased) {
    this.premiumSlotsPurchased = premiumSlotsPurchased;
    return this;
  }

  /**
   * Get premiumSlotsPurchased
   * minimum: 0
   * @return premiumSlotsPurchased
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "premiumSlotsPurchased", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("premiumSlotsPurchased")
  public Integer getPremiumSlotsPurchased() {
    return premiumSlotsPurchased;
  }

  public void setPremiumSlotsPurchased(Integer premiumSlotsPurchased) {
    this.premiumSlotsPurchased = premiumSlotsPurchased;
  }

  public CharacterSlotState maxSlots(Integer maxSlots) {
    this.maxSlots = maxSlots;
    return this;
  }

  /**
   * Get maxSlots
   * minimum: 0
   * @return maxSlots
   */
  @NotNull @Min(value = 0) 
  @Schema(name = "maxSlots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxSlots")
  public Integer getMaxSlots() {
    return maxSlots;
  }

  public void setMaxSlots(Integer maxSlots) {
    this.maxSlots = maxSlots;
  }

  public CharacterSlotState nextTierCost(CharacterSlotStateNextTierCost nextTierCost) {
    this.nextTierCost = JsonNullable.of(nextTierCost);
    return this;
  }

  /**
   * Get nextTierCost
   * @return nextTierCost
   */
  @NotNull @Valid 
  @Schema(name = "nextTierCost", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nextTierCost")
  public JsonNullable<CharacterSlotStateNextTierCost> getNextTierCost() {
    return nextTierCost;
  }

  public void setNextTierCost(JsonNullable<CharacterSlotStateNextTierCost> nextTierCost) {
    this.nextTierCost = nextTierCost;
  }

  public CharacterSlotState restrictions(List<String> restrictions) {
    this.restrictions = restrictions;
    return this;
  }

  public CharacterSlotState addRestrictionsItem(String restrictionsItem) {
    if (this.restrictions == null) {
      this.restrictions = new ArrayList<>();
    }
    this.restrictions.add(restrictionsItem);
    return this;
  }

  /**
   * Get restrictions
   * @return restrictions
   */
  
  @Schema(name = "restrictions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restrictions")
  public List<String> getRestrictions() {
    return restrictions;
  }

  public void setRestrictions(List<String> restrictions) {
    this.restrictions = restrictions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSlotState characterSlotState = (CharacterSlotState) o;
    return Objects.equals(this.totalSlots, characterSlotState.totalSlots) &&
        Objects.equals(this.usedSlots, characterSlotState.usedSlots) &&
        Objects.equals(this.premiumSlotsPurchased, characterSlotState.premiumSlotsPurchased) &&
        Objects.equals(this.maxSlots, characterSlotState.maxSlots) &&
        Objects.equals(this.nextTierCost, characterSlotState.nextTierCost) &&
        Objects.equals(this.restrictions, characterSlotState.restrictions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalSlots, usedSlots, premiumSlotsPurchased, maxSlots, nextTierCost, restrictions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSlotState {\n");
    sb.append("    totalSlots: ").append(toIndentedString(totalSlots)).append("\n");
    sb.append("    usedSlots: ").append(toIndentedString(usedSlots)).append("\n");
    sb.append("    premiumSlotsPurchased: ").append(toIndentedString(premiumSlotsPurchased)).append("\n");
    sb.append("    maxSlots: ").append(toIndentedString(maxSlots)).append("\n");
    sb.append("    nextTierCost: ").append(toIndentedString(nextTierCost)).append("\n");
    sb.append("    restrictions: ").append(toIndentedString(restrictions)).append("\n");
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

