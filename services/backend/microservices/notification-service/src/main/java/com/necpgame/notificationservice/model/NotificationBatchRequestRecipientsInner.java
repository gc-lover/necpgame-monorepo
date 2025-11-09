package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationBatchRequestRecipientsInner
 */

@JsonTypeName("NotificationBatchRequest_recipients_inner")

public class NotificationBatchRequestRecipientsInner {

  private @Nullable String recipientId;

  private @Nullable String locale;

  @Valid
  private Map<String, Object> data = new HashMap<>();

  public NotificationBatchRequestRecipientsInner recipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
    return this;
  }

  /**
   * Get recipientId
   * @return recipientId
   */
  
  @Schema(name = "recipientId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("recipientId")
  public @Nullable String getRecipientId() {
    return recipientId;
  }

  public void setRecipientId(@Nullable String recipientId) {
    this.recipientId = recipientId;
  }

  public NotificationBatchRequestRecipientsInner locale(@Nullable String locale) {
    this.locale = locale;
    return this;
  }

  /**
   * Get locale
   * @return locale
   */
  
  @Schema(name = "locale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("locale")
  public @Nullable String getLocale() {
    return locale;
  }

  public void setLocale(@Nullable String locale) {
    this.locale = locale;
  }

  public NotificationBatchRequestRecipientsInner data(Map<String, Object> data) {
    this.data = data;
    return this;
  }

  public NotificationBatchRequestRecipientsInner putDataItem(String key, Object dataItem) {
    if (this.data == null) {
      this.data = new HashMap<>();
    }
    this.data.put(key, dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  
  @Schema(name = "data", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("data")
  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationBatchRequestRecipientsInner notificationBatchRequestRecipientsInner = (NotificationBatchRequestRecipientsInner) o;
    return Objects.equals(this.recipientId, notificationBatchRequestRecipientsInner.recipientId) &&
        Objects.equals(this.locale, notificationBatchRequestRecipientsInner.locale) &&
        Objects.equals(this.data, notificationBatchRequestRecipientsInner.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recipientId, locale, data);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationBatchRequestRecipientsInner {\n");
    sb.append("    recipientId: ").append(toIndentedString(recipientId)).append("\n");
    sb.append("    locale: ").append(toIndentedString(locale)).append("\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
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

