package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.net.URI;
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
 * NotificationWebhookRequest
 */


public class NotificationWebhookRequest {

  private URI url;

  @Valid
  private List<String> events = new ArrayList<>();

  private @Nullable String secret;

  private Boolean enabled = true;

  public NotificationWebhookRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NotificationWebhookRequest(URI url, List<String> events) {
    this.url = url;
    this.events = events;
  }

  public NotificationWebhookRequest url(URI url) {
    this.url = url;
    return this;
  }

  /**
   * Get url
   * @return url
   */
  @NotNull @Valid 
  @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("url")
  public URI getUrl() {
    return url;
  }

  public void setUrl(URI url) {
    this.url = url;
  }

  public NotificationWebhookRequest events(List<String> events) {
    this.events = events;
    return this;
  }

  public NotificationWebhookRequest addEventsItem(String eventsItem) {
    if (this.events == null) {
      this.events = new ArrayList<>();
    }
    this.events.add(eventsItem);
    return this;
  }

  /**
   * Get events
   * @return events
   */
  @NotNull 
  @Schema(name = "events", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("events")
  public List<String> getEvents() {
    return events;
  }

  public void setEvents(List<String> events) {
    this.events = events;
  }

  public NotificationWebhookRequest secret(@Nullable String secret) {
    this.secret = secret;
    return this;
  }

  /**
   * Get secret
   * @return secret
   */
  
  @Schema(name = "secret", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("secret")
  public @Nullable String getSecret() {
    return secret;
  }

  public void setSecret(@Nullable String secret) {
    this.secret = secret;
  }

  public NotificationWebhookRequest enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationWebhookRequest notificationWebhookRequest = (NotificationWebhookRequest) o;
    return Objects.equals(this.url, notificationWebhookRequest.url) &&
        Objects.equals(this.events, notificationWebhookRequest.events) &&
        Objects.equals(this.secret, notificationWebhookRequest.secret) &&
        Objects.equals(this.enabled, notificationWebhookRequest.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(url, events, secret, enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationWebhookRequest {\n");
    sb.append("    url: ").append(toIndentedString(url)).append("\n");
    sb.append("    events: ").append(toIndentedString(events)).append("\n");
    sb.append("    secret: ").append(toIndentedString(secret)).append("\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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

