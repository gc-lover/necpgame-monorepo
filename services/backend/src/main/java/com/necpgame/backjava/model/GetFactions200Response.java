package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.backjava.model.Faction;
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
 * GetFactions200Response
 */

@JsonTypeName("getFactions_200_response")

public class GetFactions200Response {

  @Valid
  private List<@Valid Faction> factions = new ArrayList<>();

  public GetFactions200Response() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetFactions200Response(List<@Valid Faction> factions) {
    this.factions = factions;
  }

  public GetFactions200Response factions(List<@Valid Faction> factions) {
    this.factions = factions;
    return this;
  }

  public GetFactions200Response addFactionsItem(Faction factionsItem) {
    if (this.factions == null) {
      this.factions = new ArrayList<>();
    }
    this.factions.add(factionsItem);
    return this;
  }

  /**
   * РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… С„СЂР°РєС†РёР№
   * @return factions
   */
  @NotNull @Valid 
  @Schema(name = "factions", description = "РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… С„СЂР°РєС†РёР№", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("factions")
  public List<@Valid Faction> getFactions() {
    return factions;
  }

  public void setFactions(List<@Valid Faction> factions) {
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
    GetFactions200Response getFactions200Response = (GetFactions200Response) o;
    return Objects.equals(this.factions, getFactions200Response.factions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factions);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFactions200Response {\n");
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

