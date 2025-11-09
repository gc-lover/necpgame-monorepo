package com.necpgame.economyservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.economyservice.model.StartTradingRunRequestCargoInner;
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
 * StartTradingRunRequest
 */

@JsonTypeName("startTradingRun_request")

public class StartTradingRunRequest {

  private String characterId;

  @Valid
  private List<@Valid StartTradingRunRequestCargoInner> cargo = new ArrayList<>();

  public StartTradingRunRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StartTradingRunRequest(String characterId, List<@Valid StartTradingRunRequestCargoInner> cargo) {
    this.characterId = characterId;
    this.cargo = cargo;
  }

  public StartTradingRunRequest characterId(String characterId) {
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

  public StartTradingRunRequest cargo(List<@Valid StartTradingRunRequestCargoInner> cargo) {
    this.cargo = cargo;
    return this;
  }

  public StartTradingRunRequest addCargoItem(StartTradingRunRequestCargoInner cargoItem) {
    if (this.cargo == null) {
      this.cargo = new ArrayList<>();
    }
    this.cargo.add(cargoItem);
    return this;
  }

  /**
   * Get cargo
   * @return cargo
   */
  @NotNull @Valid 
  @Schema(name = "cargo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("cargo")
  public List<@Valid StartTradingRunRequestCargoInner> getCargo() {
    return cargo;
  }

  public void setCargo(List<@Valid StartTradingRunRequestCargoInner> cargo) {
    this.cargo = cargo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StartTradingRunRequest startTradingRunRequest = (StartTradingRunRequest) o;
    return Objects.equals(this.characterId, startTradingRunRequest.characterId) &&
        Objects.equals(this.cargo, startTradingRunRequest.cargo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, cargo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StartTradingRunRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    cargo: ").append(toIndentedString(cargo)).append("\n");
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

