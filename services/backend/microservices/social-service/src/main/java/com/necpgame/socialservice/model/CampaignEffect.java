package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ResonanceDimension;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CampaignEffect
 */


public class CampaignEffect {

  private ResonanceDimension dimension;

  private Float delta;

  private @Nullable Integer durationHours;

  private @Nullable String worldPulseImpact;

  public CampaignEffect() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CampaignEffect(ResonanceDimension dimension, Float delta) {
    this.dimension = dimension;
    this.delta = delta;
  }

  public CampaignEffect dimension(ResonanceDimension dimension) {
    this.dimension = dimension;
    return this;
  }

  /**
   * Get dimension
   * @return dimension
   */
  @NotNull @Valid 
  @Schema(name = "dimension", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("dimension")
  public ResonanceDimension getDimension() {
    return dimension;
  }

  public void setDimension(ResonanceDimension dimension) {
    this.dimension = dimension;
  }

  public CampaignEffect delta(Float delta) {
    this.delta = delta;
    return this;
  }

  /**
   * Get delta
   * @return delta
   */
  @NotNull 
  @Schema(name = "delta", example = "6.5", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("delta")
  public Float getDelta() {
    return delta;
  }

  public void setDelta(Float delta) {
    this.delta = delta;
  }

  public CampaignEffect durationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
    return this;
  }

  /**
   * Get durationHours
   * minimum: 1
   * maximum: 168
   * @return durationHours
   */
  @Min(value = 1) @Max(value = 168) 
  @Schema(name = "durationHours", example = "48", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationHours")
  public @Nullable Integer getDurationHours() {
    return durationHours;
  }

  public void setDurationHours(@Nullable Integer durationHours) {
    this.durationHours = durationHours;
  }

  public CampaignEffect worldPulseImpact(@Nullable String worldPulseImpact) {
    this.worldPulseImpact = worldPulseImpact;
    return this;
  }

  /**
   * Get worldPulseImpact
   * @return worldPulseImpact
   */
  
  @Schema(name = "worldPulseImpact", example = "Crisis risk -0.05 for 24h", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("worldPulseImpact")
  public @Nullable String getWorldPulseImpact() {
    return worldPulseImpact;
  }

  public void setWorldPulseImpact(@Nullable String worldPulseImpact) {
    this.worldPulseImpact = worldPulseImpact;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CampaignEffect campaignEffect = (CampaignEffect) o;
    return Objects.equals(this.dimension, campaignEffect.dimension) &&
        Objects.equals(this.delta, campaignEffect.delta) &&
        Objects.equals(this.durationHours, campaignEffect.durationHours) &&
        Objects.equals(this.worldPulseImpact, campaignEffect.worldPulseImpact);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dimension, delta, durationHours, worldPulseImpact);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CampaignEffect {\n");
    sb.append("    dimension: ").append(toIndentedString(dimension)).append("\n");
    sb.append("    delta: ").append(toIndentedString(delta)).append("\n");
    sb.append("    durationHours: ").append(toIndentedString(durationHours)).append("\n");
    sb.append("    worldPulseImpact: ").append(toIndentedString(worldPulseImpact)).append("\n");
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

