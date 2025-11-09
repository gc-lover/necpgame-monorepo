package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.LeagueTypeWipeRules;
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
 * LeagueType
 */


public class LeagueType {

  /**
   * Gets or Sets typeId
   */
  public enum TypeIdEnum {
    STANDARD("standard"),
    
    HARDCORE("hardcore"),
    
    EVENT("event");

    private final String value;

    TypeIdEnum(String value) {
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
    public static TypeIdEnum fromValue(String value) {
      for (TypeIdEnum b : TypeIdEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable TypeIdEnum typeId;

  private @Nullable String name;

  private @Nullable String description;

  @Valid
  private List<String> specialRules = new ArrayList<>();

  private @Nullable LeagueTypeWipeRules wipeRules;

  public LeagueType typeId(@Nullable TypeIdEnum typeId) {
    this.typeId = typeId;
    return this;
  }

  /**
   * Get typeId
   * @return typeId
   */
  
  @Schema(name = "type_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type_id")
  public @Nullable TypeIdEnum getTypeId() {
    return typeId;
  }

  public void setTypeId(@Nullable TypeIdEnum typeId) {
    this.typeId = typeId;
  }

  public LeagueType name(@Nullable String name) {
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

  public LeagueType description(@Nullable String description) {
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

  public LeagueType specialRules(List<String> specialRules) {
    this.specialRules = specialRules;
    return this;
  }

  public LeagueType addSpecialRulesItem(String specialRulesItem) {
    if (this.specialRules == null) {
      this.specialRules = new ArrayList<>();
    }
    this.specialRules.add(specialRulesItem);
    return this;
  }

  /**
   * Специальные правила типа лиги
   * @return specialRules
   */
  
  @Schema(name = "special_rules", example = "[\"Permadeath on character death\",\"No trading with other players\"]", description = "Специальные правила типа лиги", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("special_rules")
  public List<String> getSpecialRules() {
    return specialRules;
  }

  public void setSpecialRules(List<String> specialRules) {
    this.specialRules = specialRules;
  }

  public LeagueType wipeRules(@Nullable LeagueTypeWipeRules wipeRules) {
    this.wipeRules = wipeRules;
    return this;
  }

  /**
   * Get wipeRules
   * @return wipeRules
   */
  @Valid 
  @Schema(name = "wipe_rules", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wipe_rules")
  public @Nullable LeagueTypeWipeRules getWipeRules() {
    return wipeRules;
  }

  public void setWipeRules(@Nullable LeagueTypeWipeRules wipeRules) {
    this.wipeRules = wipeRules;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LeagueType leagueType = (LeagueType) o;
    return Objects.equals(this.typeId, leagueType.typeId) &&
        Objects.equals(this.name, leagueType.name) &&
        Objects.equals(this.description, leagueType.description) &&
        Objects.equals(this.specialRules, leagueType.specialRules) &&
        Objects.equals(this.wipeRules, leagueType.wipeRules);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeId, name, description, specialRules, wipeRules);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LeagueType {\n");
    sb.append("    typeId: ").append(toIndentedString(typeId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    specialRules: ").append(toIndentedString(specialRules)).append("\n");
    sb.append("    wipeRules: ").append(toIndentedString(wipeRules)).append("\n");
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

