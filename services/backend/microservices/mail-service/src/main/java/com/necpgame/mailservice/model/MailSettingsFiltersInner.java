package com.necpgame.mailservice.model;

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
 * MailSettingsFiltersInner
 */

@JsonTypeName("MailSettings_filters_inner")

public class MailSettingsFiltersInner {

  private @Nullable String filterId;

  private @Nullable String type;

  private @Nullable String value;

  public MailSettingsFiltersInner filterId(@Nullable String filterId) {
    this.filterId = filterId;
    return this;
  }

  /**
   * Get filterId
   * @return filterId
   */
  
  @Schema(name = "filterId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("filterId")
  public @Nullable String getFilterId() {
    return filterId;
  }

  public void setFilterId(@Nullable String filterId) {
    this.filterId = filterId;
  }

  public MailSettingsFiltersInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public MailSettingsFiltersInner value(@Nullable String value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable String getValue() {
    return value;
  }

  public void setValue(@Nullable String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MailSettingsFiltersInner mailSettingsFiltersInner = (MailSettingsFiltersInner) o;
    return Objects.equals(this.filterId, mailSettingsFiltersInner.filterId) &&
        Objects.equals(this.type, mailSettingsFiltersInner.type) &&
        Objects.equals(this.value, mailSettingsFiltersInner.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(filterId, type, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MailSettingsFiltersInner {\n");
    sb.append("    filterId: ").append(toIndentedString(filterId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

