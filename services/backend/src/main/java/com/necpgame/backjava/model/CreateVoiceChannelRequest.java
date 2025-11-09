package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ProximitySettings;
import com.necpgame.backjava.model.VoiceChannelOwner;
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
 * CreateVoiceChannelRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class CreateVoiceChannelRequest {

  private String channelName;

  /**
   * Gets or Sets channelType
   */
  public enum ChannelTypeEnum {
    PARTY("party"),
    
    GUILD("guild"),
    
    RAID("raid"),
    
    PROXIMITY("proximity"),
    
    CUSTOM("custom");

    private final String value;

    ChannelTypeEnum(String value) {
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
    public static ChannelTypeEnum fromValue(String value) {
      for (ChannelTypeEnum b : ChannelTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ChannelTypeEnum channelType;

  private VoiceChannelOwner owner;

  /**
   * Gets or Sets qualityPreset
   */
  public enum QualityPresetEnum {
    LOW("low"),
    
    MEDIUM("medium"),
    
    HIGH("high"),
    
    ULTRA("ultra");

    private final String value;

    QualityPresetEnum(String value) {
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
    public static QualityPresetEnum fromValue(String value) {
      for (QualityPresetEnum b : QualityPresetEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private QualityPresetEnum qualityPreset;

  private Integer maxParticipants;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  private @Nullable ProximitySettings proximity;

  private @Nullable Integer autoCloseMinutes;

  public CreateVoiceChannelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateVoiceChannelRequest(String channelName, ChannelTypeEnum channelType, VoiceChannelOwner owner, QualityPresetEnum qualityPreset, Integer maxParticipants) {
    this.channelName = channelName;
    this.channelType = channelType;
    this.owner = owner;
    this.qualityPreset = qualityPreset;
    this.maxParticipants = maxParticipants;
  }

  public CreateVoiceChannelRequest channelName(String channelName) {
    this.channelName = channelName;
    return this;
  }

  /**
   * Get channelName
   * @return channelName
   */
  @NotNull 
  @Schema(name = "channelName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelName")
  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public CreateVoiceChannelRequest channelType(ChannelTypeEnum channelType) {
    this.channelType = channelType;
    return this;
  }

  /**
   * Get channelType
   * @return channelType
   */
  @NotNull 
  @Schema(name = "channelType", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelType")
  public ChannelTypeEnum getChannelType() {
    return channelType;
  }

  public void setChannelType(ChannelTypeEnum channelType) {
    this.channelType = channelType;
  }

  public CreateVoiceChannelRequest owner(VoiceChannelOwner owner) {
    this.owner = owner;
    return this;
  }

  /**
   * Get owner
   * @return owner
   */
  @NotNull @Valid 
  @Schema(name = "owner", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("owner")
  public VoiceChannelOwner getOwner() {
    return owner;
  }

  public void setOwner(VoiceChannelOwner owner) {
    this.owner = owner;
  }

  public CreateVoiceChannelRequest qualityPreset(QualityPresetEnum qualityPreset) {
    this.qualityPreset = qualityPreset;
    return this;
  }

  /**
   * Get qualityPreset
   * @return qualityPreset
   */
  @NotNull 
  @Schema(name = "qualityPreset", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("qualityPreset")
  public QualityPresetEnum getQualityPreset() {
    return qualityPreset;
  }

  public void setQualityPreset(QualityPresetEnum qualityPreset) {
    this.qualityPreset = qualityPreset;
  }

  public CreateVoiceChannelRequest maxParticipants(Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * minimum: 2
   * maximum: 128
   * @return maxParticipants
   */
  @NotNull @Min(value = 2) @Max(value = 128) 
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("maxParticipants")
  public Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public CreateVoiceChannelRequest allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public CreateVoiceChannelRequest addAllowedRolesItem(String allowedRolesItem) {
    if (this.allowedRoles == null) {
      this.allowedRoles = new ArrayList<>();
    }
    this.allowedRoles.add(allowedRolesItem);
    return this;
  }

  /**
   * Get allowedRoles
   * @return allowedRoles
   */
  
  @Schema(name = "allowedRoles", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("allowedRoles")
  public List<String> getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public CreateVoiceChannelRequest proximity(@Nullable ProximitySettings proximity) {
    this.proximity = proximity;
    return this;
  }

  /**
   * Get proximity
   * @return proximity
   */
  @Valid 
  @Schema(name = "proximity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("proximity")
  public @Nullable ProximitySettings getProximity() {
    return proximity;
  }

  public void setProximity(@Nullable ProximitySettings proximity) {
    this.proximity = proximity;
  }

  public CreateVoiceChannelRequest autoCloseMinutes(@Nullable Integer autoCloseMinutes) {
    this.autoCloseMinutes = autoCloseMinutes;
    return this;
  }

  /**
   * Get autoCloseMinutes
   * minimum: 5
   * maximum: 240
   * @return autoCloseMinutes
   */
  @Min(value = 5) @Max(value = 240) 
  @Schema(name = "autoCloseMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoCloseMinutes")
  public @Nullable Integer getAutoCloseMinutes() {
    return autoCloseMinutes;
  }

  public void setAutoCloseMinutes(@Nullable Integer autoCloseMinutes) {
    this.autoCloseMinutes = autoCloseMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateVoiceChannelRequest createVoiceChannelRequest = (CreateVoiceChannelRequest) o;
    return Objects.equals(this.channelName, createVoiceChannelRequest.channelName) &&
        Objects.equals(this.channelType, createVoiceChannelRequest.channelType) &&
        Objects.equals(this.owner, createVoiceChannelRequest.owner) &&
        Objects.equals(this.qualityPreset, createVoiceChannelRequest.qualityPreset) &&
        Objects.equals(this.maxParticipants, createVoiceChannelRequest.maxParticipants) &&
        Objects.equals(this.allowedRoles, createVoiceChannelRequest.allowedRoles) &&
        Objects.equals(this.proximity, createVoiceChannelRequest.proximity) &&
        Objects.equals(this.autoCloseMinutes, createVoiceChannelRequest.autoCloseMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelName, channelType, owner, qualityPreset, maxParticipants, allowedRoles, proximity, autoCloseMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateVoiceChannelRequest {\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    qualityPreset: ").append(toIndentedString(qualityPreset)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    proximity: ").append(toIndentedString(proximity)).append("\n");
    sb.append("    autoCloseMinutes: ").append(toIndentedString(autoCloseMinutes)).append("\n");
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

