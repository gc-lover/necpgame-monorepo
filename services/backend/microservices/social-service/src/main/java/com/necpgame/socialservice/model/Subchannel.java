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
 * Subchannel
 */


public class Subchannel {

  private String subchannelId;

  /**
   * Gets or Sets purpose
   */
  public enum PurposeEnum {
    MAIN("main"),
    
    ROLE("role"),
    
    STRATEGY("strategy"),
    
    PRIVATE("private");

    private final String value;

    PurposeEnum(String value) {
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
    public static PurposeEnum fromValue(String value) {
      for (PurposeEnum b : PurposeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PurposeEnum purpose;

  private @Nullable String name;

  private Integer maxParticipants;

  private @Nullable Boolean isLocked;

  private @Nullable Boolean passwordProtected;

  private @Nullable String voiceChannelId;

  public Subchannel() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public Subchannel(String subchannelId, PurposeEnum purpose, Integer maxParticipants) {
    this.subchannelId = subchannelId;
    this.purpose = purpose;
    this.maxParticipants = maxParticipants;
  }

  public Subchannel subchannelId(String subchannelId) {
    this.subchannelId = subchannelId;
    return this;
  }

  /**
   * Get subchannelId
   * @return subchannelId
   */
  @NotNull 
  @Schema(name = "subchannelId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subchannelId")
  public String getSubchannelId() {
    return subchannelId;
  }

  public void setSubchannelId(String subchannelId) {
    this.subchannelId = subchannelId;
  }

  public Subchannel purpose(PurposeEnum purpose) {
    this.purpose = purpose;
    return this;
  }

  /**
   * Get purpose
   * @return purpose
   */
  @NotNull 
  @Schema(name = "purpose", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("purpose")
  public PurposeEnum getPurpose() {
    return purpose;
  }

  public void setPurpose(PurposeEnum purpose) {
    this.purpose = purpose;
  }

  public Subchannel name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public Subchannel maxParticipants(Integer maxParticipants) {
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

  public Subchannel isLocked(@Nullable Boolean isLocked) {
    this.isLocked = isLocked;
    return this;
  }

  /**
   * Get isLocked
   * @return isLocked
   */
  
  @Schema(name = "isLocked", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("isLocked")
  public @Nullable Boolean getIsLocked() {
    return isLocked;
  }

  public void setIsLocked(@Nullable Boolean isLocked) {
    this.isLocked = isLocked;
  }

  public Subchannel passwordProtected(@Nullable Boolean passwordProtected) {
    this.passwordProtected = passwordProtected;
    return this;
  }

  /**
   * Get passwordProtected
   * @return passwordProtected
   */
  
  @Schema(name = "passwordProtected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passwordProtected")
  public @Nullable Boolean getPasswordProtected() {
    return passwordProtected;
  }

  public void setPasswordProtected(@Nullable Boolean passwordProtected) {
    this.passwordProtected = passwordProtected;
  }

  public Subchannel voiceChannelId(@Nullable String voiceChannelId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Subchannel subchannel = (Subchannel) o;
    return Objects.equals(this.subchannelId, subchannel.subchannelId) &&
        Objects.equals(this.purpose, subchannel.purpose) &&
        Objects.equals(this.name, subchannel.name) &&
        Objects.equals(this.maxParticipants, subchannel.maxParticipants) &&
        Objects.equals(this.isLocked, subchannel.isLocked) &&
        Objects.equals(this.passwordProtected, subchannel.passwordProtected) &&
        Objects.equals(this.voiceChannelId, subchannel.voiceChannelId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subchannelId, purpose, name, maxParticipants, isLocked, passwordProtected, voiceChannelId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Subchannel {\n");
    sb.append("    subchannelId: ").append(toIndentedString(subchannelId)).append("\n");
    sb.append("    purpose: ").append(toIndentedString(purpose)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    maxParticipants: ").append(toIndentedString(maxParticipants)).append("\n");
    sb.append("    isLocked: ").append(toIndentedString(isLocked)).append("\n");
    sb.append("    passwordProtected: ").append(toIndentedString(passwordProtected)).append("\n");
    sb.append("    voiceChannelId: ").append(toIndentedString(voiceChannelId)).append("\n");
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

