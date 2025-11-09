package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.FactionState;
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
 * GetFactionsState200Response
 */

@JsonTypeName("getFactionsState_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GetFactionsState200Response {

  @Valid
  private List<@Valid FactionState> factions = new ArrayList<>();

  public GetFactionsState200Response factions(List<@Valid FactionState> factions) {
    this.factions = factions;
    return this;
  }

  public GetFactionsState200Response addFactionsItem(FactionState factionsItem) {
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
  public List<@Valid FactionState> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid FactionState> factions) {
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
    GetFactionsState200Response getFactionsState200Response = (GetFactionsState200Response) o;
    return Objects.equals(this.factions, getFactionsState200Response.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactionsState200Response {\n");
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

