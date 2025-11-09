package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TerritoryChange
 */


public class TerritoryChange {

  private @Nullable String territoryId;

  private @Nullable String previousOwner;

  private @Nullable String newOwner;

  public TerritoryChange territoryId(@Nullable String territoryId) {
    this.territoryId = territoryId;
    return this;
  }

  /**
   * Get territoryId
   * @return territoryId
   */
  
  @Schema(name = "territoryId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("territoryId")
  public @Nullable String getTerritoryId() {
    return territoryId;
  }

  public void setTerritoryId(@Nullable String territoryId) {
    this.territoryId = territoryId;
  }

  public TerritoryChange previousOwner(@Nullable String previousOwner) {
    this.previousOwner = previousOwner;
    return this;
  }

  /**
   * Get previousOwner
   * @return previousOwner
   */
  
  @Schema(name = "previousOwner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previousOwner")
  public @Nullable String getPreviousOwner() {
    return previousOwner;
  }

  public void setPreviousOwner(@Nullable String previousOwner) {
    this.previousOwner = previousOwner;
  }

  public TerritoryChange newOwner(@Nullable String newOwner) {
    this.newOwner = newOwner;
    return this;
  }

  /**
   * Get newOwner
   * @return newOwner
   */
  
  @Schema(name = "newOwner", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("newOwner")
  public @Nullable String getNewOwner() {
    return newOwner;
  }

  public void setNewOwner(@Nullable String newOwner) {
    this.newOwner = newOwner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerritoryChange territoryChange = (TerritoryChange) o;
    return Objects.equals(this.territoryId, territoryChange.territoryId) &&
        Objects.equals(this.previousOwner, territoryChange.previousOwner) &&
        Objects.equals(this.newOwner, territoryChange.newOwner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(territoryId, previousOwner, newOwner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TerritoryChange {\n");
    sb.append("    territoryId: ").append(toIndentedString(territoryId)).append("\n");
    sb.append("    previousOwner: ").append(toIndentedString(previousOwner)).append("\n");
    sb.append("    newOwner: ").append(toIndentedString(newOwner)).append("\n");
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

