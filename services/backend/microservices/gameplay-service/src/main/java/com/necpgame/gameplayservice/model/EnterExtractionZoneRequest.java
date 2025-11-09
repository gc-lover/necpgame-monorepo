package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * EnterExtractionZoneRequest
 */

@JsonTypeName("enterExtractionZone_request")

public class EnterExtractionZoneRequest {

  private String characterId;

  private String zoneId;

  public EnterExtractionZoneRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnterExtractionZoneRequest(String characterId, String zoneId) {
    this.characterId = characterId;
    this.zoneId = zoneId;
  }

  public EnterExtractionZoneRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public EnterExtractionZoneRequest zoneId(String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  @NotNull 
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("zone_id")
  public String getZoneId() {
    return zoneId;
  }

  public void setZoneId(String zoneId) {
    this.zoneId = zoneId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterExtractionZoneRequest enterExtractionZoneRequest = (EnterExtractionZoneRequest) o;
    return Objects.equals(this.characterId, enterExtractionZoneRequest.characterId) &&
        Objects.equals(this.zoneId, enterExtractionZoneRequest.zoneId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, zoneId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterExtractionZoneRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
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

