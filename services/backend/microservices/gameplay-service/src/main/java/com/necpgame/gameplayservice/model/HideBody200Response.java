package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * HideBody200Response
 */

@JsonTypeName("hideBody_200_response")

public class HideBody200Response {

  private @Nullable Boolean success;

  /**
   * Gets or Sets hidingQuality
   */
  public enum HidingQualityEnum {
    PERFECT("perfect"),
    
    GOOD("good"),
    
    POOR("poor");

    private final String value;

    HidingQualityEnum(String value) {
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
    public static HidingQualityEnum fromValue(String value) {
      for (HidingQualityEnum b : HidingQualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable HidingQualityEnum hidingQuality;

  private @Nullable BigDecimal detectionChance;

  public HideBody200Response success(@Nullable Boolean success) {
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

  public HideBody200Response hidingQuality(@Nullable HidingQualityEnum hidingQuality) {
    this.hidingQuality = hidingQuality;
    return this;
  }

  /**
   * Get hidingQuality
   * @return hidingQuality
   */
  
  @Schema(name = "hiding_quality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hiding_quality")
  public @Nullable HidingQualityEnum getHidingQuality() {
    return hidingQuality;
  }

  public void setHidingQuality(@Nullable HidingQualityEnum hidingQuality) {
    this.hidingQuality = hidingQuality;
  }

  public HideBody200Response detectionChance(@Nullable BigDecimal detectionChance) {
    this.detectionChance = detectionChance;
    return this;
  }

  /**
   * Шанс обнаружения (%)
   * @return detectionChance
   */
  @Valid 
  @Schema(name = "detection_chance", description = "Шанс обнаружения (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("detection_chance")
  public @Nullable BigDecimal getDetectionChance() {
    return detectionChance;
  }

  public void setDetectionChance(@Nullable BigDecimal detectionChance) {
    this.detectionChance = detectionChance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HideBody200Response hideBody200Response = (HideBody200Response) o;
    return Objects.equals(this.success, hideBody200Response.success) &&
        Objects.equals(this.hidingQuality, hideBody200Response.hidingQuality) &&
        Objects.equals(this.detectionChance, hideBody200Response.detectionChance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, hidingQuality, detectionChance);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HideBody200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    hidingQuality: ").append(toIndentedString(hidingQuality)).append("\n");
    sb.append("    detectionChance: ").append(toIndentedString(detectionChance)).append("\n");
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

