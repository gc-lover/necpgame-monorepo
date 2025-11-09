package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * TechnologyAccess
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TechnologyAccess {

  private @Nullable String technologyId;

  private @Nullable String name;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    WEAPONS("WEAPONS"),
    
    CYBERWARE("CYBERWARE"),
    
    VEHICLES("VEHICLES"),
    
    NETRUNNING("NETRUNNING"),
    
    MANUFACTURING("MANUFACTURING");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable CategoryEnum category;

  private @Nullable Boolean available;

  private @Nullable String requiredEra;

  @Valid
  private List<String> restrictedFactions = new ArrayList<>();

  public TechnologyAccess technologyId(@Nullable String technologyId) {
    this.technologyId = technologyId;
    return this;
  }

  /**
   * Get technologyId
   * @return technologyId
   */
  
  @Schema(name = "technology_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("technology_id")
  public @Nullable String getTechnologyId() {
    return technologyId;
  }

  public void setTechnologyId(@Nullable String technologyId) {
    this.technologyId = technologyId;
  }

  public TechnologyAccess name(@Nullable String name) {
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

  public TechnologyAccess category(@Nullable CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(@Nullable CategoryEnum category) {
    this.category = category;
  }

  public TechnologyAccess available(@Nullable Boolean available) {
    this.available = available;
    return this;
  }

  /**
   * Get available
   * @return available
   */
  
  @Schema(name = "available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("available")
  public @Nullable Boolean getAvailable() {
    return available;
  }

  public void setAvailable(@Nullable Boolean available) {
    this.available = available;
  }

  public TechnologyAccess requiredEra(@Nullable String requiredEra) {
    this.requiredEra = requiredEra;
    return this;
  }

  /**
   * Get requiredEra
   * @return requiredEra
   */
  
  @Schema(name = "required_era", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("required_era")
  public @Nullable String getRequiredEra() {
    return requiredEra;
  }

  public void setRequiredEra(@Nullable String requiredEra) {
    this.requiredEra = requiredEra;
  }

  public TechnologyAccess restrictedFactions(List<String> restrictedFactions) {
    this.restrictedFactions = restrictedFactions;
    return this;
  }

  public TechnologyAccess addRestrictedFactionsItem(String restrictedFactionsItem) {
    if (this.restrictedFactions == null) {
      this.restrictedFactions = new ArrayList<>();
    }
    this.restrictedFactions.add(restrictedFactionsItem);
    return this;
  }

  /**
   * Get restrictedFactions
   * @return restrictedFactions
   */
  
  @Schema(name = "restricted_factions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("restricted_factions")
  public List<String> getRestrictedFactions() {
    return restrictedFactions;
  }

  public void setRestrictedFactions(List<String> restrictedFactions) {
    this.restrictedFactions = restrictedFactions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TechnologyAccess technologyAccess = (TechnologyAccess) o;
    return Objects.equals(this.technologyId, technologyAccess.technologyId) &&
        Objects.equals(this.name, technologyAccess.name) &&
        Objects.equals(this.category, technologyAccess.category) &&
        Objects.equals(this.available, technologyAccess.available) &&
        Objects.equals(this.requiredEra, technologyAccess.requiredEra) &&
        Objects.equals(this.restrictedFactions, technologyAccess.restrictedFactions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(technologyId, name, category, available, requiredEra, restrictedFactions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TechnologyAccess {\n");
    sb.append("    technologyId: ").append(toIndentedString(technologyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    available: ").append(toIndentedString(available)).append("\n");
    sb.append("    requiredEra: ").append(toIndentedString(requiredEra)).append("\n");
    sb.append("    restrictedFactions: ").append(toIndentedString(restrictedFactions)).append("\n");
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

