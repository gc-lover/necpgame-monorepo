package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RoleRequirement;
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
 * VoiceLobbySummary
 */


public class VoiceLobbySummary {

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

  public VoiceLobbySummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceLobbySummary(String lobbyId, String name, LobbyTypeEnum lobbyType, String activityCode, String region, List<@Valid RoleRequirement> requiredRoles, Integer currentParticipants, Integer maxParticipants, StatusEnum status) {
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

  public VoiceLobbySummary lobbyId(String lobbyId) {
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

  public VoiceLobbySummary name(String name) {
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

  public VoiceLobbySummary lobbyType(LobbyTypeEnum lobbyType) {
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

  public VoiceLobbySummary activityCode(String activityCode) {
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

  public VoiceLobbySummary region(String region) {
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

  public VoiceLobbySummary language(@Nullable String language) {
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

  public VoiceLobbySummary requiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
    return this;
  }

  public VoiceLobbySummary addRequiredRolesItem(RoleRequirement requiredRolesItem) {
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

  public VoiceLobbySummary currentParticipants(Integer currentParticipants) {
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

  public VoiceLobbySummary maxParticipants(Integer maxParticipants) {
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

  public VoiceLobbySummary status(StatusEnum status) {
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

  public VoiceLobbySummary voiceQualityTier(@Nullable VoiceQualityTierEnum voiceQualityTier) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceLobbySummary voiceLobbySummary = (VoiceLobbySummary) o;
    return Objects.equals(this.lobbyId, voiceLobbySummary.lobbyId) &&
        Objects.equals(this.name, voiceLobbySummary.name) &&
        Objects.equals(this.lobbyType, voiceLobbySummary.lobbyType) &&
        Objects.equals(this.activityCode, voiceLobbySummary.activityCode) &&
        Objects.equals(this.region, voiceLobbySummary.region) &&
        Objects.equals(this.language, voiceLobbySummary.language) &&
        Objects.equals(this.requiredRoles, voiceLobbySummary.requiredRoles) &&
        Objects.equals(this.currentParticipants, voiceLobbySummary.currentParticipants) &&
        Objects.equals(this.maxParticipants, voiceLobbySummary.maxParticipants) &&
        Objects.equals(this.status, voiceLobbySummary.status) &&
        Objects.equals(this.voiceQualityTier, voiceLobbySummary.voiceQualityTier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lobbyId, name, lobbyType, activityCode, region, language, requiredRoles, currentParticipants, maxParticipants, status, voiceQualityTier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceLobbySummary {\n");
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

