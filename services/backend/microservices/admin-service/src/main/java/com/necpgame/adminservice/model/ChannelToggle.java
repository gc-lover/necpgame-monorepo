package com.necpgame.adminservice.model;

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
 * ChannelToggle
 */


public class ChannelToggle {

  private @Nullable Boolean enabled;

  private @Nullable String templateId;

  private @Nullable Integer priority;

  public ChannelToggle enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public ChannelToggle templateId(@Nullable String templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
   */
  
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templateId")
  public @Nullable String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(@Nullable String templateId) {
    this.templateId = templateId;
  }

  public ChannelToggle priority(@Nullable Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * minimum: 1
   * maximum: 10
   * @return priority
   */
  @Min(value = 1) @Max(value = 10) 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable Integer getPriority() {
    return priority;
  }

  public void setPriority(@Nullable Integer priority) {
    this.priority = priority;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChannelToggle channelToggle = (ChannelToggle) o;
    return Objects.equals(this.enabled, channelToggle.enabled) &&
        Objects.equals(this.templateId, channelToggle.templateId) &&
        Objects.equals(this.priority, channelToggle.priority);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, templateId, priority);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelToggle {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
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

