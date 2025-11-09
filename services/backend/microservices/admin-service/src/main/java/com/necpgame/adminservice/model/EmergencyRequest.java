package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * EmergencyRequest
 */


public class EmergencyRequest {

  private String message;

  /**
   * Gets or Sets channels
   */
  public enum ChannelsEnum {
    IN_GAME_BANNER("in_game_banner"),
    
    MODAL("modal"),
    
    CHAT("chat"),
    
    PUSH("push"),
    
    EMAIL("email");

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

  /**
   * Gets or Sets severity
   */
  public enum SeverityEnum {
    CRITICAL("critical"),
    
    HIGH("high"),
    
    MEDIUM("medium");

    private final String value;

    SeverityEnum(String value) {
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
    public static SeverityEnum fromValue(String value) {
      for (SeverityEnum b : SeverityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable SeverityEnum severity;

  private @Nullable Boolean requireAck;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public EmergencyRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EmergencyRequest(String message, List<ChannelsEnum> channels) {
    this.message = message;
    this.channels = channels;
  }

  public EmergencyRequest message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Get message
   * @return message
   */
  @NotNull @Size(max = 500) 
  @Schema(name = "message", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public EmergencyRequest channels(List<ChannelsEnum> channels) {
    this.channels = channels;
    return this;
  }

  public EmergencyRequest addChannelsItem(ChannelsEnum channelsItem) {
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

  public EmergencyRequest severity(@Nullable SeverityEnum severity) {
    this.severity = severity;
    return this;
  }

  /**
   * Get severity
   * @return severity
   */
  
  @Schema(name = "severity", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("severity")
  public @Nullable SeverityEnum getSeverity() {
    return severity;
  }

  public void setSeverity(@Nullable SeverityEnum severity) {
    this.severity = severity;
  }

  public EmergencyRequest requireAck(@Nullable Boolean requireAck) {
    this.requireAck = requireAck;
    return this;
  }

  /**
   * Get requireAck
   * @return requireAck
   */
  
  @Schema(name = "requireAck", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("requireAck")
  public @Nullable Boolean getRequireAck() {
    return requireAck;
  }

  public void setRequireAck(@Nullable Boolean requireAck) {
    this.requireAck = requireAck;
  }

  public EmergencyRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
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
    EmergencyRequest emergencyRequest = (EmergencyRequest) o;
    return Objects.equals(this.message, emergencyRequest.message) &&
        Objects.equals(this.channels, emergencyRequest.channels) &&
        Objects.equals(this.severity, emergencyRequest.severity) &&
        Objects.equals(this.requireAck, emergencyRequest.requireAck) &&
        Objects.equals(this.expiresAt, emergencyRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, channels, severity, requireAck, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EmergencyRequest {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    severity: ").append(toIndentedString(severity)).append("\n");
    sb.append("    requireAck: ").append(toIndentedString(requireAck)).append("\n");
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

