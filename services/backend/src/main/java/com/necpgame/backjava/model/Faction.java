package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Faction
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class Faction {

  private @Nullable String factionId;

  private @Nullable String name;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CORPORATION("CORPORATION"),
    
    GANG("GANG"),
    
    ORGANIZATION("ORGANIZATION"),
    
    GOVERNMENT("GOVERNMENT");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeEnum type;

  private @Nullable String region;

  private @Nullable Integer powerLevel;

  private @Nullable String descriptionShort;

  public Faction factionId(@Nullable String factionId) {
    this.factionId = factionId;
    return this;
  }

  /**
   * Get factionId
   * @return factionId
   */
  
  @Schema(name = "faction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("faction_id")
  public @Nullable String getFactionId() {
    return factionId;
  }

  public void setFactionId(@Nullable String factionId) {
    this.factionId = factionId;
  }

  public Faction name(@Nullable String name) {
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

  public Faction type(@Nullable TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable TypeEnum getType() {
    return type;
  }

  public void setType(@Nullable TypeEnum type) {
    this.type = type;
  }

  public Faction region(@Nullable String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  
  @Schema(name = "region", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("region")
  public @Nullable String getRegion() {
    return region;
  }

  public void setRegion(@Nullable String region) {
    this.region = region;
  }

  public Faction powerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
    return this;
  }

  /**
   * Get powerLevel
   * minimum: 1
   * maximum: 10
   * @return powerLevel
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "power_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("power_level")
  public @Nullable Integer getPowerLevel() {
    return powerLevel;
  }

  public void setPowerLevel(@Nullable Integer powerLevel) {
    this.powerLevel = powerLevel;
  }

  public Faction descriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
    return this;
  }

  /**
   * Get descriptionShort
   * @return descriptionShort
   */
  
  @Schema(name = "description_short", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description_short")
  public @Nullable String getDescriptionShort() {
    return descriptionShort;
  }

  public void setDescriptionShort(@Nullable String descriptionShort) {
    this.descriptionShort = descriptionShort;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Faction faction = (Faction) o;
    return Objects.equals(this.factionId, faction.factionId) &&
        Objects.equals(this.name, faction.name) &&
        Objects.equals(this.type, faction.type) &&
        Objects.equals(this.region, faction.region) &&
        Objects.equals(this.powerLevel, faction.powerLevel) &&
        Objects.equals(this.descriptionShort, faction.descriptionShort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factionId, name, type, region, powerLevel, descriptionShort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Faction {\n");
    sb.append("    factionId: ").append(toIndentedString(factionId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    powerLevel: ").append(toIndentedString(powerLevel)).append("\n");
    sb.append("    descriptionShort: ").append(toIndentedString(descriptionShort)).append("\n");
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

