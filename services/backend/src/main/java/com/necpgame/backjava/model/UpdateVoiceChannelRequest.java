package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.ProximitySettings;
import com.necpgame.backjava.model.VoiceChannelPermissions;
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
 * UpdateVoiceChannelRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class UpdateVoiceChannelRequest {

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

  private @Nullable QualityPresetEnum qualityPreset;

  private @Nullable Integer maxParticipants;

  private @Nullable VoiceChannelPermissions permissions;

  @Valid
  private List<String> allowedRoles = new ArrayList<>();

  private @Nullable ProximitySettings proximity;

  private @Nullable Integer autoCloseMinutes;

  public UpdateVoiceChannelRequest qualityPreset(@Nullable QualityPresetEnum qualityPreset) {
    this.qualityPreset = qualityPreset;
    return this;
  }

  /**
   * Get qualityPreset
   * @return qualityPreset
   */
  
  @Schema(name = "qualityPreset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("qualityPreset")
  public @Nullable QualityPresetEnum getQualityPreset() {
    return qualityPreset;
  }

  public void setQualityPreset(@Nullable QualityPresetEnum qualityPreset) {
    this.qualityPreset = qualityPreset;
  }

  public UpdateVoiceChannelRequest maxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
    return this;
  }

  /**
   * Get maxParticipants
   * minimum: 2
   * maximum: 128
   * @return maxParticipants
   */
  @Min(value = 2) @Max(value = 128) 
  @Schema(name = "maxParticipants", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maxParticipants")
  public @Nullable Integer getMaxParticipants() {
    return maxParticipants;
  }

  public void setMaxParticipants(@Nullable Integer maxParticipants) {
    this.maxParticipants = maxParticipants;
  }

  public UpdateVoiceChannelRequest permissions(@Nullable VoiceChannelPermissions permissions) {
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

  public UpdateVoiceChannelRequest allowedRoles(List<String> allowedRoles) {
    this.allowedRoles = allowedRoles;
    return this;
  }

  public UpdateVoiceChannelRequest addAllowedRolesItem(String allowedRolesItem) {
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

  public UpdateVoiceChannelRequest proximity(@Nullable ProximitySettings proximity) {
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

  public UpdateVoiceChannelRequest autoCloseMinutes(@Nullable Integer autoCloseMinutes) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateVoiceChannelRequest updateVoiceChannelRequest = (UpdateVoiceChannelRequest) o;
    return Objects.equals(this.qualityPreset, updateVoiceChannelRequest.qualityPreset) &&
        Objects.equals(this.maxParticipants, updateVoiceChannelRequest.maxParticipants) &&
        Objects.equals(this.permissions, updateVoiceChannelRequest.permissions) &&
        Objects.equals(this.allowedRoles, updateVoiceChannelRequest.allowedRoles) &&
        Objects.equals(this.proximity, updateVoiceChannelRequest.proximity) &&
        Objects.equals(this.autoCloseMinutes, updateVoiceChannelRequest.autoCloseMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(qualityPreset, maxParticipants, permissions, allowedRoles, proximity, autoCloseMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateVoiceChannelRequest {\n");
    sb.append("    qualityPreset: ").append(toIndentedString(qualityPreset)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    permissions: ").append(toIndentedString(permissions)).append("\n");
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

