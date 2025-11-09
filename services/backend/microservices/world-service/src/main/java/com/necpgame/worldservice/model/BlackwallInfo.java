package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * BlackwallInfo
 */


public class BlackwallInfo {

  private @Nullable String name;

  private @Nullable String description;

  private @Nullable Integer createdYear;

  private @Nullable String purpose;

  @Valid
  private List<String> structure = new ArrayList<>();

  @Valid
  private List<String> knownBreaches = new ArrayList<>();

  @Valid
  private List<String> guardians = new ArrayList<>();

  public BlackwallInfo name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public BlackwallInfo description(@Nullable String description) {
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

  public BlackwallInfo createdYear(@Nullable Integer createdYear) {
    this.createdYear = createdYear;
    return this;
  }

  /**
   * Get createdYear
   * @return createdYear
   */
  
  @Schema(name = "created_year", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_year")
  public @Nullable Integer getCreatedYear() {
    return createdYear;
  }

  public void setCreatedYear(@Nullable Integer createdYear) {
    this.createdYear = createdYear;
  }

  public BlackwallInfo purpose(@Nullable String purpose) {
    this.purpose = purpose;
    return this;
  }

  /**
   * Get purpose
   * @return purpose
   */
  
  @Schema(name = "purpose", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("purpose")
  public @Nullable String getPurpose() {
    return purpose;
  }

  public void setPurpose(@Nullable String purpose) {
    this.purpose = purpose;
  }

  public BlackwallInfo structure(List<String> structure) {
    this.structure = structure;
    return this;
  }

  public BlackwallInfo addStructureItem(String structureItem) {
    if (this.structure == null) {
      this.structure = new ArrayList<>();
    }
    this.structure.add(structureItem);
    return this;
  }

  /**
   * Get structure
   * @return structure
   */
  
  @Schema(name = "structure", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("structure")
  public List<String> getStructure() {
    return structure;
  }

  public void setStructure(List<String> structure) {
    this.structure = structure;
  }

  public BlackwallInfo knownBreaches(List<String> knownBreaches) {
    this.knownBreaches = knownBreaches;
    return this;
  }

  public BlackwallInfo addKnownBreachesItem(String knownBreachesItem) {
    if (this.knownBreaches == null) {
      this.knownBreaches = new ArrayList<>();
    }
    this.knownBreaches.add(knownBreachesItem);
    return this;
  }

  /**
   * Get knownBreaches
   * @return knownBreaches
   */
  
  @Schema(name = "known_breaches", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("known_breaches")
  public List<String> getKnownBreaches() {
    return knownBreaches;
  }

  public void setKnownBreaches(List<String> knownBreaches) {
    this.knownBreaches = knownBreaches;
  }

  public BlackwallInfo guardians(List<String> guardians) {
    this.guardians = guardians;
    return this;
  }

  public BlackwallInfo addGuardiansItem(String guardiansItem) {
    if (this.guardians == null) {
      this.guardians = new ArrayList<>();
    }
    this.guardians.add(guardiansItem);
    return this;
  }

  /**
   * Get guardians
   * @return guardians
   */
  
  @Schema(name = "guardians", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guardians")
  public List<String> getGuardians() {
    return guardians;
  }

  public void setGuardians(List<String> guardians) {
    this.guardians = guardians;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlackwallInfo blackwallInfo = (BlackwallInfo) o;
    return Objects.equals(this.name, blackwallInfo.name) &&
        Objects.equals(this.description, blackwallInfo.description) &&
        Objects.equals(this.createdYear, blackwallInfo.createdYear) &&
        Objects.equals(this.purpose, blackwallInfo.purpose) &&
        Objects.equals(this.structure, blackwallInfo.structure) &&
        Objects.equals(this.knownBreaches, blackwallInfo.knownBreaches) &&
        Objects.equals(this.guardians, blackwallInfo.guardians);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, createdYear, purpose, structure, knownBreaches, guardians);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlackwallInfo {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    createdYear: ").append(toIndentedString(createdYear)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    structure: ").append(toIndentedString(structure)).append("\n");
    sb.append("    knownBreaches: ").append(toIndentedString(knownBreaches)).append("\n");
    sb.append("    guardians: ").append(toIndentedString(guardians)).append("\n");
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

