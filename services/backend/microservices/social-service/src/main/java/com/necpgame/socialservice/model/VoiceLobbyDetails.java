package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.LobbyParticipant;
import com.necpgame.socialservice.model.RoleRequirement;
import com.necpgame.socialservice.model.Subchannel;
import com.necpgame.socialservice.model.VoiceLobbyEvent;
import com.necpgame.socialservice.model.VoiceSettings;
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
 * VoiceLobbyDetails
 */


public class VoiceLobbyDetails {

  private String lobbyId;

  private String name;

  /**
   * Gets or Sets lobbyType
   */
  public enum LobbyTypeEnum {
    RAID("raid"),
    
    TOURNAMENT("tournament"),
    
    GUILD("guild"),
    
    SOCIAL("social");

    private final String value;

    LobbyTypeEnum(String value) {
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
    public static LobbyTypeEnum fromValue(String value) {
      for (LobbyTypeEnum b : LobbyTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LobbyTypeEnum lobbyType;

  private String activityCode;

  private String region;

  private @Nullable String language;

  @Valid
  private List<@Valid RoleRequirement> requiredRoles = new ArrayList<>();

  private Integer currentParticipants;

  private Integer maxParticipants;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    OPEN("open"),
    
    PREPARING("preparing"),
    
    IN_PROGRESS("in_progress"),
    
    CLOSED("closed");

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

  /**
   * Gets or Sets voiceQualityTier
   */
  public enum VoiceQualityTierEnum {
    HIGH("high"),
    
    STANDARD("standard"),
    
    LOW("low");

    private final String value;

    VoiceQualityTierEnum(String value) {
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
    public static VoiceQualityTierEnum fromValue(String value) {
      for (VoiceQualityTierEnum b : VoiceQualityTierEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VoiceQualityTierEnum voiceQualityTier;

  private @Nullable String description;

  private @Nullable String partyId;

  private @Nullable String guildId;

  private @Nullable String voiceChannelId;

  private @Nullable VoiceSettings voiceSettings;

  /**
   * Gets or Sets readyCheckState
   */
  public enum ReadyCheckStateEnum {
    IDLE("idle"),
    
    RUNNING("running"),
    
    COMPLETED("completed");

    private final String value;

    ReadyCheckStateEnum(String value) {
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
    public static ReadyCheckStateEnum fromValue(String value) {
      for (ReadyCheckStateEnum b : ReadyCheckStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ReadyCheckStateEnum readyCheckState;

  @Valid
  private List<@Valid LobbyParticipant> participants = new ArrayList<>();

  @Valid
  private List<@Valid Subchannel> subchannels = new ArrayList<>();

  @Valid
  private List<@Valid VoiceLobbyEvent> events = new ArrayList<>();

  public VoiceLobbyDetails() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceLobbyDetails(String lobbyId, String name, LobbyTypeEnum lobbyType, String activityCode, String region, List<@Valid RoleRequirement> requiredRoles, Integer currentParticipants, Integer maxParticipants, StatusEnum status) {
    this.lobbyId = lobbyId;
    this.name = name;
    this.lobbyType = lobbyType;
    this.activityCode = activityCode;
    this.region = region;
    this.requiredRoles = requiredRoles;
    this.currentParticipants = currentParticipants;
    this.maxParticipants = maxParticipants;
    this.status = status;
  }

  public VoiceLobbyDetails lobbyId(String lobbyId) {
    this.lobbyId = lobbyId;
    return this;
  }

  /**
   * Get lobbyId
   * @return lobbyId
   */
  @NotNull 
  @Schema(name = "lobbyId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lobbyId")
  public String getLobbyId() {
    return lobbyId;
  }

  public void setLobbyId(String lobbyId) {
    this.lobbyId = lobbyId;
  }

  public VoiceLobbyDetails name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  @NotNull 
  @Schema(name = "name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public VoiceLobbyDetails lobbyType(LobbyTypeEnum lobbyType) {
    this.lobbyType = lobbyType;
    return this;
  }

  /**
   * Get lobbyType
   * @return lobbyType
   */
  @NotNull 
  @Schema(name = "lobbyType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lobbyType")
  public LobbyTypeEnum getLobbyType() {
    return lobbyType;
  }

  public void setLobbyType(LobbyTypeEnum lobbyType) {
    this.lobbyType = lobbyType;
  }

  public VoiceLobbyDetails activityCode(String activityCode) {
    this.activityCode = activityCode;
    return this;
  }

  /**
   * Get activityCode
   * @return activityCode
   */
  @NotNull 
  @Schema(name = "activityCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("activityCode")
  public String getActivityCode() {
    return activityCode;
  }

  public void setActivityCode(String activityCode) {
    this.activityCode = activityCode;
  }

  public VoiceLobbyDetails region(String region) {
    this.region = region;
    return this;
  }

  /**
   * Get region
   * @return region
   */
  @NotNull 
  @Schema(name = "region", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("region")
  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public VoiceLobbyDetails language(@Nullable String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  
  @Schema(name = "language", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("language")
  public @Nullable String getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable String language) {
    this.language = language;
  }

  public VoiceLobbyDetails requiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
    return this;
  }

  public VoiceLobbyDetails addRequiredRolesItem(RoleRequirement requiredRolesItem) {
    if (this.requiredRoles == null) {
      this.requiredRoles = new ArrayList<>();
    }
    this.requiredRoles.add(requiredRolesItem);
    return this;
  }

  /**
   * Get requiredRoles
   * @return requiredRoles
   */
  @NotNull @Valid 
  @Schema(name = "requiredRoles", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("requiredRoles")
  public List<@Valid RoleRequirement> getRequiredRoles() {
    return requiredRoles;
  }

  public void setRequiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
  }

  public VoiceLobbyDetails currentParticipants(Integer currentParticipants) {
    this.currentParticipants = currentParticipants;
    return this;
  }

  /**
   * Get currentParticipants
   * @return currentParticipants
   */
  @NotNull 
  @Schema(name = "currentParticipants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("currentParticipants")
  public Integer getCurrentParticipants() {
    return currentParticipants;
  }

  public void setCurrentParticipants(Integer currentParticipants) {
    this.currentParticipants = currentParticipants;
  }

  public VoiceLobbyDetails maxParticipants(Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * @return maxParticipants
   */
  @NotNull 
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxParticipants")
  public Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public VoiceLobbyDetails status(StatusEnum status) {
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

  public VoiceLobbyDetails voiceQualityTier(@Nullable VoiceQualityTierEnum voiceQualityTier) {
    this.voiceQualityTier = voiceQualityTier;
    return this;
  }

  /**
   * Get voiceQualityTier
   * @return voiceQualityTier
   */
  
  @Schema(name = "voiceQualityTier", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceQualityTier")
  public @Nullable VoiceQualityTierEnum getVoiceQualityTier() {
    return voiceQualityTier;
  }

  public void setVoiceQualityTier(@Nullable VoiceQualityTierEnum voiceQualityTier) {
    this.voiceQualityTier = voiceQualityTier;
  }

  public VoiceLobbyDetails description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public VoiceLobbyDetails partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public VoiceLobbyDetails guildId(@Nullable String guildId) {
    this.guildId = guildId;
    return this;
  }

  /**
   * Get guildId
   * @return guildId
   */
  
  @Schema(name = "guildId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guildId")
  public @Nullable String getGuildId() {
    return guildId;
  }

  public void setGuildId(@Nullable String guildId) {
    this.guildId = guildId;
  }

  public VoiceLobbyDetails voiceChannelId(@Nullable String voiceChannelId) {
    this.voiceChannelId = voiceChannelId;
    return this;
  }

  /**
   * Get voiceChannelId
   * @return voiceChannelId
   */
  
  @Schema(name = "voiceChannelId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceChannelId")
  public @Nullable String getVoiceChannelId() {
    return voiceChannelId;
  }

  public void setVoiceChannelId(@Nullable String voiceChannelId) {
    this.voiceChannelId = voiceChannelId;
  }

  public VoiceLobbyDetails voiceSettings(@Nullable VoiceSettings voiceSettings) {
    this.voiceSettings = voiceSettings;
    return this;
  }

  /**
   * Get voiceSettings
   * @return voiceSettings
   */
  @Valid 
  @Schema(name = "voiceSettings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("voiceSettings")
  public @Nullable VoiceSettings getVoiceSettings() {
    return voiceSettings;
  }

  public void setVoiceSettings(@Nullable VoiceSettings voiceSettings) {
    this.voiceSettings = voiceSettings;
  }

  public VoiceLobbyDetails readyCheckState(@Nullable ReadyCheckStateEnum readyCheckState) {
    this.readyCheckState = readyCheckState;
    return this;
  }

  /**
   * Get readyCheckState
   * @return readyCheckState
   */
  
  @Schema(name = "readyCheckState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyCheckState")
  public @Nullable ReadyCheckStateEnum getReadyCheckState() {
    return readyCheckState;
  }

  public void setReadyCheckState(@Nullable ReadyCheckStateEnum readyCheckState) {
    this.readyCheckState = readyCheckState;
  }

  public VoiceLobbyDetails participants(List<@Valid LobbyParticipant> participants) {
    this.participants = participants;
    return this;
  }

  public VoiceLobbyDetails addParticipantsItem(LobbyParticipant participantsItem) {
    if (this.participants == null) {
      this.participants = new ArrayList<>();
    }
    this.participants.add(participantsItem);
    return this;
  }

  /**
   * Get participants
   * @return participants
   */
  @Valid 
  @Schema(name = "participants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participants")
  public List<@Valid LobbyParticipant> getParticipants() {
    return participants;
  }

  public void setParticipants(List<@Valid LobbyParticipant> participants) {
    this.participants = participants;
  }

  public VoiceLobbyDetails subchannels(List<@Valid Subchannel> subchannels) {
    this.subchannels = subchannels;
    return this;
  }

  public VoiceLobbyDetails addSubchannelsItem(Subchannel subchannelsItem) {
    if (this.subchannels == null) {
      this.subchannels = new ArrayList<>();
    }
    this.subchannels.add(subchannelsItem);
    return this;
  }

  /**
   * Get subchannels
   * @return subchannels
   */
  @Valid 
  @Schema(name = "subchannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subchannels")
  public List<@Valid Subchannel> getSubchannels() {
    return subchannels;
  }

  public void setSubchannels(List<@Valid Subchannel> subchannels) {
    this.subchannels = subchannels;
  }

  public VoiceLobbyDetails events(List<@Valid VoiceLobbyEvent> events) {
    this.events = events;
    return this;
  }

  public VoiceLobbyDetails addEventsItem(VoiceLobbyEvent eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @Valid 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("events")
  public List<@Valid VoiceLobbyEvent> getEvents() {
    return events;
  }

  public void setEvents(List<@Valid VoiceLobbyEvent> events) {
    this.events = events;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceLobbyDetails voiceLobbyDetails = (VoiceLobbyDetails) o;
    return Objects.equals(this.lobbyId, voiceLobbyDetails.lobbyId) &&
        Objects.equals(this.name, voiceLobbyDetails.name) &&
        Objects.equals(this.lobbyType, voiceLobbyDetails.lobbyType) &&
        Objects.equals(this.activityCode, voiceLobbyDetails.activityCode) &&
        Objects.equals(this.region, voiceLobbyDetails.region) &&
        Objects.equals(this.language, voiceLobbyDetails.language) &&
        Objects.equals(this.requiredRoles, voiceLobbyDetails.requiredRoles) &&
        Objects.equals(this.currentParticipants, voiceLobbyDetails.currentParticipants) &&
        Objects.equals(this.maxParticipants, voiceLobbyDetails.maxParticipants) &&
        Objects.equals(this.status, voiceLobbyDetails.status) &&
        Objects.equals(this.voiceQualityTier, voiceLobbyDetails.voiceQualityTier) &&
        Objects.equals(this.description, voiceLobbyDetails.description) &&
        Objects.equals(this.partyId, voiceLobbyDetails.partyId) &&
        Objects.equals(this.guildId, voiceLobbyDetails.guildId) &&
        Objects.equals(this.voiceChannelId, voiceLobbyDetails.voiceChannelId) &&
        Objects.equals(this.voiceSettings, voiceLobbyDetails.voiceSettings) &&
        Objects.equals(this.readyCheckState, voiceLobbyDetails.readyCheckState) &&
        Objects.equals(this.participants, voiceLobbyDetails.participants) &&
        Objects.equals(this.subchannels, voiceLobbyDetails.subchannels) &&
        Objects.equals(this.events, voiceLobbyDetails.events);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lobbyId, name, lobbyType, activityCode, region, language, requiredRoles, currentParticipants, maxParticipants, status, voiceQualityTier, description, partyId, guildId, voiceChannelId, voiceSettings, readyCheckState, participants, subchannels, events);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceLobbyDetails {\n");
    sb.append("    lobbyId: ").append(toIndentedString(lobbyId)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lobbyType: ").append(toIndentedString(lobbyType)).append("\n");
    sb.append("    activityCode: ").append(toIndentedString(activityCode)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    requiredRoles: ").append(toIndentedString(requiredRoles)).append("\n");
    sb.append("    currentParticipants: ").append(toIndentedString(currentParticipants)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    voiceQualityTier: ").append(toIndentedString(voiceQualityTier)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    guildId: ").append(toIndentedString(guildId)).append("\n");
    sb.append("    voiceChannelId: ").append(toIndentedString(voiceChannelId)).append("\n");
    sb.append("    voiceSettings: ").append(toIndentedString(voiceSettings)).append("\n");
    sb.append("    readyCheckState: ").append(toIndentedString(readyCheckState)).append("\n");
    sb.append("    participants: ").append(toIndentedString(participants)).append("\n");
    sb.append("    subchannels: ").append(toIndentedString(subchannels)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

