package com.necpgame.adminservice.model;

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
 * AlertRule
 */


public class AlertRule {

  private @Nullable String ruleName;

  private @Nullable String condition;

  /**
   * Gets or Sets notificationChannels
   */
  public enum NotificationChannelsEnum {
    EMAIL("email"),
    
    SLACK("slack"),
    
    PAGERDUTY("pagerduty");

    private final String value;

    NotificationChannelsEnum(String value) {
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
    public static NotificationChannelsEnum fromValue(String value) {
      for (NotificationChannelsEnum b : NotificationChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<NotificationChannelsEnum> notificationChannels = new ArrayList<>();

  public AlertRule ruleName(@Nullable String ruleName) {
    this.ruleName = ruleName;
    return this;
  }

  /**
   * Get ruleName
   * @return ruleName
   */
  
  @Schema(name = "rule_name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rule_name")
  public @Nullable String getRuleName() {
    return ruleName;
  }

  public void setRuleName(@Nullable String ruleName) {
    this.ruleName = ruleName;
  }

  public AlertRule condition(@Nullable String condition) {
    this.condition = condition;
    return this;
  }

  /**
   * Alert condition (e.g., \"error_rate > 5%\")
   * @return condition
   */
  
  @Schema(name = "condition", description = "Alert condition (e.g., \"error_rate > 5%\")", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("condition")
  public @Nullable String getCondition() {
    return condition;
  }

  public void setCondition(@Nullable String condition) {
    this.condition = condition;
  }

  public AlertRule notificationChannels(List<NotificationChannelsEnum> notificationChannels) {
    this.notificationChannels = notificationChannels;
    return this;
  }

  public AlertRule addNotificationChannelsItem(NotificationChannelsEnum notificationChannelsItem) {
    if (this.notificationChannels == null) {
      this.notificationChannels = new ArrayList<>();
    }
    this.notificationChannels.add(notificationChannelsItem);
    return this;
  }

  /**
   * Get notificationChannels
   * @return notificationChannels
   */
  
  @Schema(name = "notification_channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notification_channels")
  public List<NotificationChannelsEnum> getNotificationChannels() {
    return notificationChannels;
  }

  public void setNotificationChannels(List<NotificationChannelsEnum> notificationChannels) {
    this.notificationChannels = notificationChannels;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AlertRule alertRule = (AlertRule) o;
    return Objects.equals(this.ruleName, alertRule.ruleName) &&
        Objects.equals(this.condition, alertRule.condition) &&
        Objects.equals(this.notificationChannels, alertRule.notificationChannels);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ruleName, condition, notificationChannels);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AlertRule {\n");
    sb.append("    ruleName: ").append(toIndentedString(ruleName)).append("\n");
    sb.append("    condition: ").append(toIndentedString(condition)).append("\n");
    sb.append("    notificationChannels: ").append(toIndentedString(notificationChannels)).append("\n");
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

