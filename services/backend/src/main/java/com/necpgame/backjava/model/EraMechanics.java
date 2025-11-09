package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.DCScaling;
import com.necpgame.backjava.model.EraMechanicsEconomicState;
import com.necpgame.backjava.model.EraMechanicsSocialMechanics;
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
 * EraMechanics
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class EraMechanics {

  private @Nullable String era;

  private @Nullable DCScaling dcScaling;

  private @Nullable EraMechanicsEconomicState economicState;

  @Valid
  private List<String> technologyRestrictions = new ArrayList<>();

  private @Nullable EraMechanicsSocialMechanics socialMechanics;

  public EraMechanics era(@Nullable String era) {
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

  public EraMechanics dcScaling(@Nullable DCScaling dcScaling) {
    this.dcScaling = dcScaling;
    return this;
  }

  /**
   * Get dcScaling
   * @return dcScaling
   */
  @Valid 
  @Schema(name = "dc_scaling", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc_scaling")
  public @Nullable DCScaling getDcScaling() {
    return dcScaling;
  }

  public void setDcScaling(@Nullable DCScaling dcScaling) {
    this.dcScaling = dcScaling;
  }

  public EraMechanics economicState(@Nullable EraMechanicsEconomicState economicState) {
    this.economicState = economicState;
    return this;
  }

  /**
   * Get economicState
   * @return economicState
   */
  @Valid 
  @Schema(name = "economic_state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("economic_state")
  public @Nullable EraMechanicsEconomicState getEconomicState() {
    return economicState;
  }

  public void setEconomicState(@Nullable EraMechanicsEconomicState economicState) {
    this.economicState = economicState;
  }

  public EraMechanics technologyRestrictions(List<String> technologyRestrictions) {
    this.technologyRestrictions = technologyRestrictions;
    return this;
  }

  public EraMechanics addTechnologyRestrictionsItem(String technologyRestrictionsItem) {
    if (this.technologyRestrictions == null) {
      this.technologyRestrictions = new ArrayList<>();
    }
    this.technologyRestrictions.add(technologyRestrictionsItem);
    return this;
  }

  /**
   * Недоступные технологии
   * @return technologyRestrictions
   */
  
  @Schema(name = "technology_restrictions", description = "Недоступные технологии", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technology_restrictions")
  public List<String> getTechnologyRestrictions() {
    return technologyRestrictions;
  }

  public void setTechnologyRestrictions(List<String> technologyRestrictions) {
    this.technologyRestrictions = technologyRestrictions;
  }

  public EraMechanics socialMechanics(@Nullable EraMechanicsSocialMechanics socialMechanics) {
    this.socialMechanics = socialMechanics;
    return this;
  }

  /**
   * Get socialMechanics
   * @return socialMechanics
   */
  @Valid 
  @Schema(name = "social_mechanics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("social_mechanics")
  public @Nullable EraMechanicsSocialMechanics getSocialMechanics() {
    return socialMechanics;
  }

  public void setSocialMechanics(@Nullable EraMechanicsSocialMechanics socialMechanics) {
    this.socialMechanics = socialMechanics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EraMechanics eraMechanics = (EraMechanics) o;
    return Objects.equals(this.era, eraMechanics.era) &&
        Objects.equals(this.dcScaling, eraMechanics.dcScaling) &&
        Objects.equals(this.economicState, eraMechanics.economicState) &&
        Objects.equals(this.technologyRestrictions, eraMechanics.technologyRestrictions) &&
        Objects.equals(this.socialMechanics, eraMechanics.socialMechanics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(era, dcScaling, economicState, technologyRestrictions, socialMechanics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EraMechanics {\n");
    sb.append("    era: ").append(toIndentedString(era)).append("\n");
    sb.append("    dcScaling: ").append(toIndentedString(dcScaling)).append("\n");
    sb.append("    economicState: ").append(toIndentedString(economicState)).append("\n");
    sb.append("    technologyRestrictions: ").append(toIndentedString(technologyRestrictions)).append("\n");
    sb.append("    socialMechanics: ").append(toIndentedString(socialMechanics)).append("\n");
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

