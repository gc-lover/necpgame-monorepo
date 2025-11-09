package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.XpSourcesResponseSourcesInner;
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
 * XpSourcesResponse
 */


public class XpSourcesResponse {

  @Valid
  private List<@Valid XpSourcesResponseSourcesInner> sources = new ArrayList<>();

  public XpSourcesResponse sources(List<@Valid XpSourcesResponseSourcesInner> sources) {
    this.sources = sources;
    return this;
  }

  public XpSourcesResponse addSourcesItem(XpSourcesResponseSourcesInner sourcesItem) {
    if (this.sources == null) {
      this.sources = new ArrayList<>();
    }
    this.sources.add(sourcesItem);
    return this;
  }

  /**
   * Get sources
   * @return sources
   */
  @Valid 
  @Schema(name = "sources", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sources")
  public List<@Valid XpSourcesResponseSourcesInner> getSources() {
    return sources;
  }

  public void setSources(List<@Valid XpSourcesResponseSourcesInner> sources) {
    this.sources = sources;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    XpSourcesResponse xpSourcesResponse = (XpSourcesResponse) o;
    return Objects.equals(this.sources, xpSourcesResponse.sources);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sources);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class XpSourcesResponse {\n");
    sb.append("    sources: ").append(toIndentedString(sources)).append("\n");
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

