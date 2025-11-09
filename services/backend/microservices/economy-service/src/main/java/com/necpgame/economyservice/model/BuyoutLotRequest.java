package com.necpgame.economyservice.model;

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
 * BuyoutLotRequest
 */

@JsonTypeName("buyoutLot_request")

public class BuyoutLotRequest {

  private String characterId;

  private String lotId;

  public BuyoutLotRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BuyoutLotRequest(String characterId, String lotId) {
    this.characterId = characterId;
    this.lotId = lotId;
  }

  public BuyoutLotRequest characterId(String characterId) {
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

  public BuyoutLotRequest lotId(String lotId) {
    this.lotId = lotId;
    return this;
  }

  /**
   * Get lotId
   * @return lotId
   */
  @NotNull 
  @Schema(name = "lot_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lot_id")
  public String getLotId() {
    return lotId;
  }

  public void setLotId(String lotId) {
    this.lotId = lotId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuyoutLotRequest buyoutLotRequest = (BuyoutLotRequest) o;
    return Objects.equals(this.characterId, buyoutLotRequest.characterId) &&
        Objects.equals(this.lotId, buyoutLotRequest.lotId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, lotId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuyoutLotRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lotId: ").append(toIndentedString(lotId)).append("\n");
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

