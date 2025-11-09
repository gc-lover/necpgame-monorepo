package com.necpgame.worldservice.model;

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
 * ChronicleEventLinksInner
 */

@JsonTypeName("ChronicleEvent_links_inner")

public class ChronicleEventLinksInner {

  private @Nullable String label;

  private @Nullable String href;

  public ChronicleEventLinksInner label(@Nullable String label) {
    this.label = label;
    return this;
  }

  /**
   * Get label
   * @return label
   */
  
  @Schema(name = "label", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("label")
  public @Nullable String getLabel() {
    return label;
  }

  public void setLabel(@Nullable String label) {
    this.label = label;
  }

  public ChronicleEventLinksInner href(@Nullable String href) {
    this.href = href;
    return this;
  }

  /**
   * Get href
   * @return href
   */
  
  @Schema(name = "href", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable String getHref() {
    return href;
  }

  public void setHref(@Nullable String href) {
    this.href = href;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChronicleEventLinksInner chronicleEventLinksInner = (ChronicleEventLinksInner) o;
    return Objects.equals(this.label, chronicleEventLinksInner.label) &&
        Objects.equals(this.href, chronicleEventLinksInner.href);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, href);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChronicleEventLinksInner {\n");
    sb.append("    label: ").append(toIndentedString(label)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
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

