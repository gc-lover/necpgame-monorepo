package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.Tier;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TierDefinition
 */


public class TierDefinition {

  private Tier name;

  private Integer minRating;

  private @Nullable Integer divisions;

  private @Nullable Integer promotionThreshold;

  private @Nullable Integer demotionThreshold;

  public TierDefinition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TierDefinition(Tier name, Integer minRating) {
    this.name = name;
    this.minRating = minRating;
  }

  public TierDefinition name(Tier name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull @Valid 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public Tier getName() {
    return name;
  }

  public void setName(Tier name) {
    this.name = name;
  }

  public TierDefinition minRating(Integer minRating) {
    this.minRating = minRating;
    return this;
  }

  /**
   * Get minRating
   * @return minRating
   */
  @NotNull 
  @Schema(name = "minRating", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("minRating")
  public Integer getMinRating() {
    return minRating;
  }

  public void setMinRating(Integer minRating) {
    this.minRating = minRating;
  }

  public TierDefinition divisions(@Nullable Integer divisions) {
    this.divisions = divisions;
    return this;
  }

  /**
   * Get divisions
   * minimum: 1
   * maximum: 5
   * @return divisions
   */
  @Min(value = 1) @Max(value = 5) 
  @Schema(name = "divisions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("divisions")
  public @Nullable Integer getDivisions() {
    return divisions;
  }

  public void setDivisions(@Nullable Integer divisions) {
    this.divisions = divisions;
  }

  public TierDefinition promotionThreshold(@Nullable Integer promotionThreshold) {
    this.promotionThreshold = promotionThreshold;
    return this;
  }

  /**
   * Get promotionThreshold
   * @return promotionThreshold
   */
  
  @Schema(name = "promotionThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("promotionThreshold")
  public @Nullable Integer getPromotionThreshold() {
    return promotionThreshold;
  }

  public void setPromotionThreshold(@Nullable Integer promotionThreshold) {
    this.promotionThreshold = promotionThreshold;
  }

  public TierDefinition demotionThreshold(@Nullable Integer demotionThreshold) {
    this.demotionThreshold = demotionThreshold;
    return this;
  }

  /**
   * Get demotionThreshold
   * @return demotionThreshold
   */
  
  @Schema(name = "demotionThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("demotionThreshold")
  public @Nullable Integer getDemotionThreshold() {
    return demotionThreshold;
  }

  public void setDemotionThreshold(@Nullable Integer demotionThreshold) {
    this.demotionThreshold = demotionThreshold;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TierDefinition tierDefinition = (TierDefinition) o;
    return Objects.equals(this.name, tierDefinition.name) &&
        Objects.equals(this.minRating, tierDefinition.minRating) &&
        Objects.equals(this.divisions, tierDefinition.divisions) &&
        Objects.equals(this.promotionThreshold, tierDefinition.promotionThreshold) &&
        Objects.equals(this.demotionThreshold, tierDefinition.demotionThreshold);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, minRating, divisions, promotionThreshold, demotionThreshold);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TierDefinition {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    minRating: ").append(toIndentedString(minRating)).append("\n");
    sb.append("    divisions: ").append(toIndentedString(divisions)).append("\n");
    sb.append("    promotionThreshold: ").append(toIndentedString(promotionThreshold)).append("\n");
    sb.append("    demotionThreshold: ").append(toIndentedString(demotionThreshold)).append("\n");
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

