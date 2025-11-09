package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SynergyBonuses;
import com.necpgame.gameplayservice.model.SynergyRequirements;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Synergy
 */


public class Synergy {

  private @Nullable String synergyId;

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable SynergyRequirements requirements;

  private @Nullable SynergyBonuses bonuses;

  private @Nullable Boolean active;

  public Synergy synergyId(@Nullable String synergyId) {
    this.synergyId = synergyId;
    return this;
  }

  /**
   * Get synergyId
   * @return synergyId
   */
  
  @Schema(name = "synergy_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("synergy_id")
  public @Nullable String getSynergyId() {
    return synergyId;
  }

  public void setSynergyId(@Nullable String synergyId) {
    this.synergyId = synergyId;
  }

  public Synergy name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", example = "Tech Savvy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Synergy description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public Synergy requirements(@Nullable SynergyRequirements requirements) {
    this.requirements = requirements;
    return this;
  }

  /**
   * Get requirements
   * @return requirements
   */
  @Valid 
  @Schema(name = "requirements", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requirements")
  public @Nullable SynergyRequirements getRequirements() {
    return requirements;
  }

  public void setRequirements(@Nullable SynergyRequirements requirements) {
    this.requirements = requirements;
  }

  public Synergy bonuses(@Nullable SynergyBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable SynergyBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable SynergyBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public Synergy active(@Nullable Boolean active) {
    this.active = active;
    return this;
  }

  /**
   * Get active
   * @return active
   */
  
  @Schema(name = "active", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active")
  public @Nullable Boolean getActive() {
    return active;
  }

  public void setActive(@Nullable Boolean active) {
    this.active = active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Synergy synergy = (Synergy) o;
    return Objects.equals(this.synergyId, synergy.synergyId) &&
        Objects.equals(this.name, synergy.name) &&
        Objects.equals(this.description, synergy.description) &&
        Objects.equals(this.requirements, synergy.requirements) &&
        Objects.equals(this.bonuses, synergy.bonuses) &&
        Objects.equals(this.active, synergy.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(synergyId, name, description, requirements, bonuses, active);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Synergy {\n");
    sb.append("    synergyId: ").append(toIndentedString(synergyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    requirements: ").append(toIndentedString(requirements)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    active: ").append(toIndentedString(active)).append("\n");
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

