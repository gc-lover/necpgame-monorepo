package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.worldservice.model.FactionAISlider;
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
 * GetFactionAISliders200Response
 */

@JsonTypeName("getFactionAISliders_200_response")

public class GetFactionAISliders200Response {

  @Valid
  private List<@Valid FactionAISlider> factions = new ArrayList<>();

  public GetFactionAISliders200Response factions(List<@Valid FactionAISlider> factions) {
    this.factions = factions;
    return this;
  }

  public GetFactionAISliders200Response addFactionsItem(FactionAISlider factionsItem) {
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
  public List<@Valid FactionAISlider> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid FactionAISlider> factions) {
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
    GetFactionAISliders200Response getFactionAISliders200Response = (GetFactionAISliders200Response) o;
    return Objects.equals(this.factions, getFactionAISliders200Response.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionAISliders200Response {\n");
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

