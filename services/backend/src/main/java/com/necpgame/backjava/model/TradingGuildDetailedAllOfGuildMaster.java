package com.necpgame.backjava.model;

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
 * TradingGuildDetailedAllOfGuildMaster
 */

@JsonTypeName("TradingGuildDetailed_allOf_guild_master")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class TradingGuildDetailedAllOfGuildMaster {

  private @Nullable UUID characterId;

  private @Nullable String characterName;

  public TradingGuildDetailedAllOfGuildMaster characterId(@Nullable UUID characterId) {
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

  public TradingGuildDetailedAllOfGuildMaster characterName(@Nullable String characterName) {
    this.characterName = characterName;
    return this;
  }

  /**
   * Get characterName
   * @return characterName
   */
  
  @Schema(name = "character_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_name")
  public @Nullable String getCharacterName() {
    return characterName;
  }

  public void setCharacterName(@Nullable String characterName) {
    this.characterName = characterName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TradingGuildDetailedAllOfGuildMaster tradingGuildDetailedAllOfGuildMaster = (TradingGuildDetailedAllOfGuildMaster) o;
    return Objects.equals(this.characterId, tradingGuildDetailedAllOfGuildMaster.characterId) &&
        Objects.equals(this.characterName, tradingGuildDetailedAllOfGuildMaster.characterName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, characterName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TradingGuildDetailedAllOfGuildMaster {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    characterName: ").append(toIndentedString(characterName)).append("\n");
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

