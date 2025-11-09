package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.necpgame.backjava.model.NPC;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetNPCs200Response
 */

@JsonTypeName("getNPCs_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:49:00.930667100+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class GetNPCs200Response {

  @Valid
  private List<@Valid NPC> npcs = new ArrayList<>();

  public GetNPCs200Response npcs(List<@Valid NPC> npcs) {
    this.npcs = npcs;
    return this;
  }

  public GetNPCs200Response addNpcsItem(NPC npcsItem) {
    if (this.npcs == null) {
      this.npcs = new ArrayList<>();
    }
    this.npcs.add(npcsItem);
    return this;
  }

  /**
   * Get npcs
   * @return npcs
   */
  @Valid 
  @Schema(name = "npcs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npcs")
  public List<@Valid NPC> getNpcs() {
    return npcs;
  }

  public void setNpcs(List<@Valid NPC> npcs) {
    this.npcs = npcs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNPCs200Response getNPCs200Response = (GetNPCs200Response) o;
    return Objects.equals(this.npcs, getNPCs200Response.npcs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNPCs200Response {\n");
    sb.append("    npcs: ").append(toIndentedString(npcs)).append("\n");
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


