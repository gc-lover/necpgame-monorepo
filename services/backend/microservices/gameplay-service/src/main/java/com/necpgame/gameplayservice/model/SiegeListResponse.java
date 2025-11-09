package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.SiegePlan;
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
 * SiegeListResponse
 */


public class SiegeListResponse {

  private @Nullable String warId;

  @Valid
  private List<@Valid SiegePlan> sieges = new ArrayList<>();

  public SiegeListResponse warId(@Nullable String warId) {
    this.warId = warId;
    return this;
  }

  /**
   * Get warId
   * @return warId
   */
  
  @Schema(name = "warId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("warId")
  public @Nullable String getWarId() {
    return warId;
  }

  public void setWarId(@Nullable String warId) {
    this.warId = warId;
  }

  public SiegeListResponse sieges(List<@Valid SiegePlan> sieges) {
    this.sieges = sieges;
    return this;
  }

  public SiegeListResponse addSiegesItem(SiegePlan siegesItem) {
    if (this.sieges == null) {
      this.sieges = new ArrayList<>();
    }
    this.sieges.add(siegesItem);
    return this;
  }

  /**
   * Get sieges
   * @return sieges
   */
  @Valid 
  @Schema(name = "sieges", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sieges")
  public List<@Valid SiegePlan> getSieges() {
    return sieges;
  }

  public void setSieges(List<@Valid SiegePlan> sieges) {
    this.sieges = sieges;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SiegeListResponse siegeListResponse = (SiegeListResponse) o;
    return Objects.equals(this.warId, siegeListResponse.warId) &&
        Objects.equals(this.sieges, siegeListResponse.sieges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(warId, sieges);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SiegeListResponse {\n");
    sb.append("    warId: ").append(toIndentedString(warId)).append("\n");
    sb.append("    sieges: ").append(toIndentedString(sieges)).append("\n");
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

