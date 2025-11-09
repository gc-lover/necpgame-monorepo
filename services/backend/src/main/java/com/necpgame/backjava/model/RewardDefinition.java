package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardDefinition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class RewardDefinition {

  private @Nullable String rewardId;

  private Integer level;

  /**
   * Gets or Sets track
   */
  public enum TrackEnum {
    FREE("FREE"),
    
    PREMIUM("PREMIUM");

    private final String value;

    TrackEnum(String value) {
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
    public static TrackEnum fromValue(String value) {
      for (TrackEnum b : TrackEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TrackEnum track;

  /**
   * Gets or Sets rewardType
   */
  public enum RewardTypeEnum {
    CURRENCY("CURRENCY"),
    
    ITEM("ITEM"),
    
    COSMETIC("COSMETIC"),
    
    XP_BOOST("XP_BOOST"),
    
    BUNDLE("BUNDLE");

    private final String value;

    RewardTypeEnum(String value) {
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
    public static RewardTypeEnum fromValue(String value) {
      for (RewardTypeEnum b : RewardTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private RewardTypeEnum rewardType;

  @Valid
  private Map<String, Object> rewardData = new HashMap<>();

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary");

    private final String value;

    RarityEnum(String value) {
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
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  private @Nullable URI previewAsset;

  public RewardDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public RewardDefinition(Integer level, TrackEnum track, RewardTypeEnum rewardType, Map<String, Object> rewardData) {
    this.level = level;
    this.track = track;
    this.rewardType = rewardType;
    this.rewardData = rewardData;
  }

  public RewardDefinition rewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
    return this;
  }

  /**
   * Get rewardId
   * @return rewardId
   */
  
  @Schema(name = "rewardId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rewardId")
  public @Nullable String getRewardId() {
    return rewardId;
  }

  public void setRewardId(@Nullable String rewardId) {
    this.rewardId = rewardId;
  }

  public RewardDefinition level(Integer level) {
    this.level = level;
    return this;
  }

  /**
   * Get level
   * minimum: 1
   * @return level
   */
  @NotNull @Min(value = 1) 
  @Schema(name = "level", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  public RewardDefinition track(TrackEnum track) {
    this.track = track;
    return this;
  }

  /**
   * Get track
   * @return track
   */
  @NotNull 
  @Schema(name = "track", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("track")
  public TrackEnum getTrack() {
    return track;
  }

  public void setTrack(TrackEnum track) {
    this.track = track;
  }

  public RewardDefinition rewardType(RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
    return this;
  }

  /**
   * Get rewardType
   * @return rewardType
   */
  @NotNull 
  @Schema(name = "rewardType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewardType")
  public RewardTypeEnum getRewardType() {
    return rewardType;
  }

  public void setRewardType(RewardTypeEnum rewardType) {
    this.rewardType = rewardType;
  }

  public RewardDefinition rewardData(Map<String, Object> rewardData) {
    this.rewardData = rewardData;
    return this;
  }

  public RewardDefinition putRewardDataItem(String key, Object rewardDataItem) {
    if (this.rewardData == null) {
      this.rewardData = new HashMap<>();
    }
    this.rewardData.put(key, rewardDataItem);
    return this;
  }

  /**
   * Детали награды
   * @return rewardData
   */
  @NotNull 
  @Schema(name = "rewardData", description = "Детали награды", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("rewardData")
  public Map<String, Object> getRewardData() {
    return rewardData;
  }

  public void setRewardData(Map<String, Object> rewardData) {
    this.rewardData = rewardData;
  }

  public RewardDefinition rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public RewardDefinition previewAsset(@Nullable URI previewAsset) {
    this.previewAsset = previewAsset;
    return this;
  }

  /**
   * Get previewAsset
   * @return previewAsset
   */
  @Valid 
  @Schema(name = "previewAsset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previewAsset")
  public @Nullable URI getPreviewAsset() {
    return previewAsset;
  }

  public void setPreviewAsset(@Nullable URI previewAsset) {
    this.previewAsset = previewAsset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardDefinition rewardDefinition = (RewardDefinition) o;
    return Objects.equals(this.rewardId, rewardDefinition.rewardId) &&
        Objects.equals(this.level, rewardDefinition.level) &&
        Objects.equals(this.track, rewardDefinition.track) &&
        Objects.equals(this.rewardType, rewardDefinition.rewardType) &&
        Objects.equals(this.rewardData, rewardDefinition.rewardData) &&
        Objects.equals(this.rarity, rewardDefinition.rarity) &&
        Objects.equals(this.previewAsset, rewardDefinition.previewAsset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rewardId, level, track, rewardType, rewardData, rarity, previewAsset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardDefinition {\n");
    sb.append("    rewardId: ").append(toIndentedString(rewardId)).append("\n");
    sb.append("    level: ").append(toIndentedString(level)).append("\n");
    sb.append("    track: ").append(toIndentedString(track)).append("\n");
    sb.append("    rewardType: ").append(toIndentedString(rewardType)).append("\n");
    sb.append("    rewardData: ").append(toIndentedString(rewardData)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    previewAsset: ").append(toIndentedString(previewAsset)).append("\n");
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

