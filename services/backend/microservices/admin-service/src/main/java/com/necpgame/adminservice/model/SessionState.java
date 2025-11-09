package com.necpgame.adminservice.model;

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
 * SessionState
 */


public class SessionState {

  private @Nullable String sessionId;

  private @Nullable String characterId;

  private @Nullable String accountId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastHeartbeat;

  /**
   * Gets or Sets activity
   */
  public enum ActivityEnum {
    ACTIVE("active"),
    
    IDLE("idle"),
    
    AFK("afk");

    private final String value;

    ActivityEnum(String value) {
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
    public static ActivityEnum fromValue(String value) {
      for (ActivityEnum b : ActivityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ActivityEnum activity;

  private @Nullable String location;

  private @Nullable String partyId;

  private @Nullable String currentActivity;

  /**
   * Gets or Sets connectionStatus
   */
  public enum ConnectionStatusEnum {
    CONNECTED("connected"),
    
    DISCONNECTED("disconnected"),
    
    RECONNECTING("reconnecting");

    private final String value;

    ConnectionStatusEnum(String value) {
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
    public static ConnectionStatusEnum fromValue(String value) {
      for (ConnectionStatusEnum b : ConnectionStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ConnectionStatusEnum connectionStatus;

  public SessionState sessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  /**
   * Get sessionId
   * @return sessionId
   */
  
  @Schema(name = "session_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("session_id")
  public @Nullable String getSessionId() {
    return sessionId;
  }

  public void setSessionId(@Nullable String sessionId) {
    this.sessionId = sessionId;
  }

  public SessionState characterId(@Nullable String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable String characterId) {
    this.characterId = characterId;
  }

  public SessionState accountId(@Nullable String accountId) {
    this.accountId = accountId;
    return this;
  }

  /**
   * Get accountId
   * @return accountId
   */
  
  @Schema(name = "account_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("account_id")
  public @Nullable String getAccountId() {
    return accountId;
  }

  public void setAccountId(@Nullable String accountId) {
    this.accountId = accountId;
  }

  public SessionState createdAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * Get createdAt
   * @return createdAt
   */
  @Valid 
  @Schema(name = "created_at", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("created_at")
  public @Nullable OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(@Nullable OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public SessionState lastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
    return this;
  }

  /**
   * Get lastHeartbeat
   * @return lastHeartbeat
   */
  @Valid 
  @Schema(name = "last_heartbeat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_heartbeat")
  public @Nullable OffsetDateTime getLastHeartbeat() {
    return lastHeartbeat;
  }

  public void setLastHeartbeat(@Nullable OffsetDateTime lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
  }

  public SessionState activity(@Nullable ActivityEnum activity) {
    this.activity = activity;
    return this;
  }

  /**
   * Get activity
   * @return activity
   */
  
  @Schema(name = "activity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activity")
  public @Nullable ActivityEnum getActivity() {
    return activity;
  }

  public void setActivity(@Nullable ActivityEnum activity) {
    this.activity = activity;
  }

  public SessionState location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public SessionState partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "party_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party_id")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public SessionState currentActivity(@Nullable String currentActivity) {
    this.currentActivity = currentActivity;
    return this;
  }

  /**
   * Текущее действие (combat, trading, quest, etc)
   * @return currentActivity
   */
  
  @Schema(name = "current_activity", description = "Текущее действие (combat, trading, quest, etc)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("current_activity")
  public @Nullable String getCurrentActivity() {
    return currentActivity;
  }

  public void setCurrentActivity(@Nullable String currentActivity) {
    this.currentActivity = currentActivity;
  }

  public SessionState connectionStatus(@Nullable ConnectionStatusEnum connectionStatus) {
    this.connectionStatus = connectionStatus;
    return this;
  }

  /**
   * Get connectionStatus
   * @return connectionStatus
   */
  
  @Schema(name = "connection_status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connection_status")
  public @Nullable ConnectionStatusEnum getConnectionStatus() {
    return connectionStatus;
  }

  public void setConnectionStatus(@Nullable ConnectionStatusEnum connectionStatus) {
    this.connectionStatus = connectionStatus;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SessionState sessionState = (SessionState) o;
    return Objects.equals(this.sessionId, sessionState.sessionId) &&
        Objects.equals(this.characterId, sessionState.characterId) &&
        Objects.equals(this.accountId, sessionState.accountId) &&
        Objects.equals(this.createdAt, sessionState.createdAt) &&
        Objects.equals(this.lastHeartbeat, sessionState.lastHeartbeat) &&
        Objects.equals(this.activity, sessionState.activity) &&
        Objects.equals(this.location, sessionState.location) &&
        Objects.equals(this.partyId, sessionState.partyId) &&
        Objects.equals(this.currentActivity, sessionState.currentActivity) &&
        Objects.equals(this.connectionStatus, sessionState.connectionStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sessionId, characterId, accountId, createdAt, lastHeartbeat, activity, location, partyId, currentActivity, connectionStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SessionState {\n");
    sb.append("    sessionId: ").append(toIndentedString(sessionId)).append("\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    accountId: ").append(toIndentedString(accountId)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    lastHeartbeat: ").append(toIndentedString(lastHeartbeat)).append("\n");
    sb.append("    activity: ").append(toIndentedString(activity)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    currentActivity: ").append(toIndentedString(currentActivity)).append("\n");
    sb.append("    connectionStatus: ").append(toIndentedString(connectionStatus)).append("\n");
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

