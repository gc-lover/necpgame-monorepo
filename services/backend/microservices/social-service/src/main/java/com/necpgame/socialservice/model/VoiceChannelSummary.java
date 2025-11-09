package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ProximitySettings;
import com.necpgame.socialservice.model.VoiceChannelOwner;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * VoiceChannelSummary
 */


public class VoiceChannelSummary {

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

  public VoiceChannelSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public VoiceChannelSummary(String channelId, String channelName, ChannelTypeEnum channelType, VoiceChannelOwner owner, QualityPresetEnum qualityPreset, StatusEnum status) {
    this.channelId = channelId;
    this.channelName = channelName;
    this.channelType = channelType;
    this.owner = owner;
    this.qualityPreset = qualityPreset;
    this.status = status;
  }

  public VoiceChannelSummary channelId(String channelId) {
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

  public VoiceChannelSummary channelName(String channelName) {
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

  public VoiceChannelSummary channelType(ChannelTypeEnum channelType) {
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

  public VoiceChannelSummary owner(VoiceChannelOwner owner) {
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

  public VoiceChannelSummary maxParticipants(@Nullable Integer maxParticipants) {
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

  public VoiceChannelSummary activeParticipants(@Nullable Integer activeParticipants) {
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

  public VoiceChannelSummary qualityPreset(QualityPresetEnum qualityPreset) {
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

  public VoiceChannelSummary status(StatusEnum status) {
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

  public VoiceChannelSummary proximity(@Nullable ProximitySettings proximity) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VoiceChannelSummary voiceChannelSummary = (VoiceChannelSummary) o;
    return Objects.equals(this.channelId, voiceChannelSummary.channelId) &&
        Objects.equals(this.channelName, voiceChannelSummary.channelName) &&
        Objects.equals(this.channelType, voiceChannelSummary.channelType) &&
        Objects.equals(this.owner, voiceChannelSummary.owner) &&
        Objects.equals(this.maxParticipants, voiceChannelSummary.maxParticipants) &&
        Objects.equals(this.activeParticipants, voiceChannelSummary.activeParticipants) &&
        Objects.equals(this.qualityPreset, voiceChannelSummary.qualityPreset) &&
        Objects.equals(this.status, voiceChannelSummary.status) &&
        Objects.equals(this.proximity, voiceChannelSummary.proximity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelId, channelName, channelType, owner, maxParticipants, activeParticipants, qualityPreset, status, proximity);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VoiceChannelSummary {\n");
    sb.append("    channelId: ").append(toIndentedString(channelId)).append("\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    owner: ").append(toIndentedString(owner)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    activeParticipants: ").append(toIndentedString(activeParticipants)).append("\n");
    sb.append("    qualityPreset: ").append(toIndentedString(qualityPreset)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    proximity: ").append(toIndentedString(proximity)).append("\n");
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

