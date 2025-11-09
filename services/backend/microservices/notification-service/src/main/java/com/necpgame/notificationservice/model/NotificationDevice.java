package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * NotificationDevice
 */


public class NotificationDevice {

  private String deviceId;

  /**
   * Gets or Sets platform
   */
  public enum PlatformEnum {
    IOS("IOS"),
    
    ANDROID("ANDROID"),
    
    WINDOWS("WINDOWS"),
    
    MAC("MAC"),
    
    WEB("WEB");

    private final String value;

    PlatformEnum(String value) {
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
    public static PlatformEnum fromValue(String value) {
      for (PlatformEnum b : PlatformEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private PlatformEnum platform;

  private @Nullable String token;

  @Valid
  private List<String> capabilities = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastSeen;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    
    INACTIVE("INACTIVE"),
    
    REVOKED("REVOKED");

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

  @Valid
  private Map<String, Object> metadata = new HashMap<>();

  public NotificationDevice() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationDevice(String deviceId, PlatformEnum platform, StatusEnum status) {
    this.deviceId = deviceId;
    this.platform = platform;
    this.status = status;
  }

  public NotificationDevice deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  /**
   * Get deviceId
   * @return deviceId
   */
  @NotNull 
  @Schema(name = "deviceId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("deviceId")
  public String getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
  }

  public NotificationDevice platform(PlatformEnum platform) {
    this.platform = platform;
    return this;
  }

  /**
   * Get platform
   * @return platform
   */
  @NotNull 
  @Schema(name = "platform", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("platform")
  public PlatformEnum getPlatform() {
    return platform;
  }

  public void setPlatform(PlatformEnum platform) {
    this.platform = platform;
  }

  public NotificationDevice token(@Nullable String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  
  @Schema(name = "token", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("token")
  public @Nullable String getToken() {
    return token;
  }

  public void setToken(@Nullable String token) {
    this.token = token;
  }

  public NotificationDevice capabilities(List<String> capabilities) {
    this.capabilities = capabilities;
    return this;
  }

  public NotificationDevice addCapabilitiesItem(String capabilitiesItem) {
    if (this.capabilities == null) {
      this.capabilities = new ArrayList<>();
    }
    this.capabilities.add(capabilitiesItem);
    return this;
  }

  /**
   * Get capabilities
   * @return capabilities
   */
  
  @Schema(name = "capabilities", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("capabilities")
  public List<String> getCapabilities() {
    return capabilities;
  }

  public void setCapabilities(List<String> capabilities) {
    this.capabilities = capabilities;
  }

  public NotificationDevice lastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
    return this;
  }

  /**
   * Get lastSeen
   * @return lastSeen
   */
  @Valid 
  @Schema(name = "lastSeen", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastSeen")
  public @Nullable OffsetDateTime getLastSeen() {
    return lastSeen;
  }

  public void setLastSeen(@Nullable OffsetDateTime lastSeen) {
    this.lastSeen = lastSeen;
  }

  public NotificationDevice status(StatusEnum status) {
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

  public NotificationDevice metadata(Map<String, Object> metadata) {
    this.metadata = metadata;
    return this;
  }

  public NotificationDevice putMetadataItem(String key, Object metadataItem) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    this.metadata.put(key, metadataItem);
    return this;
  }

  /**
   * Get metadata
   * @return metadata
   */
  
  @Schema(name = "metadata", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("metadata")
  public Map<String, Object> getMetadata() {
    return metadata;
  }

  public void setMetadata(Map<String, Object> metadata) {
    this.metadata = metadata;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDevice notificationDevice = (NotificationDevice) o;
    return Objects.equals(this.deviceId, notificationDevice.deviceId) &&
        Objects.equals(this.platform, notificationDevice.platform) &&
        Objects.equals(this.token, notificationDevice.token) &&
        Objects.equals(this.capabilities, notificationDevice.capabilities) &&
        Objects.equals(this.lastSeen, notificationDevice.lastSeen) &&
        Objects.equals(this.status, notificationDevice.status) &&
        Objects.equals(this.metadata, notificationDevice.metadata);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, platform, token, capabilities, lastSeen, status, metadata);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDevice {\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    capabilities: ").append(toIndentedString(capabilities)).append("\n");
    sb.append("    lastSeen: ").append(toIndentedString(lastSeen)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
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

