package com.necpgame.worldservice.model;

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
 * EraSettings
 */


public class EraSettings {

  private @Nullable String era;

  private @Nullable Object dcScaling;

  private @Nullable Object factionSliders;

  private @Nullable Object economy;

  private @Nullable Object technologies;

  public EraSettings era(@Nullable String era) {
    this.era = era;
    return this;
  }

  /**
   * Get era
   * @return era
   */
  
  @Schema(name = "era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("era")
  public @Nullable String getEra() {
    return era;
  }

  public void setEra(@Nullable String era) {
    this.era = era;
  }

  public EraSettings dcScaling(@Nullable Object dcScaling) {
    this.dcScaling = dcScaling;
    return this;
  }

  /**
   * Get dcScaling
   * @return dcScaling
   */
  
  @Schema(name = "dc_scaling", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc_scaling")
  public @Nullable Object getDcScaling() {
    return dcScaling;
  }

  public void setDcScaling(@Nullable Object dcScaling) {
    this.dcScaling = dcScaling;
  }

  public EraSettings factionSliders(@Nullable Object factionSliders) {
    this.factionSliders = factionSliders;
    return this;
  }

  /**
   * Get factionSliders
   * @return factionSliders
   */
  
  @Schema(name = "faction_sliders", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_sliders")
  public @Nullable Object getFactionSliders() {
    return factionSliders;
  }

  public void setFactionSliders(@Nullable Object factionSliders) {
    this.factionSliders = factionSliders;
  }

  public EraSettings economy(@Nullable Object economy) {
    this.economy = economy;
    return this;
  }

  /**
   * Get economy
   * @return economy
   */
  
  @Schema(name = "economy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economy")
  public @Nullable Object getEconomy() {
    return economy;
  }

  public void setEconomy(@Nullable Object economy) {
    this.economy = economy;
  }

  public EraSettings technologies(@Nullable Object technologies) {
    this.technologies = technologies;
    return this;
  }

  /**
   * Get technologies
   * @return technologies
   */
  
  @Schema(name = "technologies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technologies")
  public @Nullable Object getTechnologies() {
    return technologies;
  }

  public void setTechnologies(@Nullable Object technologies) {
    this.technologies = technologies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraSettings eraSettings = (EraSettings) o;
    return Objects.equals(this.era, eraSettings.era) &&
        Objects.equals(this.dcScaling, eraSettings.dcScaling) &&
        Objects.equals(this.factionSliders, eraSettings.factionSliders) &&
        Objects.equals(this.economy, eraSettings.economy) &&
        Objects.equals(this.technologies, eraSettings.technologies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, dcScaling, factionSliders, economy, technologies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraSettings {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    dcScaling: ").append(toIndentedString(dcScaling)).append("\n");
    sb.append("    factionSliders: ").append(toIndentedString(factionSliders)).append("\n");
    sb.append("    economy: ").append(toIndentedString(economy)).append("\n");
    sb.append("    technologies: ").append(toIndentedString(technologies)).append("\n");
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

