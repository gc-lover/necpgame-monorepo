package com.necpgame.sessionservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * Session
 */


public class Session {

  private String sessionId;

  private String playerId;

  private @Nullable String accountId;

  private @Nullable String characterId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    AFK("AFK"),
    
    DISCONNECTED("DISCONNECTED"),
    
    TERMINATED("TERMINATED");

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

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastHeartbeatAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable String reconnectToken;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime afkSince;

  public Session() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Session(String sessionId, String playerId, StatusEnum status, OffsetDateTime createdAt) {
    this.sessionId = sessionId;
    this.playerId = playerId;
    this.status = status;
    this.createdAt = createdAt;
  }

  public Session sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  @NotNull 
  @Schema(name = "sessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("sessionId")
  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public Session playerId(String playerId) {
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

  public Session accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "accountId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accountId")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public Session characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characterId")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public Session status(StatusEnum status) {
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

  public Session createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @NotNull @Valid 
  @Schema(name = "createdAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("createdAt")
  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Session lastHeartbeatAt(@Nullable OffsetDateTime lastHeartbeatAt) {
    this.lastHeartbeatAt = lastHeartbeatAt;
    return this;
  }

  /**
   * Get lastHeartbeatAt
   * @return lastHeartbeatAt
   */
  @Valid 
  @Schema(name = "lastHeartbeatAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastHeartbeatAt")
  public @Nullable OffsetDateTime getLastHeartbeatAt() {
    return lastHeartbeatAt;
  }

  public void setLastHeartbeatAt(@Nullable OffsetDateTime lastHeartbeatAt) {
    this.lastHeartbeatAt = lastHeartbeatAt;
  }

  public Session expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public Session reconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
    return this;
  }

  /**
   * Get reconnectToken
   * @return reconnectToken
   */
  
  @Schema(name = "reconnectToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reconnectToken")
  public @Nullable String getReconnectToken() {
    return reconnectToken;
  }

  public void setReconnectToken(@Nullable String reconnectToken) {
    this.reconnectToken = reconnectToken;
  }

  public Session afkSince(@Nullable OffsetDateTime afkSince) {
    this.afkSince = afkSince;
    return this;
  }

  /**
   * Get afkSince
   * @return afkSince
   */
  @Valid 
  @Schema(name = "afkSince", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("afkSince")
  public @Nullable OffsetDateTime getAfkSince() {
    return afkSince;
  }

  public void setAfkSince(@Nullable OffsetDateTime afkSince) {
    this.afkSince = afkSince;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Session session = (Session) o;
    return Objects.equals(this.sessionId, session.sessionId) &&
        Objects.equals(this.playerId, session.playerId) &&
        Objects.equals(this.accountId, session.accountId) &&
        Objects.equals(this.characterId, session.characterId) &&
        Objects.equals(this.status, session.status) &&
        Objects.equals(this.createdAt, session.createdAt) &&
        Objects.equals(this.lastHeartbeatAt, session.lastHeartbeatAt) &&
        Objects.equals(this.expiresAt, session.expiresAt) &&
        Objects.equals(this.reconnectToken, session.reconnectToken) &&
        Objects.equals(this.afkSince, session.afkSince);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, playerId, accountId, characterId, status, createdAt, lastHeartbeatAt, expiresAt, reconnectToken, afkSince);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Session {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastHeartbeatAt: ").append(toIndentedString(lastHeartbeatAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    reconnectToken: ").append(toIndentedString(reconnectToken)).append("\n");
    sb.append("    afkSince: ").append(toIndentedString(afkSince)).append("\n");
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

