package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * NotificationDeviceRegisterRequest
 */


public class NotificationDeviceRegisterRequest {

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

  private String token;

  @Valid
  private List<String> capabilities = new ArrayList<>();

  private @Nullable String timezone;

  public NotificationDeviceRegisterRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationDeviceRegisterRequest(String deviceId, PlatformEnum platform, String token) {
    this.deviceId = deviceId;
    this.platform = platform;
    this.token = token;
  }

  public NotificationDeviceRegisterRequest deviceId(String deviceId) {
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

  public NotificationDeviceRegisterRequest platform(PlatformEnum platform) {
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

  public NotificationDeviceRegisterRequest token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  @NotNull 
  @Schema(name = "token", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public NotificationDeviceRegisterRequest capabilities(List<String> capabilities) {
    this.capabilities = capabilities;
    return this;
  }

  public NotificationDeviceRegisterRequest addCapabilitiesItem(String capabilitiesItem) {
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

  public NotificationDeviceRegisterRequest timezone(@Nullable String timezone) {
    this.timezone = timezone;
    return this;
  }

  /**
   * Get timezone
   * @return timezone
   */
  
  @Schema(name = "timezone", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timezone")
  public @Nullable String getTimezone() {
    return timezone;
  }

  public void setTimezone(@Nullable String timezone) {
    this.timezone = timezone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDeviceRegisterRequest notificationDeviceRegisterRequest = (NotificationDeviceRegisterRequest) o;
    return Objects.equals(this.deviceId, notificationDeviceRegisterRequest.deviceId) &&
        Objects.equals(this.platform, notificationDeviceRegisterRequest.platform) &&
        Objects.equals(this.token, notificationDeviceRegisterRequest.token) &&
        Objects.equals(this.capabilities, notificationDeviceRegisterRequest.capabilities) &&
        Objects.equals(this.timezone, notificationDeviceRegisterRequest.timezone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deviceId, platform, token, capabilities, timezone);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDeviceRegisterRequest {\n");
    sb.append("    deviceId: ").append(toIndentedString(deviceId)).append("\n");
    sb.append("    platform: ").append(toIndentedString(platform)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
    sb.append("    capabilities: ").append(toIndentedString(capabilities)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
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

