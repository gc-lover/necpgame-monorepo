package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ Р»РёРјРёС‚Р° РёРјРїР»Р°РЅС‚РѕРІ
 */

@Schema(name = "CalculateLimitRequest", description = "Р—Р°РїСЂРѕСЃ РЅР° СЂР°СЃС‡РµС‚ Р»РёРјРёС‚Р° РёРјРїР»Р°РЅС‚РѕРІ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T19:51:47.912860600+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class CalculateLimitRequest {

  private @Nullable Integer classBonus;

  private @Nullable Integer progressionBonus;

  private @Nullable Float humanityLevel;

  public CalculateLimitRequest classBonus(@Nullable Integer classBonus) {
    this.classBonus = classBonus;
    return this;
  }

  /**
   * Р‘РѕРЅСѓСЃ РѕС‚ РєР»Р°СЃСЃР°
   * minimum: 0
   * @return classBonus
   */
  @Min(value = 0) 
  @Schema(name = "class_bonus", description = "Р‘РѕРЅСѓСЃ РѕС‚ РєР»Р°СЃСЃР°", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("class_bonus")
  public @Nullable Integer getClassBonus() {
    return classBonus;
  }

  public void setClassBonus(@Nullable Integer classBonus) {
    this.classBonus = classBonus;
  }

  public CalculateLimitRequest progressionBonus(@Nullable Integer progressionBonus) {
    this.progressionBonus = progressionBonus;
    return this;
  }

  /**
   * Р‘РѕРЅСѓСЃ РѕС‚ РїСЂРѕРєР°С‡РєРё
   * minimum: 0
   * @return progressionBonus
   */
  @Min(value = 0) 
  @Schema(name = "progression_bonus", description = "Р‘РѕРЅСѓСЃ РѕС‚ РїСЂРѕРєР°С‡РєРё", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression_bonus")
  public @Nullable Integer getProgressionBonus() {
    return progressionBonus;
  }

  public void setProgressionBonus(@Nullable Integer progressionBonus) {
    this.progressionBonus = progressionBonus;
  }

  public CalculateLimitRequest humanityLevel(@Nullable Float humanityLevel) {
    this.humanityLevel = humanityLevel;
    return this;
  }

  /**
   * РЈСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100)
   * minimum: 0
   * maximum: 100
   * @return humanityLevel
   */
  @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "humanity_level", description = "РЈСЂРѕРІРµРЅСЊ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё (0-100)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("humanity_level")
  public @Nullable Float getHumanityLevel() {
    return humanityLevel;
  }

  public void setHumanityLevel(@Nullable Float humanityLevel) {
    this.humanityLevel = humanityLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateLimitRequest calculateLimitRequest = (CalculateLimitRequest) o;
    return Objects.equals(this.classBonus, calculateLimitRequest.classBonus) &&
        Objects.equals(this.progressionBonus, calculateLimitRequest.progressionBonus) &&
        Objects.equals(this.humanityLevel, calculateLimitRequest.humanityLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(classBonus, progressionBonus, humanityLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateLimitRequest {\n");
    sb.append("    classBonus: ").append(toIndentedString(classBonus)).append("\n");
    sb.append("    progressionBonus: ").append(toIndentedString(progressionBonus)).append("\n");
    sb.append("    humanityLevel: ").append(toIndentedString(humanityLevel)).append("\n");
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

