package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationPreferenceCategory;
import com.necpgame.notificationservice.model.NotificationPreferenceChannel;
import com.necpgame.notificationservice.model.QuietHours;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationPreferences
 */


public class NotificationPreferences {

  @Valid
  private List<@Valid NotificationPreferenceChannel> channels = new ArrayList<>();

  @Valid
  private List<@Valid NotificationPreferenceCategory> categories = new ArrayList<>();

  private @Nullable QuietHours quietHours;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime mutedUntil;

  public NotificationPreferences channels(List<@Valid NotificationPreferenceChannel> channels) {
    this.channels = channels;
    return this;
  }

  public NotificationPreferences addChannelsItem(NotificationPreferenceChannel channelsItem) {
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
  @Valid 
  @Schema(name = "channels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("channels")
  public List<@Valid NotificationPreferenceChannel> getChannels() {
    return channels;
  }

  public void setChannels(List<@Valid NotificationPreferenceChannel> channels) {
    this.channels = channels;
  }

  public NotificationPreferences categories(List<@Valid NotificationPreferenceCategory> categories) {
    this.categories = categories;
    return this;
  }

  public NotificationPreferences addCategoriesItem(NotificationPreferenceCategory categoriesItem) {
    if (this.categories == null) {
      this.categories = new ArrayList<>();
    }
    this.categories.add(categoriesItem);
    return this;
  }

  /**
   * Get categories
   * @return categories
   */
  @Valid 
  @Schema(name = "categories", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("categories")
  public List<@Valid NotificationPreferenceCategory> getCategories() {
    return categories;
  }

  public void setCategories(List<@Valid NotificationPreferenceCategory> categories) {
    this.categories = categories;
  }

  public NotificationPreferences quietHours(@Nullable QuietHours quietHours) {
    this.quietHours = quietHours;
    return this;
  }

  /**
   * Get quietHours
   * @return quietHours
   */
  @Valid 
  @Schema(name = "quietHours", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quietHours")
  public @Nullable QuietHours getQuietHours() {
    return quietHours;
  }

  public void setQuietHours(@Nullable QuietHours quietHours) {
    this.quietHours = quietHours;
  }

  public NotificationPreferences mutedUntil(@Nullable OffsetDateTime mutedUntil) {
    this.mutedUntil = mutedUntil;
    return this;
  }

  /**
   * Get mutedUntil
   * @return mutedUntil
   */
  @Valid 
  @Schema(name = "mutedUntil", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mutedUntil")
  public @Nullable OffsetDateTime getMutedUntil() {
    return mutedUntil;
  }

  public void setMutedUntil(@Nullable OffsetDateTime mutedUntil) {
    this.mutedUntil = mutedUntil;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPreferences notificationPreferences = (NotificationPreferences) o;
    return Objects.equals(this.channels, notificationPreferences.channels) &&
        Objects.equals(this.categories, notificationPreferences.categories) &&
        Objects.equals(this.quietHours, notificationPreferences.quietHours) &&
        Objects.equals(this.mutedUntil, notificationPreferences.mutedUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, categories, quietHours, mutedUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPreferences {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
    sb.append("    quietHours: ").append(toIndentedString(quietHours)).append("\n");
    sb.append("    mutedUntil: ").append(toIndentedString(mutedUntil)).append("\n");
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

