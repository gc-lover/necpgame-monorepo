package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RewardAsset
 */


public class RewardAsset {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CREDITS("CREDITS"),
    
    ITEM("ITEM"),
    
    BLUEPRINT("BLUEPRINT"),
    
    GUILD_FAVOR("GUILD_FAVOR"),
    
    WORLD_FLAG("WORLD_FLAG"),
    
    TITLE("TITLE");

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

  private @Nullable String assetId;

  private @Nullable Integer quantity;

  private @Nullable String quality;

  public RewardAsset type(@Nullable TypeEnum type) {
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

  public RewardAsset assetId(@Nullable String assetId) {
    this.assetId = assetId;
    return this;
  }

  /**
   * Get assetId
   * @return assetId
   */
  
  @Schema(name = "assetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("assetId")
  public @Nullable String getAssetId() {
    return assetId;
  }

  public void setAssetId(@Nullable String assetId) {
    this.assetId = assetId;
  }

  public RewardAsset quantity(@Nullable Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  /**
   * Get quantity
   * minimum: 1
   * @return quantity
   */
  @Min(value = 1) 
  @Schema(name = "quantity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quantity")
  public @Nullable Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(@Nullable Integer quantity) {
    this.quantity = quantity;
  }

  public RewardAsset quality(@Nullable String quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable String getQuality() {
    return quality;
  }

  public void setQuality(@Nullable String quality) {
    this.quality = quality;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RewardAsset rewardAsset = (RewardAsset) o;
    return Objects.equals(this.type, rewardAsset.type) &&
        Objects.equals(this.assetId, rewardAsset.assetId) &&
        Objects.equals(this.quantity, rewardAsset.quantity) &&
        Objects.equals(this.quality, rewardAsset.quality);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, assetId, quantity, quality);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RewardAsset {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    assetId: ").append(toIndentedString(assetId)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
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

