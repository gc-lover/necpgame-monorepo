package com.necpgame.adminservice.model;

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
 * Параметры эскалации к следующему уровню поддержки.
 */

@Schema(name = "EscalationCreate", description = "Параметры эскалации к следующему уровню поддержки.")

public class EscalationCreate {

  private String targetGroup;

  private String reason;

  /**
   * Gets or Sets urgency
   */
  public enum UrgencyEnum {
    IMMEDIATE("immediate"),
    
    HIGH("high"),
    
    NORMAL("normal");

    private final String value;

    UrgencyEnum(String value) {
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
    public static UrgencyEnum fromValue(String value) {
      for (UrgencyEnum b : UrgencyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable UrgencyEnum urgency;

  private @Nullable String notifyChannel;

  public EscalationCreate() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public EscalationCreate(String targetGroup, String reason) {
    this.targetGroup = targetGroup;
    this.reason = reason;
  }

  public EscalationCreate targetGroup(String targetGroup) {
    this.targetGroup = targetGroup;
    return this;
  }

  /**
   * Get targetGroup
   * @return targetGroup
   */
  @NotNull 
  @Schema(name = "target_group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("target_group")
  public String getTargetGroup() {
    return targetGroup;
  }

  public void setTargetGroup(String targetGroup) {
    this.targetGroup = targetGroup;
  }

  public EscalationCreate reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @NotNull 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reason")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public EscalationCreate urgency(@Nullable UrgencyEnum urgency) {
    this.urgency = urgency;
    return this;
  }

  /**
   * Get urgency
   * @return urgency
   */
  
  @Schema(name = "urgency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("urgency")
  public @Nullable UrgencyEnum getUrgency() {
    return urgency;
  }

  public void setUrgency(@Nullable UrgencyEnum urgency) {
    this.urgency = urgency;
  }

  public EscalationCreate notifyChannel(@Nullable String notifyChannel) {
    this.notifyChannel = notifyChannel;
    return this;
  }

  /**
   * Get notifyChannel
   * @return notifyChannel
   */
  
  @Schema(name = "notify_channel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notify_channel")
  public @Nullable String getNotifyChannel() {
    return notifyChannel;
  }

  public void setNotifyChannel(@Nullable String notifyChannel) {
    this.notifyChannel = notifyChannel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EscalationCreate escalationCreate = (EscalationCreate) o;
    return Objects.equals(this.targetGroup, escalationCreate.targetGroup) &&
        Objects.equals(this.reason, escalationCreate.reason) &&
        Objects.equals(this.urgency, escalationCreate.urgency) &&
        Objects.equals(this.notifyChannel, escalationCreate.notifyChannel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetGroup, reason, urgency, notifyChannel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EscalationCreate {\n");
    sb.append("    targetGroup: ").append(toIndentedString(targetGroup)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    urgency: ").append(toIndentedString(urgency)).append("\n");
    sb.append("    notifyChannel: ").append(toIndentedString(notifyChannel)).append("\n");
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

