package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.GetFactionPower200ResponseFactionsInner;
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
 * GetFactionPower200Response
 */

@JsonTypeName("getFactionPower_200_response")

public class GetFactionPower200Response {

  @Valid
  private List<@Valid GetFactionPower200ResponseFactionsInner> factions = new ArrayList<>();

  public GetFactionPower200Response factions(List<@Valid GetFactionPower200ResponseFactionsInner> factions) {
    this.factions = factions;
    return this;
  }

  public GetFactionPower200Response addFactionsItem(GetFactionPower200ResponseFactionsInner factionsItem) {
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
  public List<@Valid GetFactionPower200ResponseFactionsInner> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid GetFactionPower200ResponseFactionsInner> factions) {
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
    GetFactionPower200Response getFactionPower200Response = (GetFactionPower200Response) o;
    return Objects.equals(this.factions, getFactionPower200Response.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionPower200Response {\n");
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

