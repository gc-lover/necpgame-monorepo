package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EraSettingsFactionSliders
 */

@JsonTypeName("EraSettings_faction_sliders")

public class EraSettingsFactionSliders {

  private @Nullable Integer aggression;

  private @Nullable Integer expansion;

  private @Nullable Integer diplomacy;

  private @Nullable Integer espionage;

  public EraSettingsFactionSliders aggression(@Nullable Integer aggression) {
    this.aggression = aggression;
    return this;
  }

  /**
   * Get aggression
   * @return aggression
   */
  
  @Schema(name = "aggression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aggression")
  public @Nullable Integer getAggression() {
    return aggression;
  }

  public void setAggression(@Nullable Integer aggression) {
    this.aggression = aggression;
  }

  public EraSettingsFactionSliders expansion(@Nullable Integer expansion) {
    this.expansion = expansion;
    return this;
  }

  /**
   * Get expansion
   * @return expansion
   */
  
  @Schema(name = "expansion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expansion")
  public @Nullable Integer getExpansion() {
    return expansion;
  }

  public void setExpansion(@Nullable Integer expansion) {
    this.expansion = expansion;
  }

  public EraSettingsFactionSliders diplomacy(@Nullable Integer diplomacy) {
    this.diplomacy = diplomacy;
    return this;
  }

  /**
   * Get diplomacy
   * @return diplomacy
   */
  
  @Schema(name = "diplomacy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diplomacy")
  public @Nullable Integer getDiplomacy() {
    return diplomacy;
  }

  public void setDiplomacy(@Nullable Integer diplomacy) {
    this.diplomacy = diplomacy;
  }

  public EraSettingsFactionSliders espionage(@Nullable Integer espionage) {
    this.espionage = espionage;
    return this;
  }

  /**
   * Get espionage
   * @return espionage
   */
  
  @Schema(name = "espionage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("espionage")
  public @Nullable Integer getEspionage() {
    return espionage;
  }

  public void setEspionage(@Nullable Integer espionage) {
    this.espionage = espionage;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraSettingsFactionSliders eraSettingsFactionSliders = (EraSettingsFactionSliders) o;
    return Objects.equals(this.aggression, eraSettingsFactionSliders.aggression) &&
        Objects.equals(this.expansion, eraSettingsFactionSliders.expansion) &&
        Objects.equals(this.diplomacy, eraSettingsFactionSliders.diplomacy) &&
        Objects.equals(this.espionage, eraSettingsFactionSliders.espionage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aggression, expansion, diplomacy, espionage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraSettingsFactionSliders {\n");
    sb.append("    aggression: ").append(toIndentedString(aggression)).append("\n");
    sb.append("    expansion: ").append(toIndentedString(expansion)).append("\n");
    sb.append("    diplomacy: ").append(toIndentedString(diplomacy)).append("\n");
    sb.append("    espionage: ").append(toIndentedString(espionage)).append("\n");
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

