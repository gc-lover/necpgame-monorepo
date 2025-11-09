package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.gameplayservice.model.Daemon;
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
 * GetAvailableDaemons200Response
 */

@JsonTypeName("getAvailableDaemons_200_response")

public class GetAvailableDaemons200Response {

  private @Nullable String characterId;

  @Valid
  private List<@Valid Daemon> daemons = new ArrayList<>();

  public GetAvailableDaemons200Response characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public GetAvailableDaemons200Response daemons(List<@Valid Daemon> daemons) {
    this.daemons = daemons;
    return this;
  }

  public GetAvailableDaemons200Response addDaemonsItem(Daemon daemonsItem) {
    if (this.daemons == null) {
      this.daemons = new ArrayList<>();
    }
    this.daemons.add(daemonsItem);
    return this;
  }

  /**
   * Get daemons
   * @return daemons
   */
  @Valid 
  @Schema(name = "daemons", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("daemons")
  public List<@Valid Daemon> getDaemons() {
    return daemons;
  }

  public void setDaemons(List<@Valid Daemon> daemons) {
    this.daemons = daemons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAvailableDaemons200Response getAvailableDaemons200Response = (GetAvailableDaemons200Response) o;
    return Objects.equals(this.characterId, getAvailableDaemons200Response.characterId) &&
        Objects.equals(this.daemons, getAvailableDaemons200Response.daemons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, daemons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAvailableDaemons200Response {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    daemons: ").append(toIndentedString(daemons)).append("\n");
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

