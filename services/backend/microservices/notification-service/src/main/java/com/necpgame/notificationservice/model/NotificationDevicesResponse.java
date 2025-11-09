package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationDevice;
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
 * NotificationDevicesResponse
 */


public class NotificationDevicesResponse {

  @Valid
  private List<@Valid NotificationDevice> devices = new ArrayList<>();

  public NotificationDevicesResponse devices(List<@Valid NotificationDevice> devices) {
    this.devices = devices;
    return this;
  }

  public NotificationDevicesResponse addDevicesItem(NotificationDevice devicesItem) {
    if (this.devices == null) {
      this.devices = new ArrayList<>();
    }
    this.devices.add(devicesItem);
    return this;
  }

  /**
   * Get devices
   * @return devices
   */
  @Valid 
  @Schema(name = "devices", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("devices")
  public List<@Valid NotificationDevice> getDevices() {
    return devices;
  }

  public void setDevices(List<@Valid NotificationDevice> devices) {
    this.devices = devices;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationDevicesResponse notificationDevicesResponse = (NotificationDevicesResponse) o;
    return Objects.equals(this.devices, notificationDevicesResponse.devices);
  }

  @Override
  public int hashCode() {
    return Objects.hash(devices);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationDevicesResponse {\n");
    sb.append("    devices: ").append(toIndentedString(devices)).append("\n");
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

