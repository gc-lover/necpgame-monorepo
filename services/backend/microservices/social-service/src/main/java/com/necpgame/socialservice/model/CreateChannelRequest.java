package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.ChannelSettings;
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
 * CreateChannelRequest
 */


public class CreateChannelRequest {

  private String channelName;

  /**
   * Gets or Sets channelType
   */
  public enum ChannelTypeEnum {
    CUSTOM("CUSTOM"),
    
    EVENT("EVENT");

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

  private @Nullable ChannelSettings settings;

  private Boolean inviteOnly = false;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public CreateChannelRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateChannelRequest(String channelName, ChannelTypeEnum channelType) {
    this.channelName = channelName;
    this.channelType = channelType;
  }

  public CreateChannelRequest channelName(String channelName) {
    this.channelName = channelName;
    return this;
  }

  /**
   * Get channelName
   * @return channelName
   */
  @NotNull @Size(min = 3, max = 50) 
  @Schema(name = "channelName", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channelName")
  public String getChannelName() {
    return channelName;
  }

  public void setChannelName(String channelName) {
    this.channelName = channelName;
  }

  public CreateChannelRequest channelType(ChannelTypeEnum channelType) {
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

  public CreateChannelRequest settings(@Nullable ChannelSettings settings) {
    this.settings = settings;
    return this;
  }

  /**
   * Get settings
   * @return settings
   */
  @Valid 
  @Schema(name = "settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("settings")
  public @Nullable ChannelSettings getSettings() {
    return settings;
  }

  public void setSettings(@Nullable ChannelSettings settings) {
    this.settings = settings;
  }

  public CreateChannelRequest inviteOnly(Boolean inviteOnly) {
    this.inviteOnly = inviteOnly;
    return this;
  }

  /**
   * Get inviteOnly
   * @return inviteOnly
   */
  
  @Schema(name = "inviteOnly", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteOnly")
  public Boolean getInviteOnly() {
    return inviteOnly;
  }

  public void setInviteOnly(Boolean inviteOnly) {
    this.inviteOnly = inviteOnly;
  }

  public CreateChannelRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateChannelRequest createChannelRequest = (CreateChannelRequest) o;
    return Objects.equals(this.channelName, createChannelRequest.channelName) &&
        Objects.equals(this.channelType, createChannelRequest.channelType) &&
        Objects.equals(this.settings, createChannelRequest.settings) &&
        Objects.equals(this.inviteOnly, createChannelRequest.inviteOnly) &&
        Objects.equals(this.expiresAt, createChannelRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channelName, channelType, settings, inviteOnly, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateChannelRequest {\n");
    sb.append("    channelName: ").append(toIndentedString(channelName)).append("\n");
    sb.append("    channelType: ").append(toIndentedString(channelType)).append("\n");
    sb.append("    settings: ").append(toIndentedString(settings)).append("\n");
    sb.append("    inviteOnly: ").append(toIndentedString(inviteOnly)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

