package com.necpgame.worldservice.model;

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
 * RegisterWorldImpact200Response
 */

@JsonTypeName("registerWorldImpact_200_response")

public class RegisterWorldImpact200Response {

  private @Nullable String impactId;

  /**
   * Gets or Sets aggregatedLevel
   */
  public enum AggregatedLevelEnum {
    INDIVIDUAL("individual"),
    
    GROUP("group"),
    
    FACTION("faction"),
    
    REGIONAL("regional"),
    
    GLOBAL("global");

    private final String value;

    AggregatedLevelEnum(String value) {
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
    public static AggregatedLevelEnum fromValue(String value) {
      for (AggregatedLevelEnum b : AggregatedLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AggregatedLevelEnum aggregatedLevel;

  @Valid
  private List<Object> consequences = new ArrayList<>();

  public RegisterWorldImpact200Response impactId(@Nullable String impactId) {
    this.impactId = impactId;
    return this;
  }

  /**
   * Get impactId
   * @return impactId
   */
  
  @Schema(name = "impact_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("impact_id")
  public @Nullable String getImpactId() {
    return impactId;
  }

  public void setImpactId(@Nullable String impactId) {
    this.impactId = impactId;
  }

  public RegisterWorldImpact200Response aggregatedLevel(@Nullable AggregatedLevelEnum aggregatedLevel) {
    this.aggregatedLevel = aggregatedLevel;
    return this;
  }

  /**
   * Get aggregatedLevel
   * @return aggregatedLevel
   */
  
  @Schema(name = "aggregated_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aggregated_level")
  public @Nullable AggregatedLevelEnum getAggregatedLevel() {
    return aggregatedLevel;
  }

  public void setAggregatedLevel(@Nullable AggregatedLevelEnum aggregatedLevel) {
    this.aggregatedLevel = aggregatedLevel;
  }

  public RegisterWorldImpact200Response consequences(List<Object> consequences) {
    this.consequences = consequences;
    return this;
  }

  public RegisterWorldImpact200Response addConsequencesItem(Object consequencesItem) {
    if (this.consequences == null) {
      this.consequences = new ArrayList<>();
    }
    this.consequences.add(consequencesItem);
    return this;
  }

  /**
   * Get consequences
   * @return consequences
   */
  
  @Schema(name = "consequences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("consequences")
  public List<Object> getConsequences() {
    return consequences;
  }

  public void setConsequences(List<Object> consequences) {
    this.consequences = consequences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegisterWorldImpact200Response registerWorldImpact200Response = (RegisterWorldImpact200Response) o;
    return Objects.equals(this.impactId, registerWorldImpact200Response.impactId) &&
        Objects.equals(this.aggregatedLevel, registerWorldImpact200Response.aggregatedLevel) &&
        Objects.equals(this.consequences, registerWorldImpact200Response.consequences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(impactId, aggregatedLevel, consequences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RegisterWorldImpact200Response {\n");
    sb.append("    impactId: ").append(toIndentedString(impactId)).append("\n");
    sb.append("    aggregatedLevel: ").append(toIndentedString(aggregatedLevel)).append("\n");
    sb.append("    consequences: ").append(toIndentedString(consequences)).append("\n");
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

