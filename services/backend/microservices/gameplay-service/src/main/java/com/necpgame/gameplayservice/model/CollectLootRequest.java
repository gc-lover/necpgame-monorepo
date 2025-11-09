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
 * CollectLootRequest
 */

@JsonTypeName("collectLoot_request")

public class CollectLootRequest {

  private String characterId;

  private String lootItemId;

  public CollectLootRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CollectLootRequest(String characterId, String lootItemId) {
    this.characterId = characterId;
    this.lootItemId = lootItemId;
  }

  public CollectLootRequest characterId(String characterId) {
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

  public CollectLootRequest lootItemId(String lootItemId) {
    this.lootItemId = lootItemId;
    return this;
  }

  /**
   * Get lootItemId
   * @return lootItemId
   */
  @NotNull 
  @Schema(name = "loot_item_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("loot_item_id")
  public String getLootItemId() {
    return lootItemId;
  }

  public void setLootItemId(String lootItemId) {
    this.lootItemId = lootItemId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CollectLootRequest collectLootRequest = (CollectLootRequest) o;
    return Objects.equals(this.characterId, collectLootRequest.characterId) &&
        Objects.equals(this.lootItemId, collectLootRequest.lootItemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, lootItemId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CollectLootRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    lootItemId: ").append(toIndentedString(lootItemId)).append("\n");
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

