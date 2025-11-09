package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * AcceptContractRequest
 */

@JsonTypeName("acceptContract_request")

public class AcceptContractRequest {

  private @Nullable UUID characterId;

  private @Nullable Integer collateral;

  public AcceptContractRequest characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public AcceptContractRequest collateral(@Nullable Integer collateral) {
    this.collateral = collateral;
    return this;
  }

  /**
   * Залог (если требуется)
   * @return collateral
   */
  
  @Schema(name = "collateral", description = "Залог (если требуется)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("collateral")
  public @Nullable Integer getCollateral() {
    return collateral;
  }

  public void setCollateral(@Nullable Integer collateral) {
    this.collateral = collateral;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AcceptContractRequest acceptContractRequest = (AcceptContractRequest) o;
    return Objects.equals(this.characterId, acceptContractRequest.characterId) &&
        Objects.equals(this.collateral, acceptContractRequest.collateral);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, collateral);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AcceptContractRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    collateral: ").append(toIndentedString(collateral)).append("\n");
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

