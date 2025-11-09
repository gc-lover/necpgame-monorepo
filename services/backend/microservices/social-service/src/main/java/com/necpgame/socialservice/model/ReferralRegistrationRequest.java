package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReferralRegistrationRequest
 */


public class ReferralRegistrationRequest {

  private String newPlayerId;

  private String referralCode;

  private @Nullable String sourceChannel;

  private @Nullable String deviceInfo;

  public ReferralRegistrationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReferralRegistrationRequest(String newPlayerId, String referralCode) {
    this.newPlayerId = newPlayerId;
    this.referralCode = referralCode;
  }

  public ReferralRegistrationRequest newPlayerId(String newPlayerId) {
    this.newPlayerId = newPlayerId;
    return this;
  }

  /**
   * Get newPlayerId
   * @return newPlayerId
   */
  @NotNull 
  @Schema(name = "newPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("newPlayerId")
  public String getNewPlayerId() {
    return newPlayerId;
  }

  public void setNewPlayerId(String newPlayerId) {
    this.newPlayerId = newPlayerId;
  }

  public ReferralRegistrationRequest referralCode(String referralCode) {
    this.referralCode = referralCode;
    return this;
  }

  /**
   * Get referralCode
   * @return referralCode
   */
  @NotNull 
  @Schema(name = "referralCode", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("referralCode")
  public String getReferralCode() {
    return referralCode;
  }

  public void setReferralCode(String referralCode) {
    this.referralCode = referralCode;
  }

  public ReferralRegistrationRequest sourceChannel(@Nullable String sourceChannel) {
    this.sourceChannel = sourceChannel;
    return this;
  }

  /**
   * Get sourceChannel
   * @return sourceChannel
   */
  
  @Schema(name = "sourceChannel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sourceChannel")
  public @Nullable String getSourceChannel() {
    return sourceChannel;
  }

  public void setSourceChannel(@Nullable String sourceChannel) {
    this.sourceChannel = sourceChannel;
  }

  public ReferralRegistrationRequest deviceInfo(@Nullable String deviceInfo) {
    this.deviceInfo = deviceInfo;
    return this;
  }

  /**
   * Get deviceInfo
   * @return deviceInfo
   */
  
  @Schema(name = "deviceInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("deviceInfo")
  public @Nullable String getDeviceInfo() {
    return deviceInfo;
  }

  public void setDeviceInfo(@Nullable String deviceInfo) {
    this.deviceInfo = deviceInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferralRegistrationRequest referralRegistrationRequest = (ReferralRegistrationRequest) o;
    return Objects.equals(this.newPlayerId, referralRegistrationRequest.newPlayerId) &&
        Objects.equals(this.referralCode, referralRegistrationRequest.referralCode) &&
        Objects.equals(this.sourceChannel, referralRegistrationRequest.sourceChannel) &&
        Objects.equals(this.deviceInfo, referralRegistrationRequest.deviceInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(newPlayerId, referralCode, sourceChannel, deviceInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReferralRegistrationRequest {\n");
    sb.append("    newPlayerId: ").append(toIndentedString(newPlayerId)).append("\n");
    sb.append("    referralCode: ").append(toIndentedString(referralCode)).append("\n");
    sb.append("    sourceChannel: ").append(toIndentedString(sourceChannel)).append("\n");
    sb.append("    deviceInfo: ").append(toIndentedString(deviceInfo)).append("\n");
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

