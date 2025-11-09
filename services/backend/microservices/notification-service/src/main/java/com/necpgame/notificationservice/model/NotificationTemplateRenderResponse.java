package com.necpgame.notificationservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.notificationservice.model.NotificationTemplateRenderResponseRendered;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NotificationTemplateRenderResponse
 */


public class NotificationTemplateRenderResponse {

  private @Nullable NotificationTemplateRenderResponseRendered rendered;

  public NotificationTemplateRenderResponse rendered(@Nullable NotificationTemplateRenderResponseRendered rendered) {
    this.rendered = rendered;
    return this;
  }

  /**
   * Get rendered
   * @return rendered
   */
  @Valid 
  @Schema(name = "rendered", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rendered")
  public @Nullable NotificationTemplateRenderResponseRendered getRendered() {
    return rendered;
  }

  public void setRendered(@Nullable NotificationTemplateRenderResponseRendered rendered) {
    this.rendered = rendered;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NotificationTemplateRenderResponse notificationTemplateRenderResponse = (NotificationTemplateRenderResponse) o;
    return Objects.equals(this.rendered, notificationTemplateRenderResponse.rendered);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rendered);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NotificationTemplateRenderResponse {\n");
    sb.append("    rendered: ").append(toIndentedString(rendered)).append("\n");
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

