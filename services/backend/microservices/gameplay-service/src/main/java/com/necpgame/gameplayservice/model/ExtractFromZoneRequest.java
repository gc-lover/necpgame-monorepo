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
 * ExtractFromZoneRequest
 */

@JsonTypeName("extractFromZone_request")

public class ExtractFromZoneRequest {

  private String characterId;

  private String extractionPointId;

  public ExtractFromZoneRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ExtractFromZoneRequest(String characterId, String extractionPointId) {
    this.characterId = characterId;
    this.extractionPointId = extractionPointId;
  }

  public ExtractFromZoneRequest characterId(String characterId) {
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

  public ExtractFromZoneRequest extractionPointId(String extractionPointId) {
    this.extractionPointId = extractionPointId;
    return this;
  }

  /**
   * Get extractionPointId
   * @return extractionPointId
   */
  @NotNull 
  @Schema(name = "extraction_point_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("extraction_point_id")
  public String getExtractionPointId() {
    return extractionPointId;
  }

  public void setExtractionPointId(String extractionPointId) {
    this.extractionPointId = extractionPointId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExtractFromZoneRequest extractFromZoneRequest = (ExtractFromZoneRequest) o;
    return Objects.equals(this.characterId, extractFromZoneRequest.characterId) &&
        Objects.equals(this.extractionPointId, extractFromZoneRequest.extractionPointId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, extractionPointId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExtractFromZoneRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    extractionPointId: ").append(toIndentedString(extractionPointId)).append("\n");
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

