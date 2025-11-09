package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceEventGenerationRequestGenerationParams
 */

@JsonTypeName("RomanceEventGenerationRequest_generation_params")

public class RomanceEventGenerationRequestGenerationParams {

  private Integer maxEvents = 5;

  private BigDecimal minScore = new BigDecimal("0.5");

  private BigDecimal diversityFactor = new BigDecimal("0.3");

  public RomanceEventGenerationRequestGenerationParams maxEvents(Integer maxEvents) {
    this.maxEvents = maxEvents;
    return this;
  }

  /**
   * Максимум событий в результате
   * minimum: 1
   * maximum: 20
   * @return maxEvents
   */
  @Min(value = 1) @Max(value = 20) 
  @Schema(name = "max_events", description = "Максимум событий в результате", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_events")
  public Integer getMaxEvents() {
    return maxEvents;
  }

  public void setMaxEvents(Integer maxEvents) {
    this.maxEvents = maxEvents;
  }

  public RomanceEventGenerationRequestGenerationParams minScore(BigDecimal minScore) {
    this.minScore = minScore;
    return this;
  }

  /**
   * Минимальный score (0-1)
   * minimum: 0
   * maximum: 1
   * @return minScore
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "min_score", description = "Минимальный score (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("min_score")
  public BigDecimal getMinScore() {
    return minScore;
  }

  public void setMinScore(BigDecimal minScore) {
    this.minScore = minScore;
  }

  public RomanceEventGenerationRequestGenerationParams diversityFactor(BigDecimal diversityFactor) {
    this.diversityFactor = diversityFactor;
    return this;
  }

  /**
   * Фактор разнообразия (0-1)
   * minimum: 0
   * maximum: 1
   * @return diversityFactor
   */
  @Valid @DecimalMin(value = "0") @DecimalMax(value = "1") 
  @Schema(name = "diversity_factor", description = "Фактор разнообразия (0-1)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("diversity_factor")
  public BigDecimal getDiversityFactor() {
    return diversityFactor;
  }

  public void setDiversityFactor(BigDecimal diversityFactor) {
    this.diversityFactor = diversityFactor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationRequestGenerationParams romanceEventGenerationRequestGenerationParams = (RomanceEventGenerationRequestGenerationParams) o;
    return Objects.equals(this.maxEvents, romanceEventGenerationRequestGenerationParams.maxEvents) &&
        Objects.equals(this.minScore, romanceEventGenerationRequestGenerationParams.minScore) &&
        Objects.equals(this.diversityFactor, romanceEventGenerationRequestGenerationParams.diversityFactor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maxEvents, minScore, diversityFactor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationRequestGenerationParams {\n");
    sb.append("    maxEvents: ").append(toIndentedString(maxEvents)).append("\n");
    sb.append("    minScore: ").append(toIndentedString(minScore)).append("\n");
    sb.append("    diversityFactor: ").append(toIndentedString(diversityFactor)).append("\n");
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

