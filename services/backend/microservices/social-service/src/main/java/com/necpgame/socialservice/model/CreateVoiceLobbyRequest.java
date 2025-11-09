package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.RoleRequirement;
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
 * CreateVoiceLobbyRequest
 */


public class CreateVoiceLobbyRequest {

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

  private String language;

  private Integer maxParticipants;

  @Valid
  private List<@Valid RoleRequirement> requiredRoles = new ArrayList<>();

  private @Nullable Boolean autoFillFromParty;

  private @Nullable VoiceSettings voiceSettings;

  public CreateVoiceLobbyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateVoiceLobbyRequest(String name, LobbyTypeEnum lobbyType, String activityCode, String region, String language, Integer maxParticipants, List<@Valid RoleRequirement> requiredRoles) {
    this.name = name;
    this.lobbyType = lobbyType;
    this.activityCode = activityCode;
    this.region = region;
    this.language = language;
    this.maxParticipants = maxParticipants;
    this.requiredRoles = requiredRoles;
  }

  public CreateVoiceLobbyRequest name(String name) {
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

  public CreateVoiceLobbyRequest lobbyType(LobbyTypeEnum lobbyType) {
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

  public CreateVoiceLobbyRequest activityCode(String activityCode) {
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

  public CreateVoiceLobbyRequest region(String region) {
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

  public CreateVoiceLobbyRequest language(String language) {
    this.language = language;
    return this;
  }

  /**
   * Get language
   * @return language
   */
  @NotNull 
  @Schema(name = "language", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("language")
  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public CreateVoiceLobbyRequest maxParticipants(Integer maxParticipants) {
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

  public CreateVoiceLobbyRequest requiredRoles(List<@Valid RoleRequirement> requiredRoles) {
    this.requiredRoles = requiredRoles;
    return this;
  }

  public CreateVoiceLobbyRequest addRequiredRolesItem(RoleRequirement requiredRolesItem) {
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

  public CreateVoiceLobbyRequest autoFillFromParty(@Nullable Boolean autoFillFromParty) {
    this.autoFillFromParty = autoFillFromParty;
    return this;
  }

  /**
   * Get autoFillFromParty
   * @return autoFillFromParty
   */
  
  @Schema(name = "autoFillFromParty", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoFillFromParty")
  public @Nullable Boolean getAutoFillFromParty() {
    return autoFillFromParty;
  }

  public void setAutoFillFromParty(@Nullable Boolean autoFillFromParty) {
    this.autoFillFromParty = autoFillFromParty;
  }

  public CreateVoiceLobbyRequest voiceSettings(@Nullable VoiceSettings voiceSettings) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateVoiceLobbyRequest createVoiceLobbyRequest = (CreateVoiceLobbyRequest) o;
    return Objects.equals(this.name, createVoiceLobbyRequest.name) &&
        Objects.equals(this.lobbyType, createVoiceLobbyRequest.lobbyType) &&
        Objects.equals(this.activityCode, createVoiceLobbyRequest.activityCode) &&
        Objects.equals(this.region, createVoiceLobbyRequest.region) &&
        Objects.equals(this.language, createVoiceLobbyRequest.language) &&
        Objects.equals(this.maxParticipants, createVoiceLobbyRequest.maxParticipants) &&
        Objects.equals(this.requiredRoles, createVoiceLobbyRequest.requiredRoles) &&
        Objects.equals(this.autoFillFromParty, createVoiceLobbyRequest.autoFillFromParty) &&
        Objects.equals(this.voiceSettings, createVoiceLobbyRequest.voiceSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, lobbyType, activityCode, region, language, maxParticipants, requiredRoles, autoFillFromParty, voiceSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateVoiceLobbyRequest {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lobbyType: ").append(toIndentedString(lobbyType)).append("\n");
    sb.append("    activityCode: ").append(toIndentedString(activityCode)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    language: ").append(toIndentedString(language)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    requiredRoles: ").append(toIndentedString(requiredRoles)).append("\n");
    sb.append("    autoFillFromParty: ").append(toIndentedString(autoFillFromParty)).append("\n");
    sb.append("    voiceSettings: ").append(toIndentedString(voiceSettings)).append("\n");
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

