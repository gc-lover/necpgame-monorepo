package com.necpgame.systemservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * NotificationPlanTemplatesInner
 */

@JsonTypeName("NotificationPlan_templates_inner")

public class NotificationPlanTemplatesInner {

  private String channel;

  private String templateId;

  @Valid
  private List<String> localization = new ArrayList<>();

  public NotificationPlanTemplatesInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationPlanTemplatesInner(String channel, String templateId) {
    this.channel = channel;
    this.templateId = templateId;
  }

  public NotificationPlanTemplatesInner channel(String channel) {
    this.channel = channel;
    return this;
  }

  /**
   * Get channel
   * @return channel
   */
  @NotNull 
  @Schema(name = "channel", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("channel")
  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public NotificationPlanTemplatesInner templateId(String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  @NotNull 
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("templateId")
  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public NotificationPlanTemplatesInner localization(List<String> localization) {
    this.localization = localization;
    return this;
  }

  public NotificationPlanTemplatesInner addLocalizationItem(String localizationItem) {
    if (this.localization == null) {
      this.localization = new ArrayList<>();
    }
    this.localization.add(localizationItem);
    return this;
  }

  /**
   * Get localization
   * @return localization
   */
  
  @Schema(name = "localization", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("localization")
  public List<String> getLocalization() {
    return localization;
  }

  public void setLocalization(List<String> localization) {
    this.localization = localization;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPlanTemplatesInner notificationPlanTemplatesInner = (NotificationPlanTemplatesInner) o;
    return Objects.equals(this.channel, notificationPlanTemplatesInner.channel) &&
        Objects.equals(this.templateId, notificationPlanTemplatesInner.templateId) &&
        Objects.equals(this.localization, notificationPlanTemplatesInner.localization);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channel, templateId, localization);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPlanTemplatesInner {\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    localization: ").append(toIndentedString(localization)).append("\n");
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

