package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * CosmeticItemExclusivity
 */

@JsonTypeName("CosmeticItem_exclusivity")

public class CosmeticItemExclusivity {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    NONE("none"),
    
    BATTLE_PASS("battle_pass"),
    
    EVENT_ONLY("event_only"),
    
    LIMITED_TIME("limited_time"),
    
    FOUNDER("founder");

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

  private @Nullable String season;

  @Valid
  private List<String> regions = new ArrayList<>();

  public CosmeticItemExclusivity type(@Nullable TypeEnum type) {
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

  public CosmeticItemExclusivity season(@Nullable String season) {
    this.season = season;
    return this;
  }

  /**
   * Get season
   * @return season
   */
  
  @Schema(name = "season", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("season")
  public @Nullable String getSeason() {
    return season;
  }

  public void setSeason(@Nullable String season) {
    this.season = season;
  }

  public CosmeticItemExclusivity regions(List<String> regions) {
    this.regions = regions;
    return this;
  }

  public CosmeticItemExclusivity addRegionsItem(String regionsItem) {
    if (this.regions == null) {
      this.regions = new ArrayList<>();
    }
    this.regions.add(regionsItem);
    return this;
  }

  /**
   * Get regions
   * @return regions
   */
  
  @Schema(name = "regions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("regions")
  public List<String> getRegions() {
    return regions;
  }

  public void setRegions(List<String> regions) {
    this.regions = regions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CosmeticItemExclusivity cosmeticItemExclusivity = (CosmeticItemExclusivity) o;
    return Objects.equals(this.type, cosmeticItemExclusivity.type) &&
        Objects.equals(this.season, cosmeticItemExclusivity.season) &&
        Objects.equals(this.regions, cosmeticItemExclusivity.regions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, season, regions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CosmeticItemExclusivity {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    season: ").append(toIndentedString(season)).append("\n");
    sb.append("    regions: ").append(toIndentedString(regions)).append("\n");
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

