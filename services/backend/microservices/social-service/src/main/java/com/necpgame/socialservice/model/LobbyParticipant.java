package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * LobbyParticipant
 */


public class LobbyParticipant {

  private String playerId;

  private String nickname;

  private String role;

  /**
   * Gets or Sets micStatus
   */
  public enum MicStatusEnum {
    PUSH_TO_TALK("push_to_talk"),
    
    VOICE_ACTIVATION("voice_activation"),
    
    MUTED("muted");

    private final String value;

    MicStatusEnum(String value) {
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
    public static MicStatusEnum fromValue(String value) {
      for (MicStatusEnum b : MicStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private MicStatusEnum micStatus;

  /**
   * Gets or Sets readyStatus
   */
  public enum ReadyStatusEnum {
    READY("ready"),
    
    PENDING("pending"),
    
    NOT_READY("not_ready");

    private final String value;

    ReadyStatusEnum(String value) {
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
    public static ReadyStatusEnum fromValue(String value) {
      for (ReadyStatusEnum b : ReadyStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ReadyStatusEnum readyStatus;

  /**
   * Gets or Sets leadership
   */
  public enum LeadershipEnum {
    OWNER("owner"),
    
    MODERATOR("moderator"),
    
    MEMBER("member");

    private final String value;

    LeadershipEnum(String value) {
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
    public static LeadershipEnum fromValue(String value) {
      for (LeadershipEnum b : LeadershipEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LeadershipEnum leadership;

  private @Nullable Boolean partyMember;

  private @Nullable String guildRank;

  public LobbyParticipant() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LobbyParticipant(String playerId, String nickname, String role, MicStatusEnum micStatus, ReadyStatusEnum readyStatus) {
    this.playerId = playerId;
    this.nickname = nickname;
    this.role = role;
    this.micStatus = micStatus;
    this.readyStatus = readyStatus;
  }

  public LobbyParticipant playerId(String playerId) {
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

  public LobbyParticipant nickname(String nickname) {
    this.nickname = nickname;
    return this;
  }

  /**
   * Get nickname
   * @return nickname
   */
  @NotNull 
  @Schema(name = "nickname", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("nickname")
  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public LobbyParticipant role(String role) {
    this.role = role;
    return this;
  }

  /**
   * Get role
   * @return role
   */
  @NotNull 
  @Schema(name = "role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("role")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public LobbyParticipant micStatus(MicStatusEnum micStatus) {
    this.micStatus = micStatus;
    return this;
  }

  /**
   * Get micStatus
   * @return micStatus
   */
  @NotNull 
  @Schema(name = "micStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("micStatus")
  public MicStatusEnum getMicStatus() {
    return micStatus;
  }

  public void setMicStatus(MicStatusEnum micStatus) {
    this.micStatus = micStatus;
  }

  public LobbyParticipant readyStatus(ReadyStatusEnum readyStatus) {
    this.readyStatus = readyStatus;
    return this;
  }

  /**
   * Get readyStatus
   * @return readyStatus
   */
  @NotNull 
  @Schema(name = "readyStatus", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("readyStatus")
  public ReadyStatusEnum getReadyStatus() {
    return readyStatus;
  }

  public void setReadyStatus(ReadyStatusEnum readyStatus) {
    this.readyStatus = readyStatus;
  }

  public LobbyParticipant leadership(@Nullable LeadershipEnum leadership) {
    this.leadership = leadership;
    return this;
  }

  /**
   * Get leadership
   * @return leadership
   */
  
  @Schema(name = "leadership", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("leadership")
  public @Nullable LeadershipEnum getLeadership() {
    return leadership;
  }

  public void setLeadership(@Nullable LeadershipEnum leadership) {
    this.leadership = leadership;
  }

  public LobbyParticipant partyMember(@Nullable Boolean partyMember) {
    this.partyMember = partyMember;
    return this;
  }

  /**
   * Get partyMember
   * @return partyMember
   */
  
  @Schema(name = "partyMember", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyMember")
  public @Nullable Boolean getPartyMember() {
    return partyMember;
  }

  public void setPartyMember(@Nullable Boolean partyMember) {
    this.partyMember = partyMember;
  }

  public LobbyParticipant guildRank(@Nullable String guildRank) {
    this.guildRank = guildRank;
    return this;
  }

  /**
   * Get guildRank
   * @return guildRank
   */
  
  @Schema(name = "guildRank", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildRank")
  public @Nullable String getGuildRank() {
    return guildRank;
  }

  public void setGuildRank(@Nullable String guildRank) {
    this.guildRank = guildRank;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LobbyParticipant lobbyParticipant = (LobbyParticipant) o;
    return Objects.equals(this.playerId, lobbyParticipant.playerId) &&
        Objects.equals(this.nickname, lobbyParticipant.nickname) &&
        Objects.equals(this.role, lobbyParticipant.role) &&
        Objects.equals(this.micStatus, lobbyParticipant.micStatus) &&
        Objects.equals(this.readyStatus, lobbyParticipant.readyStatus) &&
        Objects.equals(this.leadership, lobbyParticipant.leadership) &&
        Objects.equals(this.partyMember, lobbyParticipant.partyMember) &&
        Objects.equals(this.guildRank, lobbyParticipant.guildRank);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId, nickname, role, micStatus, readyStatus, leadership, partyMember, guildRank);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LobbyParticipant {\n");
    sb.append("    playerId: ").append(toIndentedString(playerId)).append("\n");
    sb.append("    nickname: ").append(toIndentedString(nickname)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    micStatus: ").append(toIndentedString(micStatus)).append("\n");
    sb.append("    readyStatus: ").append(toIndentedString(readyStatus)).append("\n");
    sb.append("    leadership: ").append(toIndentedString(leadership)).append("\n");
    sb.append("    partyMember: ").append(toIndentedString(partyMember)).append("\n");
    sb.append("    guildRank: ").append(toIndentedString(guildRank)).append("\n");
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

