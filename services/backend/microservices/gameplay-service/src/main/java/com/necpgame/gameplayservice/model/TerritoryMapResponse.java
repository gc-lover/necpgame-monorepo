package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Territory;
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
 * TerritoryMapResponse
 */


public class TerritoryMapResponse {

  private @Nullable String region;

  @Valid
  private List<@Valid Territory> territories = new ArrayList<>();

  public TerritoryMapResponse region(@Nullable String region) {
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

  public TerritoryMapResponse territories(List<@Valid Territory> territories) {
    this.territories = territories;
    return this;
  }

  public TerritoryMapResponse addTerritoriesItem(Territory territoriesItem) {
    if (this.territories == null) {
      this.territories = new ArrayList<>();
    }
    this.territories.add(territoriesItem);
    return this;
  }

  /**
   * Get territories
   * @return territories
   */
  @Valid 
  @Schema(name = "territories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territories")
  public List<@Valid Territory> getTerritories() {
    return territories;
  }

  public void setTerritories(List<@Valid Territory> territories) {
    this.territories = territories;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerritoryMapResponse territoryMapResponse = (TerritoryMapResponse) o;
    return Objects.equals(this.region, territoryMapResponse.region) &&
        Objects.equals(this.territories, territoryMapResponse.territories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(region, territories);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TerritoryMapResponse {\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    territories: ").append(toIndentedString(territories)).append("\n");
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

