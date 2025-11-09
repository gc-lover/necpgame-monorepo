package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GameCharacterState;
import com.necpgame.gameplayservice.model.GameLocation;
import com.necpgame.gameplayservice.model.GameReturnResponseActiveQuestsInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * GameReturnResponse
 */


public class GameReturnResponse {

  private UUID gameSessionId;

  private UUID characterId;

  private GameLocation currentLocation;

  private GameCharacterState characterState;

  @Valid
  private List<@Valid GameReturnResponseActiveQuestsInner> activeQuests = new ArrayList<>();

  public GameReturnResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameReturnResponse(UUID gameSessionId, UUID characterId, GameLocation currentLocation, GameCharacterState characterState) {
    this.gameSessionId = gameSessionId;
    this.characterId = characterId;
    this.currentLocation = currentLocation;
    this.characterState = characterState;
  }

  public GameReturnResponse gameSessionId(UUID gameSessionId) {
    this.gameSessionId = gameSessionId;
    return this;
  }

  /**
   * ID игровой сессии
   * @return gameSessionId
   */
  @NotNull @Valid 
  @Schema(name = "gameSessionId", description = "ID игровой сессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gameSessionId")
  public UUID getGameSessionId() {
    return gameSessionId;
  }

  public void setGameSessionId(UUID gameSessionId) {
    this.gameSessionId = gameSessionId;
  }

  public GameReturnResponse characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * ID персонажа
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", example = "550e8400-e29b-41d4-a716-446655440000", description = "ID персонажа", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public GameReturnResponse currentLocation(GameLocation currentLocation) {
    this.currentLocation = currentLocation;
    return this;
  }

  /**
   * Get currentLocation
   * @return currentLocation
   */
  @NotNull @Valid 
  @Schema(name = "currentLocation", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentLocation")
  public GameLocation getCurrentLocation() {
    return currentLocation;
  }

  public void setCurrentLocation(GameLocation currentLocation) {
    this.currentLocation = currentLocation;
  }

  public GameReturnResponse characterState(GameCharacterState characterState) {
    this.characterState = characterState;
    return this;
  }

  /**
   * Get characterState
   * @return characterState
   */
  @NotNull @Valid 
  @Schema(name = "characterState", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterState")
  public GameCharacterState getCharacterState() {
    return characterState;
  }

  public void setCharacterState(GameCharacterState characterState) {
    this.characterState = characterState;
  }

  public GameReturnResponse activeQuests(List<@Valid GameReturnResponseActiveQuestsInner> activeQuests) {
    this.activeQuests = activeQuests;
    return this;
  }

  public GameReturnResponse addActiveQuestsItem(GameReturnResponseActiveQuestsInner activeQuestsItem) {
    if (this.activeQuests == null) {
      this.activeQuests = new ArrayList<>();
    }
    this.activeQuests.add(activeQuestsItem);
    return this;
  }

  /**
   * Активные квесты
   * @return activeQuests
   */
  @Valid 
  @Schema(name = "activeQuests", description = "Активные квесты", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeQuests")
  public List<@Valid GameReturnResponseActiveQuestsInner> getActiveQuests() {
    return activeQuests;
  }

  public void setActiveQuests(List<@Valid GameReturnResponseActiveQuestsInner> activeQuests) {
    this.activeQuests = activeQuests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameReturnResponse gameReturnResponse = (GameReturnResponse) o;
    return Objects.equals(this.gameSessionId, gameReturnResponse.gameSessionId) &&
        Objects.equals(this.characterId, gameReturnResponse.characterId) &&
        Objects.equals(this.currentLocation, gameReturnResponse.currentLocation) &&
        Objects.equals(this.characterState, gameReturnResponse.characterState) &&
        Objects.equals(this.activeQuests, gameReturnResponse.activeQuests);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameSessionId, characterId, currentLocation, characterState, activeQuests);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameReturnResponse {\n");
    sb.append("    gameSessionId: ").append(toIndentedString(gameSessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    characterState: ").append(toIndentedString(characterState)).append("\n");
    sb.append("    activeQuests: ").append(toIndentedString(activeQuests)).append("\n");
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

