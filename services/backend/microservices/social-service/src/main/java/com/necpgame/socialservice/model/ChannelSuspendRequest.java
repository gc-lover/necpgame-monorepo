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
 * ChannelSuspendRequest
 */


public class ChannelSuspendRequest {

  private String reason;

  private Integer durationMinutes;

  private Boolean notifyMembers = true;

  public ChannelSuspendRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelSuspendRequest(String reason, Integer durationMinutes) {
    this.reason = reason;
    this.durationMinutes = durationMinutes;
  }

  public ChannelSuspendRequest reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull @Size(max = 200) 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public ChannelSuspendRequest durationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * minimum: 1
   * maximum: 1440
   * @return durationMinutes
   */
  @NotNull @Min(value = 1) @Max(value = 1440) 
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("durationMinutes")
  public Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  public ChannelSuspendRequest notifyMembers(Boolean notifyMembers) {
    this.notifyMembers = notifyMembers;
    return this;
  }

  /**
   * Get notifyMembers
   * @return notifyMembers
   */
  
  @Schema(name = "notifyMembers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifyMembers")
  public Boolean getNotifyMembers() {
    return notifyMembers;
  }

  public void setNotifyMembers(Boolean notifyMembers) {
    this.notifyMembers = notifyMembers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelSuspendRequest channelSuspendRequest = (ChannelSuspendRequest) o;
    return Objects.equals(this.reason, channelSuspendRequest.reason) &&
        Objects.equals(this.durationMinutes, channelSuspendRequest.durationMinutes) &&
        Objects.equals(this.notifyMembers, channelSuspendRequest.notifyMembers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, durationMinutes, notifyMembers);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelSuspendRequest {\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
    sb.append("    notifyMembers: ").append(toIndentedString(notifyMembers)).append("\n");
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

