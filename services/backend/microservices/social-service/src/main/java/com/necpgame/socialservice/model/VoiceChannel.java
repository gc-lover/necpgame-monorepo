package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ProximitySettings;
import com.necpgame.socialservice.model.VoiceChannelOwner;
import com.necpgame.socialservice.model.VoiceChannelPermissions;
import com.necpgame.socialservice.model.VoiceMetrics;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * VoiceChannel
 */


public class VoiceChannel {

  private String channelId;

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

  private @Nullable Integer maxParticipants;

  private @Nullable Integer activeParticipants;

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

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("active"),
    
    SCHEDULED("scheduled"),
    
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

  private @Nullable ProximitySettings proximity;

  private @Nullable String description;

  private @Nullable VoiceChannelPermissions permissions;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  private @Nullable Integer maxBitrateKbps;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime createdAt;

  private @Nullable Integer autoCloseMinutes;

  private @Nullable VoiceMetrics analytics;

  public VoiceChannel() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceChannel(String channelId, String channelName, ChannelTypeEnum channelType, VoiceChannelOwner owner, QualityPresetEnum qualityPreset, StatusEnum status) {
    this.channelId = channelId;
    this.channelName = channelName;
    this.channelType = channelType;
    this.owner = owner;
    this.qualityPreset = qualityPreset;
    this.status = status;
  }

  public VoiceChannel channelId(String channelId) {
    this.channelId = channelId;
    return this;
  }

  /**
   * Get channelId
   * @return channelId
   */
  @NotNull 
  @Schema(name = "channelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelId")
  public String getChannelId() {
    return channelId;
  }

  public void setChannelId(String channelId) {
    this.channelId = channelId;
  }

  public VoiceChannel channelName(String channelName) {
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

  public VoiceChannel channelType(ChannelTypeEnum channelType) {
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

  public VoiceChannel owner(VoiceChannelOwner owner) {
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

  public VoiceChannel maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * minimum: 1
   * maximum: 128
   * @return maxParticipants
   */
  @Min(value = 1) @Max(value = 128) 
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public VoiceChannel activeParticipants(@Nullable Integer activeParticipants) {
    this.activeParticipants = activeParticipants;
    return this;
  }

  /**
   * Get activeParticipants
   * minimum: 0
   * @return activeParticipants
   */
  @Min(value = 0) 
  @Schema(name = "activeParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("activeParticipants")
  public @Nullable Integer getActiveParticipants() {
    return activeParticipants;
  }

  public void setActiveParticipants(@Nullable Integer activeParticipants) {
    this.activeParticipants = activeParticipants;
  }

  public VoiceChannel qualityPreset(QualityPresetEnum qualityPreset) {
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

  public VoiceChannel status(StatusEnum status) {
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

  public VoiceChannel proximity(@Nullable ProximitySettings proximity) {
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

  public VoiceChannel description(@Nullable String description) {
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

  public VoiceChannel permissions(@Nullable VoiceChannelPermissions permissions) {
    this.permissions = permissions;
    return this;
  }

  /**
   * Get permissions
   * @return permissions
   */
  @Valid 
  @Schema(name = "permissions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("permissions")
  public @Nullable VoiceChannelPermissions getPermissions() {
    return permissions;
  }

  public void setPermissions(@Nullable VoiceChannelPermissions permissions) {
    this.permissions = permissions;
  }

  public VoiceChannel allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public VoiceChannel addAllowedRolesItem(String allowedRolesItem) {
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

  public VoiceChannel maxBitrateKbps(@Nullable Integer maxBitrateKbps) {
    this.maxBitrateKbps = maxBitrateKbps;
    return this;
  }

  /**
   * Get maxBitrateKbps
   * @return maxBitrateKbps
   */
  
  @Schema(name = "maxBitrateKbps", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxBitrateKbps")
  public @Nullable Integer getMaxBitrateKbps() {
    return maxBitrateKbps;
  }

  public void setMaxBitrateKbps(@Nullable Integer maxBitrateKbps) {
    this.maxBitrateKbps = maxBitrateKbps;
  }

  public VoiceChannel createdAt(@Nullable OffsetDateTime createdAt) {
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

  public VoiceChannel autoCloseMinutes(@Nullable Integer autoCloseMinutes) {
    this.autoCloseMinutes = autoCloseMinutes;
    return this;
  }

  /**
   * Get autoCloseMinutes
   * @return autoCloseMinutes
   */
  
  @Schema(name = "autoCloseMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("autoCloseMinutes")
  public @Nullable Integer getAutoCloseMinutes() {
    return autoCloseMinutes;
  }

  public void setAutoCloseMinutes(@Nullable Integer autoCloseMinutes) {
    this.autoCloseMinutes = autoCloseMinutes;
  }

  public VoiceChannel analytics(@Nullable VoiceMetrics analytics) {
    this.analytics = analytics;
    return this;
  }

  /**
   * Get analytics
   * @return analytics
   */
  @Valid 
  @Schema(name = "analytics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("analytics")
  public @Nullable VoiceMetrics getAnalytics() {
    return analytics;
  }

  public void setAnalytics(@Nullable VoiceMetrics analytics) {
    this.analytics = analytics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceChannel voiceChannel = (VoiceChannel) o;
    return Objects.equals(this.channelId, voiceChannel.channelId) &&
        Objects.equals(this.channelName, voiceChannel.channelName) &&
        Objects.equals(this.channelType, voiceChannel.channelType) &&
        Objects.equals(this.owner, voiceChannel.owner) &&
        Objects.equals(this.maxParticipants, voiceChannel.maxParticipants) &&
        Objects.equals(this.activeParticipants, voiceChannel.activeParticipants) &&
        Objects.equals(this.qualityPreset, voiceChannel.qualityPreset) &&
        Objects.equals(this.status, voiceChannel.status) &&
        Objects.equals(this.proximity, voiceChannel.proximity) &&
        Objects.equals(this.description, voiceChannel.description) &&
        Objects.equals(this.permissions, voiceChannel.permissions) &&
        Objects.equals(this.allowedRoles, voiceChannel.allowedRoles) &&
        Objects.equals(this.maxBitrateKbps, voiceChannel.maxBitrateKbps) &&
        Objects.equals(this.createdAt, voiceChannel.createdAt) &&
        Objects.equals(this.autoCloseMinutes, voiceChannel.autoCloseMinutes) &&
        Objects.equals(this.analytics, voiceChannel.analytics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelName, channelType, owner, maxParticipants, activeParticipants, qualityPreset, status, proximity, description, permissions, allowedRoles, maxBitrateKbps, createdAt, autoCloseMinutes, analytics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceChannel {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    activeParticipants: ").append(toIndentedString(activeParticipants)).append("\n");
    sb.append("    qualityPreset: ").append(toIndentedString(qualityPreset)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    proximity: ").append(toIndentedString(proximity)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
    sb.append("    allowedRoles: ").append(toIndentedString(allowedRoles)).append("\n");
    sb.append("    maxBitrateKbps: ").append(toIndentedString(maxBitrateKbps)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    autoCloseMinutes: ").append(toIndentedString(autoCloseMinutes)).append("\n");
    sb.append("    analytics: ").append(toIndentedString(analytics)).append("\n");
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

