package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * SystemMaintenanceWindowsWindowIdNotificationsPostRequest
 */

@JsonTypeName("_system_maintenance_windows__windowId__notifications_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class SystemMaintenanceWindowsWindowIdNotificationsPostRequest {

  /**
   * Gets or Sets channels
   */
  public enum ChannelsEnum {
    IN_GAME("IN_GAME"),
    
    EMAIL("EMAIL"),
    
    PUSH("PUSH"),
    
    STATUS_PAGE("STATUS_PAGE"),
    
    DISCORD("DISCORD");

    private final String value;

    ChannelsEnum(String value) {
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
    public static ChannelsEnum fromValue(String value) {
      for (ChannelsEnum b : ChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<ChannelsEnum> channels = new ArrayList<>();

  private @Nullable String templateOverride;

  private @Nullable String message;

  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest(List<ChannelsEnum> channels) {
    this.channels = channels;
  }

  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest channels(List<ChannelsEnum> channels) {
    this.channels = channels;
    return this;
  }

  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest addChannelsItem(ChannelsEnum channelsItem) {
    if (this.channels == null) {
      this.channels = new ArrayList<>();
    }
    this.channels.add(channelsItem);
    return this;
  }

  /**
   * Get channels
   * @return channels
   */
  @NotNull 
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channels")
  public List<ChannelsEnum> getChannels() {
    return channels;
  }

  public void setChannels(List<ChannelsEnum> channels) {
    this.channels = channels;
  }

  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest templateOverride(@Nullable String templateOverride) {
    this.templateOverride = templateOverride;
    return this;
  }

  /**
   * Get templateOverride
   * @return templateOverride
   */
  
  @Schema(name = "templateOverride", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templateOverride")
  public @Nullable String getTemplateOverride() {
    return templateOverride;
  }

  public void setTemplateOverride(@Nullable String templateOverride) {
    this.templateOverride = templateOverride;
  }

  public SystemMaintenanceWindowsWindowIdNotificationsPostRequest message(@Nullable String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  
  @Schema(name = "message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("message")
  public @Nullable String getMessage() {
    return message;
  }

  public void setMessage(@Nullable String message) {
    this.message = message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SystemMaintenanceWindowsWindowIdNotificationsPostRequest systemMaintenanceWindowsWindowIdNotificationsPostRequest = (SystemMaintenanceWindowsWindowIdNotificationsPostRequest) o;
    return Objects.equals(this.channels, systemMaintenanceWindowsWindowIdNotificationsPostRequest.channels) &&
        Objects.equals(this.templateOverride, systemMaintenanceWindowsWindowIdNotificationsPostRequest.templateOverride) &&
        Objects.equals(this.message, systemMaintenanceWindowsWindowIdNotificationsPostRequest.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, templateOverride, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SystemMaintenanceWindowsWindowIdNotificationsPostRequest {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    templateOverride: ").append(toIndentedString(templateOverride)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

