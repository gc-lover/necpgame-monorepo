package com.necpgame.backjava.model;

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
 * VoiceParticipant
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class VoiceParticipant {

  private String playerId;

  private @Nullable String displayName;

  private @Nullable String role;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    CONNECTING("connecting"),
    
    CONNECTED("connected"),
    
    RECONNECTING("reconnecting"),
    
    DISCONNECTED("disconnected");

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

  private Boolean isMuted;

  private Boolean isDeafened;

  private @Nullable Boolean isSpeaking;

  /**
   * Gets or Sets audioQuality
   */
  public enum AudioQualityEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high");

    private final String value;

    AudioQualityEnum(String value) {
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
    public static AudioQualityEnum fromValue(String value) {
      for (AudioQualityEnum b : AudioQualityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable AudioQualityEnum audioQuality;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime joinedAt;

  private @Nullable String connectionId;

  public VoiceParticipant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceParticipant(String playerId, StatusEnum status, Boolean isMuted, Boolean isDeafened, OffsetDateTime joinedAt) {
    this.playerId = playerId;
    this.status = status;
    this.isMuted = isMuted;
    this.isDeafened = isDeafened;
    this.joinedAt = joinedAt;
  }

  public VoiceParticipant playerId(String playerId) {
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

  public VoiceParticipant displayName(@Nullable String displayName) {
    this.displayName = displayName;
    return this;
  }

  /**
   * Get displayName
   * @return displayName
   */
  
  @Schema(name = "displayName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("displayName")
  public @Nullable String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(@Nullable String displayName) {
    this.displayName = displayName;
  }

  public VoiceParticipant role(@Nullable String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  
  @Schema(name = "role", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("role")
  public @Nullable String getRole() {
    return role;
  }

  public void setRole(@Nullable String role) {
    this.role = role;
  }

  public VoiceParticipant status(StatusEnum status) {
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

  public VoiceParticipant isMuted(Boolean isMuted) {
    this.isMuted = isMuted;
    return this;
  }

  /**
   * Get isMuted
   * @return isMuted
   */
  @NotNull 
  @Schema(name = "isMuted", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isMuted")
  public Boolean getIsMuted() {
    return isMuted;
  }

  public void setIsMuted(Boolean isMuted) {
    this.isMuted = isMuted;
  }

  public VoiceParticipant isDeafened(Boolean isDeafened) {
    this.isDeafened = isDeafened;
    return this;
  }

  /**
   * Get isDeafened
   * @return isDeafened
   */
  @NotNull 
  @Schema(name = "isDeafened", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("isDeafened")
  public Boolean getIsDeafened() {
    return isDeafened;
  }

  public void setIsDeafened(Boolean isDeafened) {
    this.isDeafened = isDeafened;
  }

  public VoiceParticipant isSpeaking(@Nullable Boolean isSpeaking) {
    this.isSpeaking = isSpeaking;
    return this;
  }

  /**
   * Get isSpeaking
   * @return isSpeaking
   */
  
  @Schema(name = "isSpeaking", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isSpeaking")
  public @Nullable Boolean getIsSpeaking() {
    return isSpeaking;
  }

  public void setIsSpeaking(@Nullable Boolean isSpeaking) {
    this.isSpeaking = isSpeaking;
  }

  public VoiceParticipant audioQuality(@Nullable AudioQualityEnum audioQuality) {
    this.audioQuality = audioQuality;
    return this;
  }

  /**
   * Get audioQuality
   * @return audioQuality
   */
  
  @Schema(name = "audioQuality", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audioQuality")
  public @Nullable AudioQualityEnum getAudioQuality() {
    return audioQuality;
  }

  public void setAudioQuality(@Nullable AudioQualityEnum audioQuality) {
    this.audioQuality = audioQuality;
  }

  public VoiceParticipant joinedAt(OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
    return this;
  }

  /**
   * Get joinedAt
   * @return joinedAt
   */
  @NotNull @Valid 
  @Schema(name = "joinedAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("joinedAt")
  public OffsetDateTime getJoinedAt() {
    return joinedAt;
  }

  public void setJoinedAt(OffsetDateTime joinedAt) {
    this.joinedAt = joinedAt;
  }

  public VoiceParticipant connectionId(@Nullable String connectionId) {
    this.connectionId = connectionId;
    return this;
  }

  /**
   * Get connectionId
   * @return connectionId
   */
  
  @Schema(name = "connectionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("connectionId")
  public @Nullable String getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(@Nullable String connectionId) {
    this.connectionId = connectionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceParticipant voiceParticipant = (VoiceParticipant) o;
    return Objects.equals(this.playerId, voiceParticipant.playerId) &&
        Objects.equals(this.displayName, voiceParticipant.displayName) &&
        Objects.equals(this.role, voiceParticipant.role) &&
        Objects.equals(this.status, voiceParticipant.status) &&
        Objects.equals(this.isMuted, voiceParticipant.isMuted) &&
        Objects.equals(this.isDeafened, voiceParticipant.isDeafened) &&
        Objects.equals(this.isSpeaking, voiceParticipant.isSpeaking) &&
        Objects.equals(this.audioQuality, voiceParticipant.audioQuality) &&
        Objects.equals(this.joinedAt, voiceParticipant.joinedAt) &&
        Objects.equals(this.connectionId, voiceParticipant.connectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, displayName, role, status, isMuted, isDeafened, isSpeaking, audioQuality, joinedAt, connectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceParticipant {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    displayName: ").append(toIndentedString(displayName)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    isMuted: ").append(toIndentedString(isMuted)).append("\n");
    sb.append("    isDeafened: ").append(toIndentedString(isDeafened)).append("\n");
    sb.append("    isSpeaking: ").append(toIndentedString(isSpeaking)).append("\n");
    sb.append("    audioQuality: ").append(toIndentedString(audioQuality)).append("\n");
    sb.append("    joinedAt: ").append(toIndentedString(joinedAt)).append("\n");
    sb.append("    connectionId: ").append(toIndentedString(connectionId)).append("\n");
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

