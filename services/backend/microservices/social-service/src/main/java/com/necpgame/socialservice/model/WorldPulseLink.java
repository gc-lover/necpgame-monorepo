package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.OffsetDateTime;
import java.util.Arrays;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.util.NoSuchElementException;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WorldPulseLink
 */


public class WorldPulseLink {

  private String pulseLevel;

  private Float crisisRisk;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private JsonNullable<OffsetDateTime> lastCrisisTrigger = JsonNullable.<OffsetDateTime>undefined();

  private @Nullable String projectedAfterCampaign;

  public WorldPulseLink() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WorldPulseLink(String pulseLevel, Float crisisRisk) {
    this.pulseLevel = pulseLevel;
    this.crisisRisk = crisisRisk;
  }

  public WorldPulseLink pulseLevel(String pulseLevel) {
    this.pulseLevel = pulseLevel;
    return this;
  }

  /**
   * Get pulseLevel
   * @return pulseLevel
   */
  @NotNull 
  @Schema(name = "pulseLevel", example = "INFLECTION", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("pulseLevel")
  public String getPulseLevel() {
    return pulseLevel;
  }

  public void setPulseLevel(String pulseLevel) {
    this.pulseLevel = pulseLevel;
  }

  public WorldPulseLink crisisRisk(Float crisisRisk) {
    this.crisisRisk = crisisRisk;
    return this;
  }

  /**
   * Get crisisRisk
   * minimum: 0
   * maximum: 1
   * @return crisisRisk
   */
  @NotNull @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "crisisRisk", example = "0.28", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("crisisRisk")
  public Float getCrisisRisk() {
    return crisisRisk;
  }

  public void setCrisisRisk(Float crisisRisk) {
    this.crisisRisk = crisisRisk;
  }

  public WorldPulseLink lastCrisisTrigger(OffsetDateTime lastCrisisTrigger) {
    this.lastCrisisTrigger = JsonNullable.of(lastCrisisTrigger);
    return this;
  }

  /**
   * Get lastCrisisTrigger
   * @return lastCrisisTrigger
   */
  @Valid 
  @Schema(name = "lastCrisisTrigger", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastCrisisTrigger")
  public JsonNullable<OffsetDateTime> getLastCrisisTrigger() {
    return lastCrisisTrigger;
  }

  public void setLastCrisisTrigger(JsonNullable<OffsetDateTime> lastCrisisTrigger) {
    this.lastCrisisTrigger = lastCrisisTrigger;
  }

  public WorldPulseLink projectedAfterCampaign(@Nullable String projectedAfterCampaign) {
    this.projectedAfterCampaign = projectedAfterCampaign;
    return this;
  }

  /**
   * Get projectedAfterCampaign
   * @return projectedAfterCampaign
   */
  
  @Schema(name = "projectedAfterCampaign", example = "CALM", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("projectedAfterCampaign")
  public @Nullable String getProjectedAfterCampaign() {
    return projectedAfterCampaign;
  }

  public void setProjectedAfterCampaign(@Nullable String projectedAfterCampaign) {
    this.projectedAfterCampaign = projectedAfterCampaign;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WorldPulseLink worldPulseLink = (WorldPulseLink) o;
    return Objects.equals(this.pulseLevel, worldPulseLink.pulseLevel) &&
        Objects.equals(this.crisisRisk, worldPulseLink.crisisRisk) &&
        equalsNullable(this.lastCrisisTrigger, worldPulseLink.lastCrisisTrigger) &&
        Objects.equals(this.projectedAfterCampaign, worldPulseLink.projectedAfterCampaign);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(pulseLevel, crisisRisk, hashCodeNullable(lastCrisisTrigger), projectedAfterCampaign);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WorldPulseLink {\n");
    sb.append("    pulseLevel: ").append(toIndentedString(pulseLevel)).append("\n");
    sb.append("    crisisRisk: ").append(toIndentedString(crisisRisk)).append("\n");
    sb.append("    lastCrisisTrigger: ").append(toIndentedString(lastCrisisTrigger)).append("\n");
    sb.append("    projectedAfterCampaign: ").append(toIndentedString(projectedAfterCampaign)).append("\n");
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

