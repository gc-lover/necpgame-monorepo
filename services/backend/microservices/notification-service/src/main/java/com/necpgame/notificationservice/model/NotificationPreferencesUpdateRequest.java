package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationPreferenceCategory;
import com.necpgame.notificationservice.model.NotificationPreferenceChannel;
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
 * NotificationPreferencesUpdateRequest
 */


public class NotificationPreferencesUpdateRequest {

  @Valid
  private List<@Valid NotificationPreferenceChannel> channels = new ArrayList<>();

  @Valid
  private List<@Valid NotificationPreferenceCategory> categories = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime mutedUntil;

  public NotificationPreferencesUpdateRequest channels(List<@Valid NotificationPreferenceChannel> channels) {
    this.channels = channels;
    return this;
  }

  public NotificationPreferencesUpdateRequest addChannelsItem(NotificationPreferenceChannel channelsItem) {
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

  public NotificationPreferencesUpdateRequest categories(List<@Valid NotificationPreferenceCategory> categories) {
    this.categories = categories;
    return this;
  }

  public NotificationPreferencesUpdateRequest addCategoriesItem(NotificationPreferenceCategory categoriesItem) {
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

  public NotificationPreferencesUpdateRequest mutedUntil(@Nullable OffsetDateTime mutedUntil) {
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
    NotificationPreferencesUpdateRequest notificationPreferencesUpdateRequest = (NotificationPreferencesUpdateRequest) o;
    return Objects.equals(this.channels, notificationPreferencesUpdateRequest.channels) &&
        Objects.equals(this.categories, notificationPreferencesUpdateRequest.categories) &&
        Objects.equals(this.mutedUntil, notificationPreferencesUpdateRequest.mutedUntil);
  }

  @Override
  public int hashCode() {
    return Objects.hash(channels, categories, mutedUntil);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPreferencesUpdateRequest {\n");
    sb.append("    channels: ").append(toIndentedString(channels)).append("\n");
    sb.append("    categories: ").append(toIndentedString(categories)).append("\n");
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

