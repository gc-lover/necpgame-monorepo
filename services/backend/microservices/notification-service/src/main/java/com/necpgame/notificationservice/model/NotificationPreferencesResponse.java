package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationPreferences;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationPreferencesResponse
 */


public class NotificationPreferencesResponse {

  private @Nullable NotificationPreferences preferences;

  public NotificationPreferencesResponse preferences(@Nullable NotificationPreferences preferences) {
    this.preferences = preferences;
    return this;
  }

  /**
   * Get preferences
   * @return preferences
   */
  @Valid 
  @Schema(name = "preferences", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("preferences")
  public @Nullable NotificationPreferences getPreferences() {
    return preferences;
  }

  public void setPreferences(@Nullable NotificationPreferences preferences) {
    this.preferences = preferences;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationPreferencesResponse notificationPreferencesResponse = (NotificationPreferencesResponse) o;
    return Objects.equals(this.preferences, notificationPreferencesResponse.preferences);
  }

  @Override
  public int hashCode() {
    return Objects.hash(preferences);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationPreferencesResponse {\n");
    sb.append("    preferences: ").append(toIndentedString(preferences)).append("\n");
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

