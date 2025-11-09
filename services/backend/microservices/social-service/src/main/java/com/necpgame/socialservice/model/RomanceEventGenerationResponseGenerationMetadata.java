package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * RomanceEventGenerationResponseGenerationMetadata
 */

@JsonTypeName("RomanceEventGenerationResponse_generation_metadata")

public class RomanceEventGenerationResponseGenerationMetadata {

  private @Nullable Integer totalCandidates;

  private @Nullable Integer filteredOut;

  private @Nullable Integer generationTimeMs;

  private @Nullable String algorithmVersion;

  public RomanceEventGenerationResponseGenerationMetadata totalCandidates(@Nullable Integer totalCandidates) {
    this.totalCandidates = totalCandidates;
    return this;
  }

  /**
   * Всего кандидатов после фильтрации
   * @return totalCandidates
   */
  
  @Schema(name = "total_candidates", description = "Всего кандидатов после фильтрации", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_candidates")
  public @Nullable Integer getTotalCandidates() {
    return totalCandidates;
  }

  public void setTotalCandidates(@Nullable Integer totalCandidates) {
    this.totalCandidates = totalCandidates;
  }

  public RomanceEventGenerationResponseGenerationMetadata filteredOut(@Nullable Integer filteredOut) {
    this.filteredOut = filteredOut;
    return this;
  }

  /**
   * Отфильтровано событий
   * @return filteredOut
   */
  
  @Schema(name = "filtered_out", description = "Отфильтровано событий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filtered_out")
  public @Nullable Integer getFilteredOut() {
    return filteredOut;
  }

  public void setFilteredOut(@Nullable Integer filteredOut) {
    this.filteredOut = filteredOut;
  }

  public RomanceEventGenerationResponseGenerationMetadata generationTimeMs(@Nullable Integer generationTimeMs) {
    this.generationTimeMs = generationTimeMs;
    return this;
  }

  /**
   * Get generationTimeMs
   * @return generationTimeMs
   */
  
  @Schema(name = "generation_time_ms", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("generation_time_ms")
  public @Nullable Integer getGenerationTimeMs() {
    return generationTimeMs;
  }

  public void setGenerationTimeMs(@Nullable Integer generationTimeMs) {
    this.generationTimeMs = generationTimeMs;
  }

  public RomanceEventGenerationResponseGenerationMetadata algorithmVersion(@Nullable String algorithmVersion) {
    this.algorithmVersion = algorithmVersion;
    return this;
  }

  /**
   * Get algorithmVersion
   * @return algorithmVersion
   */
  
  @Schema(name = "algorithm_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("algorithm_version")
  public @Nullable String getAlgorithmVersion() {
    return algorithmVersion;
  }

  public void setAlgorithmVersion(@Nullable String algorithmVersion) {
    this.algorithmVersion = algorithmVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RomanceEventGenerationResponseGenerationMetadata romanceEventGenerationResponseGenerationMetadata = (RomanceEventGenerationResponseGenerationMetadata) o;
    return Objects.equals(this.totalCandidates, romanceEventGenerationResponseGenerationMetadata.totalCandidates) &&
        Objects.equals(this.filteredOut, romanceEventGenerationResponseGenerationMetadata.filteredOut) &&
        Objects.equals(this.generationTimeMs, romanceEventGenerationResponseGenerationMetadata.generationTimeMs) &&
        Objects.equals(this.algorithmVersion, romanceEventGenerationResponseGenerationMetadata.algorithmVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalCandidates, filteredOut, generationTimeMs, algorithmVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RomanceEventGenerationResponseGenerationMetadata {\n");
    sb.append("    totalCandidates: ").append(toIndentedString(totalCandidates)).append("\n");
    sb.append("    filteredOut: ").append(toIndentedString(filteredOut)).append("\n");
    sb.append("    generationTimeMs: ").append(toIndentedString(generationTimeMs)).append("\n");
    sb.append("    algorithmVersion: ").append(toIndentedString(algorithmVersion)).append("\n");
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

