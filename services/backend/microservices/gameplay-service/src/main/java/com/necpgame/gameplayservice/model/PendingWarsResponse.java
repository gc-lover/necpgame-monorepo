package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.ClanWarSummary;
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
 * PendingWarsResponse
 */


public class PendingWarsResponse {

  @Valid
  private List<@Valid ClanWarSummary> wars = new ArrayList<>();

  public PendingWarsResponse wars(List<@Valid ClanWarSummary> wars) {
    this.wars = wars;
    return this;
  }

  public PendingWarsResponse addWarsItem(ClanWarSummary warsItem) {
    if (this.wars == null) {
      this.wars = new ArrayList<>();
    }
    this.wars.add(warsItem);
    return this;
  }

  /**
   * Get wars
   * @return wars
   */
  @Valid 
  @Schema(name = "wars", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wars")
  public List<@Valid ClanWarSummary> getWars() {
    return wars;
  }

  public void setWars(List<@Valid ClanWarSummary> wars) {
    this.wars = wars;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PendingWarsResponse pendingWarsResponse = (PendingWarsResponse) o;
    return Objects.equals(this.wars, pendingWarsResponse.wars);
  }

  @Override
  public int hashCode() {
    return Objects.hash(wars);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PendingWarsResponse {\n");
    sb.append("    wars: ").append(toIndentedString(wars)).append("\n");
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

