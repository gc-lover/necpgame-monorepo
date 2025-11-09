package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.RewardItem;
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
 * PromotionRequest
 */


public class PromotionRequest {

  /**
   * Gets or Sets targetRarity
   */
  public enum TargetRarityEnum {
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    MYTHIC("mythic");

    private final String value;

    TargetRarityEnum(String value) {
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
    public static TargetRarityEnum fromValue(String value) {
      for (TargetRarityEnum b : TargetRarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TargetRarityEnum targetRarity;

  @Valid
  private List<String> requirements = new ArrayList<>();

  @Valid
  private List<@Valid RewardItem> consumeItems = new ArrayList<>();

  public PromotionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PromotionRequest(TargetRarityEnum targetRarity) {
    this.targetRarity = targetRarity;
  }

  public PromotionRequest targetRarity(TargetRarityEnum targetRarity) {
    this.targetRarity = targetRarity;
    return this;
  }

  /**
   * Get targetRarity
   * @return targetRarity
   */
  @NotNull 
  @Schema(name = "targetRarity", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetRarity")
  public TargetRarityEnum getTargetRarity() {
    return targetRarity;
  }

  public void setTargetRarity(TargetRarityEnum targetRarity) {
    this.targetRarity = targetRarity;
  }

  public PromotionRequest requirements(List<String> requirements) {
    this.requirements = requirements;
    return this;
  }

  public PromotionRequest addRequirementsItem(String requirementsItem) {
    if (this.requirements == null) {
      this.requirements = new ArrayList<>();
    }
    this.requirements.add(requirementsItem);
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public List<String> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<String> requirements) {
    this.requirements = requirements;
  }

  public PromotionRequest consumeItems(List<@Valid RewardItem> consumeItems) {
    this.consumeItems = consumeItems;
    return this;
  }

  public PromotionRequest addConsumeItemsItem(RewardItem consumeItemsItem) {
    if (this.consumeItems == null) {
      this.consumeItems = new ArrayList<>();
    }
    this.consumeItems.add(consumeItemsItem);
    return this;
  }

  /**
   * Get consumeItems
   * @return consumeItems
   */
  @Valid 
  @Schema(name = "consumeItems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consumeItems")
  public List<@Valid RewardItem> getConsumeItems() {
    return consumeItems;
  }

  public void setConsumeItems(List<@Valid RewardItem> consumeItems) {
    this.consumeItems = consumeItems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PromotionRequest promotionRequest = (PromotionRequest) o;
    return Objects.equals(this.targetRarity, promotionRequest.targetRarity) &&
        Objects.equals(this.requirements, promotionRequest.requirements) &&
        Objects.equals(this.consumeItems, promotionRequest.consumeItems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetRarity, requirements, consumeItems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PromotionRequest {\n");
    sb.append("    targetRarity: ").append(toIndentedString(targetRarity)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    consumeItems: ").append(toIndentedString(consumeItems)).append("\n");
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

