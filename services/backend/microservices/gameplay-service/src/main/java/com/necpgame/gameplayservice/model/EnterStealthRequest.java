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
 * EnterStealthRequest
 */

@JsonTypeName("enterStealth_request")

public class EnterStealthRequest {

  private String characterId;

  private @Nullable Boolean useImplants;

  public EnterStealthRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EnterStealthRequest(String characterId) {
    this.characterId = characterId;
  }

  public EnterStealthRequest characterId(String characterId) {
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

  public EnterStealthRequest useImplants(@Nullable Boolean useImplants) {
    this.useImplants = useImplants;
    return this;
  }

  /**
   * Использовать импланты (оптический камуфляж)
   * @return useImplants
   */
  
  @Schema(name = "use_implants", description = "Использовать импланты (оптический камуфляж)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("use_implants")
  public @Nullable Boolean getUseImplants() {
    return useImplants;
  }

  public void setUseImplants(@Nullable Boolean useImplants) {
    this.useImplants = useImplants;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EnterStealthRequest enterStealthRequest = (EnterStealthRequest) o;
    return Objects.equals(this.characterId, enterStealthRequest.characterId) &&
        Objects.equals(this.useImplants, enterStealthRequest.useImplants);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, useImplants);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EnterStealthRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    useImplants: ").append(toIndentedString(useImplants)).append("\n");
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

