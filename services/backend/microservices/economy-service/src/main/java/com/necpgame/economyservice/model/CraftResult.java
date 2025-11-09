package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CraftResult
 */


public class CraftResult {

  private @Nullable Boolean success;

  private @Nullable Object itemCreated;

  /**
   * Gets or Sets quality
   */
  public enum QualityEnum {
    FAILED("failed"),
    
    NORMAL("normal"),
    
    HIGH_QUALITY("high_quality"),
    
    MASTERWORK("masterwork");

    private final String value;

    QualityEnum(String value) {
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
    public static QualityEnum fromValue(String value) {
      for (QualityEnum b : QualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable QualityEnum quality;

  private @Nullable BigDecimal experienceGained;

  public CraftResult success(@Nullable Boolean success) {
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

  public CraftResult itemCreated(@Nullable Object itemCreated) {
    this.itemCreated = itemCreated;
    return this;
  }

  /**
   * Созданный предмет
   * @return itemCreated
   */
  
  @Schema(name = "item_created", description = "Созданный предмет", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("item_created")
  public @Nullable Object getItemCreated() {
    return itemCreated;
  }

  public void setItemCreated(@Nullable Object itemCreated) {
    this.itemCreated = itemCreated;
  }

  public CraftResult quality(@Nullable QualityEnum quality) {
    this.quality = quality;
    return this;
  }

  /**
   * Get quality
   * @return quality
   */
  
  @Schema(name = "quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quality")
  public @Nullable QualityEnum getQuality() {
    return quality;
  }

  public void setQuality(@Nullable QualityEnum quality) {
    this.quality = quality;
  }

  public CraftResult experienceGained(@Nullable BigDecimal experienceGained) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CraftResult craftResult = (CraftResult) o;
    return Objects.equals(this.success, craftResult.success) &&
        Objects.equals(this.itemCreated, craftResult.itemCreated) &&
        Objects.equals(this.quality, craftResult.quality) &&
        Objects.equals(this.experienceGained, craftResult.experienceGained);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, itemCreated, quality, experienceGained);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CraftResult {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    itemCreated: ").append(toIndentedString(itemCreated)).append("\n");
    sb.append("    quality: ").append(toIndentedString(quality)).append("\n");
    sb.append("    experienceGained: ").append(toIndentedString(experienceGained)).append("\n");
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

