package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.AntiAbuseStatus;
import com.necpgame.gameplayservice.model.CompanionDetailBonding;
import com.necpgame.gameplayservice.model.CompanionLoadout;
import com.necpgame.gameplayservice.model.CompanionProgression;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CompanionDetail
 */


public class CompanionDetail {

  private String companionId;

  private String playerId;

  /**
   * Gets or Sets type
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

  private @Nullable String rarity;

  private @Nullable String nickname;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    IN_MISSION("IN_MISSION"),
    
    SUSPENDED("SUSPENDED"),
    
    ARCHIVED("ARCHIVED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status;

  private @Nullable Integer loyalty;

  private @Nullable CompanionDetailBonding bonding;

  /**
   * Gets or Sets aiMode
   */
  public enum AiModeEnum {
    ASSIST("assist"),
    
    DEFEND("defend"),
    
    SENTRY("sentry"),
    
    AUTONOMOUS("autonomous");

    private final String value;

    AiModeEnum(String value) {
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
    public static AiModeEnum fromValue(String value) {
      for (AiModeEnum b : AiModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AiModeEnum aiMode;

  private @Nullable CompanionProgression progression;

  private @Nullable CompanionLoadout loadout;

  private @Nullable Integer missionSlots;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUsedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable AntiAbuseStatus antiAbuse;

  public CompanionDetail() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CompanionDetail(String companionId, String playerId, TypeEnum type, StatusEnum status) {
    this.companionId = companionId;
    this.playerId = playerId;
    this.type = type;
    this.status = status;
  }

  public CompanionDetail companionId(String companionId) {
    this.companionId = companionId;
    return this;
  }

  /**
   * Get companionId
   * @return companionId
   */
  @NotNull 
  @Schema(name = "companionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("companionId")
  public String getCompanionId() {
    return companionId;
  }

  public void setCompanionId(String companionId) {
    this.companionId = companionId;
  }

  public CompanionDetail playerId(String playerId) {
    this.playerId = playerId;
    return this;
  }

  /**
   * Get playerId
   * @return playerId
   */
  @NotNull 
  @Schema(name = "playerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("playerId")
  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public CompanionDetail type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public CompanionDetail subType(@Nullable String subType) {
    this.subType = subType;
    return this;
  }

  /**
   * Get subType
   * @return subType
   */
  
  @Schema(name = "subType", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subType")
  public @Nullable String getSubType() {
    return subType;
  }

  public void setSubType(@Nullable String subType) {
    this.subType = subType;
  }

  public CompanionDetail rarity(@Nullable String rarity) {
    this.rarity = rarity;
    return this;
  }

  /**
   * Get rarity
   * @return rarity
   */
  
  @Schema(name = "rarity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarity")
  public @Nullable String getRarity() {
    return rarity;
  }

  public void setRarity(@Nullable String rarity) {
    this.rarity = rarity;
  }

  public CompanionDetail nickname(@Nullable String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("nickname")
  public @Nullable String getNickname() {
    return nickname;
  }

  public void setNickname(@Nullable String nickname) {
    this.nickname = nickname;
  }

  public CompanionDetail status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  @NotNull 
  @Schema(name = "status", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public CompanionDetail loyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
    return this;
  }

  /**
   * Get loyalty
   * minimum: 0
   * maximum: 100
   * @return loyalty
   */
  @Min(value = 0) @Max(value = 100) 
  @Schema(name = "loyalty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loyalty")
  public @Nullable Integer getLoyalty() {
    return loyalty;
  }

  public void setLoyalty(@Nullable Integer loyalty) {
    this.loyalty = loyalty;
  }

  public CompanionDetail bonding(@Nullable CompanionDetailBonding bonding) {
    this.bonding = bonding;
    return this;
  }

  /**
   * Get bonding
   * @return bonding
   */
  @Valid 
  @Schema(name = "bonding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonding")
  public @Nullable CompanionDetailBonding getBonding() {
    return bonding;
  }

  public void setBonding(@Nullable CompanionDetailBonding bonding) {
    this.bonding = bonding;
  }

  public CompanionDetail aiMode(@Nullable AiModeEnum aiMode) {
    this.aiMode = aiMode;
    return this;
  }

  /**
   * Get aiMode
   * @return aiMode
   */
  
  @Schema(name = "aiMode", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aiMode")
  public @Nullable AiModeEnum getAiMode() {
    return aiMode;
  }

  public void setAiMode(@Nullable AiModeEnum aiMode) {
    this.aiMode = aiMode;
  }

  public CompanionDetail progression(@Nullable CompanionProgression progression) {
    this.progression = progression;
    return this;
  }

  /**
   * Get progression
   * @return progression
   */
  @Valid 
  @Schema(name = "progression", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("progression")
  public @Nullable CompanionProgression getProgression() {
    return progression;
  }

  public void setProgression(@Nullable CompanionProgression progression) {
    this.progression = progression;
  }

  public CompanionDetail loadout(@Nullable CompanionLoadout loadout) {
    this.loadout = loadout;
    return this;
  }

  /**
   * Get loadout
   * @return loadout
   */
  @Valid 
  @Schema(name = "loadout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loadout")
  public @Nullable CompanionLoadout getLoadout() {
    return loadout;
  }

  public void setLoadout(@Nullable CompanionLoadout loadout) {
    this.loadout = loadout;
  }

  public CompanionDetail missionSlots(@Nullable Integer missionSlots) {
    this.missionSlots = missionSlots;
    return this;
  }

  /**
   * Доступные слоты миссий
   * @return missionSlots
   */
  
  @Schema(name = "missionSlots", description = "Доступные слоты миссий", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("missionSlots")
  public @Nullable Integer getMissionSlots() {
    return missionSlots;
  }

  public void setMissionSlots(@Nullable Integer missionSlots) {
    this.missionSlots = missionSlots;
  }

  public CompanionDetail lastUsedAt(@Nullable OffsetDateTime lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
    return this;
  }

  /**
   * Get lastUsedAt
   * @return lastUsedAt
   */
  @Valid 
  @Schema(name = "lastUsedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastUsedAt")
  public @Nullable OffsetDateTime getLastUsedAt() {
    return lastUsedAt;
  }

  public void setLastUsedAt(@Nullable OffsetDateTime lastUsedAt) {
    this.lastUsedAt = lastUsedAt;
  }

  public CompanionDetail createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("createdAt")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public CompanionDetail antiAbuse(@Nullable AntiAbuseStatus antiAbuse) {
    this.antiAbuse = antiAbuse;
    return this;
  }

  /**
   * Get antiAbuse
   * @return antiAbuse
   */
  @Valid 
  @Schema(name = "antiAbuse", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("antiAbuse")
  public @Nullable AntiAbuseStatus getAntiAbuse() {
    return antiAbuse;
  }

  public void setAntiAbuse(@Nullable AntiAbuseStatus antiAbuse) {
    this.antiAbuse = antiAbuse;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CompanionDetail companionDetail = (CompanionDetail) o;
    return Objects.equals(this.companionId, companionDetail.companionId) &&
        Objects.equals(this.playerId, companionDetail.playerId) &&
        Objects.equals(this.type, companionDetail.type) &&
        Objects.equals(this.subType, companionDetail.subType) &&
        Objects.equals(this.rarity, companionDetail.rarity) &&
        Objects.equals(this.nickname, companionDetail.nickname) &&
        Objects.equals(this.status, companionDetail.status) &&
        Objects.equals(this.loyalty, companionDetail.loyalty) &&
        Objects.equals(this.bonding, companionDetail.bonding) &&
        Objects.equals(this.aiMode, companionDetail.aiMode) &&
        Objects.equals(this.progression, companionDetail.progression) &&
        Objects.equals(this.loadout, companionDetail.loadout) &&
        Objects.equals(this.missionSlots, companionDetail.missionSlots) &&
        Objects.equals(this.lastUsedAt, companionDetail.lastUsedAt) &&
        Objects.equals(this.createdAt, companionDetail.createdAt) &&
        Objects.equals(this.antiAbuse, companionDetail.antiAbuse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(companionId, playerId, type, subType, rarity, nickname, status, loyalty, bonding, aiMode, progression, loadout, missionSlots, lastUsedAt, createdAt, antiAbuse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CompanionDetail {\n");
    sb.append("    companionId: ").append(toIndentedString(companionId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    subType: ").append(toIndentedString(subType)).append("\n");
    sb.append("    rarity: ").append(toIndentedString(rarity)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    loyalty: ").append(toIndentedString(loyalty)).append("\n");
    sb.append("    bonding: ").append(toIndentedString(bonding)).append("\n");
    sb.append("    aiMode: ").append(toIndentedString(aiMode)).append("\n");
    sb.append("    progression: ").append(toIndentedString(progression)).append("\n");
    sb.append("    loadout: ").append(toIndentedString(loadout)).append("\n");
    sb.append("    missionSlots: ").append(toIndentedString(missionSlots)).append("\n");
    sb.append("    lastUsedAt: ").append(toIndentedString(lastUsedAt)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    antiAbuse: ").append(toIndentedString(antiAbuse)).append("\n");
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

