package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.AbilitySlotAssignment;
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
 * CreateCompanionRequest
 */


public class CreateCompanionRequest {

  private String playerId;

  /**
   * Архетип компаньона
   */
  public enum TypeEnum {
    COMBAT("combat"),
    
    UTILITY("utility"),
    
    SOCIAL("social");

    private final String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable String subType;

  /**
   * Gets or Sets rarity
   */
  public enum RarityEnum {
    COMMON("common"),
    
    RARE("rare"),
    
    EPIC("epic"),
    
    LEGENDARY("legendary"),
    
    MYTHIC("mythic");

    private final String value;

    RarityEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RarityEnum fromValue(String value) {
      for (RarityEnum b : RarityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable RarityEnum rarity;

  private @Nullable String nickname;

  @Valid
  private List<@Valid AbilitySlotAssignment> startingAbilities = new ArrayList<>();

  public CreateCompanionRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateCompanionRequest(String playerId, TypeEnum type) {
    this.playerId = playerId;
    this.type = type;
  }

  public CreateCompanionRequest playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Аккаунт игрока, создающего компаньона
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", description = "Аккаунт игрока, создающего компаньона", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public CreateCompanionRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Архетип компаньона
   * @return type
   */
  @NotNull 
  @Schema(name = "type", description = "Архетип компаньона", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public CreateCompanionRequest subType(@Nullable String subType) {
    this.subType = subType;
    return this;
  }

  /**
   * Класс внутри архетипа (assault_drone, medic_hound)
   * @return subType
   */
  
  @Schema(name = "subType", description = "Класс внутри архетипа (assault_drone, medic_hound)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subType")
  public @Nullable String getSubType() {
    return subType;
  }

  public void setSubType(@Nullable String subType) {
    this.subType = subType;
  }

  public CreateCompanionRequest rarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable RarityEnum getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable RarityEnum rarity) {
    this.rarity = rarity;
  }

  public CreateCompanionRequest nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  @Size(max = 32) 
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public CreateCompanionRequest startingAbilities(List<@Valid AbilitySlotAssignment> startingAbilities) {
    this.startingAbilities = startingAbilities;
    return this;
  }

  public CreateCompanionRequest addStartingAbilitiesItem(AbilitySlotAssignment startingAbilitiesItem) {
    if (this.startingAbilities == null) {
      this.startingAbilities = new ArrayList<>();
    }
    this.startingAbilities.add(startingAbilitiesItem);
    return this;
  }

  /**
   * Get startingAbilities
   * @return startingAbilities
   */
  @Valid @Size(max = 4) 
  @Schema(name = "startingAbilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startingAbilities")
  public List<@Valid AbilitySlotAssignment> getStartingAbilities() {
    return startingAbilities;
  }

  public void setStartingAbilities(List<@Valid AbilitySlotAssignment> startingAbilities) {
    this.startingAbilities = startingAbilities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateCompanionRequest createCompanionRequest = (CreateCompanionRequest) o;
    return Objects.equals(this.playerId, createCompanionRequest.playerId) &&
        Objects.equals(this.type, createCompanionRequest.type) &&
        Objects.equals(this.subType, createCompanionRequest.subType) &&
        Objects.equals(this.rarity, createCompanionRequest.rarity) &&
        Objects.equals(this.nickname, createCompanionRequest.nickname) &&
        Objects.equals(this.startingAbilities, createCompanionRequest.startingAbilities);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, type, subType, rarity, nickname, startingAbilities);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateCompanionRequest {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    subType: ").append(toIndentedString(subType)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    startingAbilities: ").append(toIndentedString(startingAbilities)).append("\n");
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

