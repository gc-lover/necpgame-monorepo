package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.StealthStatusEnemiesAware;
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
 * StealthStatus
 */


public class StealthStatus {

  private @Nullable String characterId;

  /**
   * Текущий уровень скрытности
   */
  public enum StealthLevelEnum {
    HIDDEN("hidden"),
    
    SUSPICIOUS("suspicious"),
    
    DETECTED("detected"),
    
    COMBAT("combat");

    private final String value;

    StealthLevelEnum(String value) {
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
    public static StealthLevelEnum fromValue(String value) {
      for (StealthLevelEnum b : StealthLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StealthLevelEnum stealthLevel;

  private @Nullable BigDecimal visibility;

  private @Nullable BigDecimal noiseLevel;

  private @Nullable BigDecimal lightExposure;

  private @Nullable StealthStatusEnemiesAware enemiesAware;

  @Valid
  private List<String> activeEffects = new ArrayList<>();

  public StealthStatus characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public StealthStatus stealthLevel(@Nullable StealthLevelEnum stealthLevel) {
    this.stealthLevel = stealthLevel;
    return this;
  }

  /**
   * Текущий уровень скрытности
   * @return stealthLevel
   */
  
  @Schema(name = "stealth_level", description = "Текущий уровень скрытности", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stealth_level")
  public @Nullable StealthLevelEnum getStealthLevel() {
    return stealthLevel;
  }

  public void setStealthLevel(@Nullable StealthLevelEnum stealthLevel) {
    this.stealthLevel = stealthLevel;
  }

  public StealthStatus visibility(@Nullable BigDecimal visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Видимость персонажа (%)
   * minimum: 0
   * maximum: 100
   * @return visibility
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "visibility", description = "Видимость персонажа (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable BigDecimal getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable BigDecimal visibility) {
    this.visibility = visibility;
  }

  public StealthStatus noiseLevel(@Nullable BigDecimal noiseLevel) {
    this.noiseLevel = noiseLevel;
    return this;
  }

  /**
   * Уровень шума (%)
   * minimum: 0
   * maximum: 100
   * @return noiseLevel
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "noise_level", description = "Уровень шума (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("noise_level")
  public @Nullable BigDecimal getNoiseLevel() {
    return noiseLevel;
  }

  public void setNoiseLevel(@Nullable BigDecimal noiseLevel) {
    this.noiseLevel = noiseLevel;
  }

  public StealthStatus lightExposure(@Nullable BigDecimal lightExposure) {
    this.lightExposure = lightExposure;
    return this;
  }

  /**
   * Освещенность (%)
   * minimum: 0
   * maximum: 100
   * @return lightExposure
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "100") 
  @Schema(name = "light_exposure", description = "Освещенность (%)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("light_exposure")
  public @Nullable BigDecimal getLightExposure() {
    return lightExposure;
  }

  public void setLightExposure(@Nullable BigDecimal lightExposure) {
    this.lightExposure = lightExposure;
  }

  public StealthStatus enemiesAware(@Nullable StealthStatusEnemiesAware enemiesAware) {
    this.enemiesAware = enemiesAware;
    return this;
  }

  /**
   * Get enemiesAware
   * @return enemiesAware
   */
  @Valid 
  @Schema(name = "enemies_aware", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enemies_aware")
  public @Nullable StealthStatusEnemiesAware getEnemiesAware() {
    return enemiesAware;
  }

  public void setEnemiesAware(@Nullable StealthStatusEnemiesAware enemiesAware) {
    this.enemiesAware = enemiesAware;
  }

  public StealthStatus activeEffects(List<String> activeEffects) {
    this.activeEffects = activeEffects;
    return this;
  }

  public StealthStatus addActiveEffectsItem(String activeEffectsItem) {
    if (this.activeEffects == null) {
      this.activeEffects = new ArrayList<>();
    }
    this.activeEffects.add(activeEffectsItem);
    return this;
  }

  /**
   * Активные эффекты (optical_camo, sound_dampener)
   * @return activeEffects
   */
  
  @Schema(name = "active_effects", description = "Активные эффекты (optical_camo, sound_dampener)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_effects")
  public List<String> getActiveEffects() {
    return activeEffects;
  }

  public void setActiveEffects(List<String> activeEffects) {
    this.activeEffects = activeEffects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StealthStatus stealthStatus = (StealthStatus) o;
    return Objects.equals(this.characterId, stealthStatus.characterId) &&
        Objects.equals(this.stealthLevel, stealthStatus.stealthLevel) &&
        Objects.equals(this.visibility, stealthStatus.visibility) &&
        Objects.equals(this.noiseLevel, stealthStatus.noiseLevel) &&
        Objects.equals(this.lightExposure, stealthStatus.lightExposure) &&
        Objects.equals(this.enemiesAware, stealthStatus.enemiesAware) &&
        Objects.equals(this.activeEffects, stealthStatus.activeEffects);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, stealthLevel, visibility, noiseLevel, lightExposure, enemiesAware, activeEffects);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StealthStatus {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    stealthLevel: ").append(toIndentedString(stealthLevel)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    noiseLevel: ").append(toIndentedString(noiseLevel)).append("\n");
    sb.append("    lightExposure: ").append(toIndentedString(lightExposure)).append("\n");
    sb.append("    enemiesAware: ").append(toIndentedString(enemiesAware)).append("\n");
    sb.append("    activeEffects: ").append(toIndentedString(activeEffects)).append("\n");
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

