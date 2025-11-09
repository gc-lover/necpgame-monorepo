package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationPreferenceChannel;
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
 * NotificationPreferenceCategory
 */


public class NotificationPreferenceCategory {

  private @Nullable String category;

  private @Nullable Boolean enabled;

  @Valid
  private List<@Valid NotificationPreferenceChannel> channelOverrides = new ArrayList<>();

  public NotificationPreferenceCategory category(@Nullable String category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  
  @Schema(name = "category", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("category")
  public @Nullable String getCategory() {
    return category;
  }

  public void setCategory(@Nullable String category) {
    this.category = category;
  }

  public NotificationPreferenceCategory enabled(@Nullable Boolean enabled) {
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

  public NotificationPreferenceCategory channelOverrides(List<@Valid NotificationPreferenceChannel> channelOverrides) {
    this.channelOverrides = channelOverrides;
    return this;
  }

  public NotificationPreferenceCategory addChannelOverridesItem(NotificationPreferenceChannel channelOverridesItem) {
    if (this.channelOverrides == null) {
      this.channelOverrides = new ArrayList<>();
    }
    this.channelOverrides.add(channelOverridesItem);
    return this;
  }

  /**
   * Get channelOverrides
   * @return channelOverrides
   */
  @Valid 
  @Schema(name = "channelOverrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channelOverrides")
  public List<@Valid NotificationPreferenceChannel> getChannelOverrides() {
    return channelOverrides;
  }

  public void setChannelOverrides(List<@Valid NotificationPreferenceChannel> channelOverrides) {
    this.channelOverrides = channelOverrides;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPreferenceCategory notificationPreferenceCategory = (NotificationPreferenceCategory) o;
    return Objects.equals(this.category, notificationPreferenceCategory.category) &&
        Objects.equals(this.enabled, notificationPreferenceCategory.enabled) &&
        Objects.equals(this.channelOverrides, notificationPreferenceCategory.channelOverrides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(category, enabled, channelOverrides);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPreferenceCategory {\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    channelOverrides: ").append(toIndentedString(channelOverrides)).append("\n");
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

