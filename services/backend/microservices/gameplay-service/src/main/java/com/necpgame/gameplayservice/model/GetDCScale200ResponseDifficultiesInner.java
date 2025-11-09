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
 * GetDCScale200ResponseDifficultiesInner
 */

@JsonTypeName("getDCScale_200_response_difficulties_inner")

public class GetDCScale200ResponseDifficultiesInner {

  private @Nullable String name;

  private @Nullable Integer dc;

  private @Nullable String description;

  public GetDCScale200ResponseDifficultiesInner name(@Nullable String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
   */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public @Nullable String getName() {
    return name;
  }

  public void setName(@Nullable String name) {
    this.name = name;
  }

  public GetDCScale200ResponseDifficultiesInner dc(@Nullable Integer dc) {
    this.dc = dc;
    return this;
  }

  /**
   * Get dc
   * @return dc
   */
  
  @Schema(name = "dc", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dc")
  public @Nullable Integer getDc() {
    return dc;
  }

  public void setDc(@Nullable Integer dc) {
    this.dc = dc;
  }

  public GetDCScale200ResponseDifficultiesInner description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   * @return description
   */
  
  @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetDCScale200ResponseDifficultiesInner getDCScale200ResponseDifficultiesInner = (GetDCScale200ResponseDifficultiesInner) o;
    return Objects.equals(this.name, getDCScale200ResponseDifficultiesInner.name) &&
        Objects.equals(this.dc, getDCScale200ResponseDifficultiesInner.dc) &&
        Objects.equals(this.description, getDCScale200ResponseDifficultiesInner.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, dc, description);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetDCScale200ResponseDifficultiesInner {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    dc: ").append(toIndentedString(dc)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
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

