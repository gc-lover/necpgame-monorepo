package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationTemplate;
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
 * NotificationTemplatesResponse
 */


public class NotificationTemplatesResponse {

  @Valid
  private List<@Valid NotificationTemplate> templates = new ArrayList<>();

  public NotificationTemplatesResponse templates(List<@Valid NotificationTemplate> templates) {
    this.templates = templates;
    return this;
  }

  public NotificationTemplatesResponse addTemplatesItem(NotificationTemplate templatesItem) {
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
  public List<@Valid NotificationTemplate> getTemplates() {
    return templates;
  }

  public void setTemplates(List<@Valid NotificationTemplate> templates) {
    this.templates = templates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTemplatesResponse notificationTemplatesResponse = (NotificationTemplatesResponse) o;
    return Objects.equals(this.templates, notificationTemplatesResponse.templates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTemplatesResponse {\n");
    sb.append("    templates: ").append(toIndentedString(templates)).append("\n");
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

