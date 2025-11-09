package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.socialservice.model.PersonalNPC;
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
 * GetPersonalNPCs200Response
 */

@JsonTypeName("getPersonalNPCs_200_response")

public class GetPersonalNPCs200Response {

  @Valid
  private List<@Valid PersonalNPC> npcs = new ArrayList<>();

  public GetPersonalNPCs200Response npcs(List<@Valid PersonalNPC> npcs) {
    this.npcs = npcs;
    return this;
  }

  public GetPersonalNPCs200Response addNpcsItem(PersonalNPC npcsItem) {
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
  public List<@Valid PersonalNPC> getNpcs() {
    return npcs;
  }

  public void setNpcs(List<@Valid PersonalNPC> npcs) {
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
    GetPersonalNPCs200Response getPersonalNPCs200Response = (GetPersonalNPCs200Response) o;
    return Objects.equals(this.npcs, getPersonalNPCs200Response.npcs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(npcs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPersonalNPCs200Response {\n");
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

