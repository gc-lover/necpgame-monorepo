package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.narrativeservice.model.FactionEvolution;
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
 * GetFactionEvolution200Response
 */

@JsonTypeName("getFactionEvolution_200_response")

public class GetFactionEvolution200Response {

  @Valid
  private List<@Valid FactionEvolution> factions = new ArrayList<>();

  public GetFactionEvolution200Response factions(List<@Valid FactionEvolution> factions) {
    this.factions = factions;
    return this;
  }

  public GetFactionEvolution200Response addFactionsItem(FactionEvolution factionsItem) {
    if (this.factions == null) {
      this.factions = new ArrayList<>();
    }
    this.factions.add(factionsItem);
    return this;
  }

  /**
   * Get factions
   * @return factions
   */
  @Valid 
  @Schema(name = "factions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("factions")
  public List<@Valid FactionEvolution> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid FactionEvolution> factions) {
    this.factions = factions;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFactionEvolution200Response getFactionEvolution200Response = (GetFactionEvolution200Response) o;
    return Objects.equals(this.factions, getFactionEvolution200Response.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionEvolution200Response {\n");
    sb.append("    factions: ").append(toIndentedString(factions)).append("\n");
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

