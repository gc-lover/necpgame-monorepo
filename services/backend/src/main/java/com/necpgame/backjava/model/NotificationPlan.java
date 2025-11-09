package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.backjava.model.NotificationPlanAudience;
import com.necpgame.backjava.model.NotificationPlanScheduleInner;
import com.necpgame.backjava.model.NotificationPlanTemplatesInner;
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
 * NotificationPlan
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class NotificationPlan {

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

  @Valid
  private List<@Valid NotificationPlanTemplatesInner> templates = new ArrayList<>();

  @Valid
  private List<@Valid NotificationPlanScheduleInner> schedule = new ArrayList<>();

  private @Nullable NotificationPlanAudience audience;

  public NotificationPlan() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationPlan(List<ChannelsEnum> channels) {
    this.channels = channels;
  }

  public NotificationPlan channels(List<ChannelsEnum> channels) {
    this.channels = channels;
    return this;
  }

  public NotificationPlan addChannelsItem(ChannelsEnum channelsItem) {
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

  public NotificationPlan templates(List<@Valid NotificationPlanTemplatesInner> templates) {
    this.templates = templates;
    return this;
  }

  public NotificationPlan addTemplatesItem(NotificationPlanTemplatesInner templatesItem) {
    if (this.templates == null) {
      this.templates = new ArrayList<>();
    }
    this.templates.add(templatesItem);
    return this;
  }

  /**
   * Get templates
   * @return templates
   */
  @Valid 
  @Schema(name = "templates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templates")
  public List<@Valid NotificationPlanTemplatesInner> getTemplates() {
    return templates;
  }

  public void setTemplates(List<@Valid NotificationPlanTemplatesInner> templates) {
    this.templates = templates;
  }

  public NotificationPlan schedule(List<@Valid NotificationPlanScheduleInner> schedule) {
    this.schedule = schedule;
    return this;
  }

  public NotificationPlan addScheduleItem(NotificationPlanScheduleInner scheduleItem) {
    if (this.schedule == null) {
      this.schedule = new ArrayList<>();
    }
    this.schedule.add(scheduleItem);
    return this;
  }

  /**
   * Get schedule
   * @return schedule
   */
  @Valid 
  @Schema(name = "schedule", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("schedule")
  public List<@Valid NotificationPlanScheduleInner> getSchedule() {
    return schedule;
  }

  public void setSchedule(List<@Valid NotificationPlanScheduleInner> schedule) {
    this.schedule = schedule;
  }

  public NotificationPlan audience(@Nullable NotificationPlanAudience audience) {
    this.audience = audience;
    return this;
  }

  /**
   * Get audience
   * @return audience
   */
  @Valid 
  @Schema(name = "audience", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audience")
  public @Nullable NotificationPlanAudience getAudience() {
    return audience;
  }

  public void setAudience(@Nullable NotificationPlanAudience audience) {
    this.audience = audience;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPlan notificationPlan = (NotificationPlan) o;
    return Objects.equals(this.channels, notificationPlan.channels) &&
        Objects.equals(this.templates, notificationPlan.templates) &&
        Objects.equals(this.schedule, notificationPlan.schedule) &&
        Objects.equals(this.audience, notificationPlan.audience);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, templates, schedule, audience);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPlan {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
    sb.append("    schedule: ").append(toIndentedString(schedule)).append("\n");
    sb.append("    audience: ").append(toIndentedString(audience)).append("\n");
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

