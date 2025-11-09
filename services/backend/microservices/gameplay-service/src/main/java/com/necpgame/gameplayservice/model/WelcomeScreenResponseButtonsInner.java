package com.necpgame.gameplayservice.model;

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
 * WelcomeScreenResponseButtonsInner
 */

@JsonTypeName("WelcomeScreenResponse_buttons_inner")

public class WelcomeScreenResponseButtonsInner {

  private @Nullable String id;

  private @Nullable String label;

  public WelcomeScreenResponseButtonsInner id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * ID кнопки
   * @return id
   */
  
  @Schema(name = "id", description = "ID кнопки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public WelcomeScreenResponseButtonsInner label(@Nullable String label) {
    this.label = label;
    return this;
  }

  /**
   * Текст кнопки
   * @return label
   */
  
  @Schema(name = "label", description = "Текст кнопки", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("label")
  public @Nullable String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeScreenResponseButtonsInner welcomeScreenResponseButtonsInner = (WelcomeScreenResponseButtonsInner) o;
    return Objects.equals(this.id, welcomeScreenResponseButtonsInner.id) &&
        Objects.equals(this.label, welcomeScreenResponseButtonsInner.label);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, label);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WelcomeScreenResponseButtonsInner {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
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

