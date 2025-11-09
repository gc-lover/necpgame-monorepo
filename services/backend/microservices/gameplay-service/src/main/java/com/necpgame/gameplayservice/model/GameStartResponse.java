package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.GameCharacterState;
import com.necpgame.gameplayservice.model.GameLocation;
import com.necpgame.gameplayservice.model.GameStartingItem;
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
 * GameStartResponse
 */


public class GameStartResponse {

  private UUID gameSessionId;

  private UUID characterId;

  private GameLocation currentLocation;

  private GameCharacterState characterState;

  @Valid
  private List<@Valid GameStartingItem> startingEquipment = new ArrayList<>();

  private String welcomeMessage;

  private Boolean tutorialEnabled;

  public GameStartResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GameStartResponse(UUID gameSessionId, UUID characterId, GameLocation currentLocation, GameCharacterState characterState, List<@Valid GameStartingItem> startingEquipment, String welcomeMessage, Boolean tutorialEnabled) {
    this.gameSessionId = gameSessionId;
    this.characterId = characterId;
    this.currentLocation = currentLocation;
    this.characterState = characterState;
    this.startingEquipment = startingEquipment;
    this.welcomeMessage = welcomeMessage;
    this.tutorialEnabled = tutorialEnabled;
  }

  public GameStartResponse gameSessionId(UUID gameSessionId) {
    this.gameSessionId = gameSessionId;
    return this;
  }

  /**
   * ID игровой сессии
   * @return gameSessionId
   */
  @NotNull @Valid 
  @Schema(name = "gameSessionId", example = "660f9511-f30c-52e5-b827-557766551111", description = "ID игровой сессии", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("gameSessionId")
  public UUID getGameSessionId() {
    return gameSessionId;
  }

  public void setGameSessionId(UUID gameSessionId) {
    this.gameSessionId = gameSessionId;
  }

  public GameStartResponse characterId(UUID characterId) {
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

  public GameStartResponse currentLocation(GameLocation currentLocation) {
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

  public GameStartResponse characterState(GameCharacterState characterState) {
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

  public GameStartResponse startingEquipment(List<@Valid GameStartingItem> startingEquipment) {
    this.startingEquipment = startingEquipment;
    return this;
  }

  public GameStartResponse addStartingEquipmentItem(GameStartingItem startingEquipmentItem) {
    if (this.startingEquipment == null) {
      this.startingEquipment = new ArrayList<>();
    }
    this.startingEquipment.add(startingEquipmentItem);
    return this;
  }

  /**
   * Стартовое снаряжение
   * @return startingEquipment
   */
  @NotNull @Valid 
  @Schema(name = "startingEquipment", description = "Стартовое снаряжение", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startingEquipment")
  public List<@Valid GameStartingItem> getStartingEquipment() {
    return startingEquipment;
  }

  public void setStartingEquipment(List<@Valid GameStartingItem> startingEquipment) {
    this.startingEquipment = startingEquipment;
  }

  public GameStartResponse welcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
    return this;
  }

  /**
   * Приветственное сообщение
   * @return welcomeMessage
   */
  @NotNull @Size(min = 10, max = 1000) 
  @Schema(name = "welcomeMessage", example = "Добро пожаловать в Night City. Вы стоите в центре корпоративного района Downtown. Неоновые вывески мигают на стенах зданий. Ваше приключение начинается...", description = "Приветственное сообщение", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("welcomeMessage")
  public String getWelcomeMessage() {
    return welcomeMessage;
  }

  public void setWelcomeMessage(String welcomeMessage) {
    this.welcomeMessage = welcomeMessage;
  }

  public GameStartResponse tutorialEnabled(Boolean tutorialEnabled) {
    this.tutorialEnabled = tutorialEnabled;
    return this;
  }

  /**
   * Включен ли туториал
   * @return tutorialEnabled
   */
  @NotNull 
  @Schema(name = "tutorialEnabled", example = "true", description = "Включен ли туториал", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("tutorialEnabled")
  public Boolean getTutorialEnabled() {
    return tutorialEnabled;
  }

  public void setTutorialEnabled(Boolean tutorialEnabled) {
    this.tutorialEnabled = tutorialEnabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameStartResponse gameStartResponse = (GameStartResponse) o;
    return Objects.equals(this.gameSessionId, gameStartResponse.gameSessionId) &&
        Objects.equals(this.characterId, gameStartResponse.characterId) &&
        Objects.equals(this.currentLocation, gameStartResponse.currentLocation) &&
        Objects.equals(this.characterState, gameStartResponse.characterState) &&
        Objects.equals(this.startingEquipment, gameStartResponse.startingEquipment) &&
        Objects.equals(this.welcomeMessage, gameStartResponse.welcomeMessage) &&
        Objects.equals(this.tutorialEnabled, gameStartResponse.tutorialEnabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gameSessionId, characterId, currentLocation, characterState, startingEquipment, welcomeMessage, tutorialEnabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameStartResponse {\n");
    sb.append("    gameSessionId: ").append(toIndentedString(gameSessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    currentLocation: ").append(toIndentedString(currentLocation)).append("\n");
    sb.append("    characterState: ").append(toIndentedString(characterState)).append("\n");
    sb.append("    startingEquipment: ").append(toIndentedString(startingEquipment)).append("\n");
    sb.append("    welcomeMessage: ").append(toIndentedString(welcomeMessage)).append("\n");
    sb.append("    tutorialEnabled: ").append(toIndentedString(tutorialEnabled)).append("\n");
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

