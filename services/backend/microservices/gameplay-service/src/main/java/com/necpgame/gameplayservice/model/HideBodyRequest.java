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
 * HideBodyRequest
 */

@JsonTypeName("hideBody_request")

public class HideBodyRequest {

  private String characterId;

  private String bodyId;

  private String hidingSpotId;

  public HideBodyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HideBodyRequest(String characterId, String bodyId, String hidingSpotId) {
    this.characterId = characterId;
    this.bodyId = bodyId;
    this.hidingSpotId = hidingSpotId;
  }

  public HideBodyRequest characterId(String characterId) {
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

  public HideBodyRequest bodyId(String bodyId) {
    this.bodyId = bodyId;
    return this;
  }

  /**
   * Get bodyId
   * @return bodyId
   */
  @NotNull 
  @Schema(name = "body_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("body_id")
  public String getBodyId() {
    return bodyId;
  }

  public void setBodyId(String bodyId) {
    this.bodyId = bodyId;
  }

  public HideBodyRequest hidingSpotId(String hidingSpotId) {
    this.hidingSpotId = hidingSpotId;
    return this;
  }

  /**
   * ID места для сокрытия (контейнер, вентиляция)
   * @return hidingSpotId
   */
  @NotNull 
  @Schema(name = "hiding_spot_id", description = "ID места для сокрытия (контейнер, вентиляция)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hiding_spot_id")
  public String getHidingSpotId() {
    return hidingSpotId;
  }

  public void setHidingSpotId(String hidingSpotId) {
    this.hidingSpotId = hidingSpotId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HideBodyRequest hideBodyRequest = (HideBodyRequest) o;
    return Objects.equals(this.characterId, hideBodyRequest.characterId) &&
        Objects.equals(this.bodyId, hideBodyRequest.bodyId) &&
        Objects.equals(this.hidingSpotId, hideBodyRequest.hidingSpotId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, bodyId, hidingSpotId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HideBodyRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    bodyId: ").append(toIndentedString(bodyId)).append("\n");
    sb.append("    hidingSpotId: ").append(toIndentedString(hidingSpotId)).append("\n");
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

