package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.RomanceRelationship;
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
 * GetRomanceRelationships200Response
 */

@JsonTypeName("getRomanceRelationships_200_response")

public class GetRomanceRelationships200Response {

  @Valid
  private List<@Valid RomanceRelationship> activeRomances = new ArrayList<>();

  private @Nullable Integer maxConcurrentRomances;

  public GetRomanceRelationships200Response activeRomances(List<@Valid RomanceRelationship> activeRomances) {
    this.activeRomances = activeRomances;
    return this;
  }

  public GetRomanceRelationships200Response addActiveRomancesItem(RomanceRelationship activeRomancesItem) {
    if (this.activeRomances == null) {
      this.activeRomances = new ArrayList<>();
    }
    this.activeRomances.add(activeRomancesItem);
    return this;
  }

  /**
   * Get activeRomances
   * @return activeRomances
   */
  @Valid 
  @Schema(name = "active_romances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("active_romances")
  public List<@Valid RomanceRelationship> getActiveRomances() {
    return activeRomances;
  }

  public void setActiveRomances(List<@Valid RomanceRelationship> activeRomances) {
    this.activeRomances = activeRomances;
  }

  public GetRomanceRelationships200Response maxConcurrentRomances(@Nullable Integer maxConcurrentRomances) {
    this.maxConcurrentRomances = maxConcurrentRomances;
    return this;
  }

  /**
   * Можно одновременно несколько романов
   * @return maxConcurrentRomances
   */
  
  @Schema(name = "max_concurrent_romances", example = "3", description = "Можно одновременно несколько романов", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("max_concurrent_romances")
  public @Nullable Integer getMaxConcurrentRomances() {
    return maxConcurrentRomances;
  }

  public void setMaxConcurrentRomances(@Nullable Integer maxConcurrentRomances) {
    this.maxConcurrentRomances = maxConcurrentRomances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetRomanceRelationships200Response getRomanceRelationships200Response = (GetRomanceRelationships200Response) o;
    return Objects.equals(this.activeRomances, getRomanceRelationships200Response.activeRomances) &&
        Objects.equals(this.maxConcurrentRomances, getRomanceRelationships200Response.maxConcurrentRomances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(activeRomances, maxConcurrentRomances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetRomanceRelationships200Response {\n");
    sb.append("    activeRomances: ").append(toIndentedString(activeRomances)).append("\n");
    sb.append("    maxConcurrentRomances: ").append(toIndentedString(maxConcurrentRomances)).append("\n");
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

