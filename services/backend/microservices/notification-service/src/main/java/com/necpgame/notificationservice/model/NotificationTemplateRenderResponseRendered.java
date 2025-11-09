package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationTemplateRenderResponseRendered
 */

@JsonTypeName("NotificationTemplateRenderResponse_rendered")

public class NotificationTemplateRenderResponseRendered {

  private @Nullable String title;

  private @Nullable String body;

  private @Nullable String html;

  private @Nullable String plainText;

  public NotificationTemplateRenderResponseRendered title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * Get title
   * @return title
   */
  
  @Schema(name = "title", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public NotificationTemplateRenderResponseRendered body(@Nullable String body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable String getBody() {
    return body;
  }

  public void setBody(@Nullable String body) {
    this.body = body;
  }

  public NotificationTemplateRenderResponseRendered html(@Nullable String html) {
    this.html = html;
    return this;
  }

  /**
   * Get html
   * @return html
   */
  
  @Schema(name = "html", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("html")
  public @Nullable String getHtml() {
    return html;
  }

  public void setHtml(@Nullable String html) {
    this.html = html;
  }

  public NotificationTemplateRenderResponseRendered plainText(@Nullable String plainText) {
    this.plainText = plainText;
    return this;
  }

  /**
   * Get plainText
   * @return plainText
   */
  
  @Schema(name = "plainText", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("plainText")
  public @Nullable String getPlainText() {
    return plainText;
  }

  public void setPlainText(@Nullable String plainText) {
    this.plainText = plainText;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTemplateRenderResponseRendered notificationTemplateRenderResponseRendered = (NotificationTemplateRenderResponseRendered) o;
    return Objects.equals(this.title, notificationTemplateRenderResponseRendered.title) &&
        Objects.equals(this.body, notificationTemplateRenderResponseRendered.body) &&
        Objects.equals(this.html, notificationTemplateRenderResponseRendered.html) &&
        Objects.equals(this.plainText, notificationTemplateRenderResponseRendered.plainText);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, body, html, plainText);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTemplateRenderResponseRendered {\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    plainText: ").append(toIndentedString(plainText)).append("\n");
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

