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
 * CalculateDnDModifiersRequest
 */

@JsonTypeName("calculateDnDModifiers_request")

public class CalculateDnDModifiersRequest {

  private @Nullable Integer body;

  private @Nullable Integer reflex;

  private @Nullable Integer tech;

  private @Nullable Integer intelligence;

  private @Nullable Integer cool;

  public CalculateDnDModifiersRequest body(@Nullable Integer body) {
    this.body = body;
    return this;
  }

  /**
   * Get body
   * @return body
   */
  
  @Schema(name = "body", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("body")
  public @Nullable Integer getBody() {
    return body;
  }

  public void setBody(@Nullable Integer body) {
    this.body = body;
  }

  public CalculateDnDModifiersRequest reflex(@Nullable Integer reflex) {
    this.reflex = reflex;
    return this;
  }

  /**
   * Get reflex
   * @return reflex
   */
  
  @Schema(name = "reflex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reflex")
  public @Nullable Integer getReflex() {
    return reflex;
  }

  public void setReflex(@Nullable Integer reflex) {
    this.reflex = reflex;
  }

  public CalculateDnDModifiersRequest tech(@Nullable Integer tech) {
    this.tech = tech;
    return this;
  }

  /**
   * Get tech
   * @return tech
   */
  
  @Schema(name = "tech", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tech")
  public @Nullable Integer getTech() {
    return tech;
  }

  public void setTech(@Nullable Integer tech) {
    this.tech = tech;
  }

  public CalculateDnDModifiersRequest intelligence(@Nullable Integer intelligence) {
    this.intelligence = intelligence;
    return this;
  }

  /**
   * Get intelligence
   * @return intelligence
   */
  
  @Schema(name = "intelligence", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intelligence")
  public @Nullable Integer getIntelligence() {
    return intelligence;
  }

  public void setIntelligence(@Nullable Integer intelligence) {
    this.intelligence = intelligence;
  }

  public CalculateDnDModifiersRequest cool(@Nullable Integer cool) {
    this.cool = cool;
    return this;
  }

  /**
   * Get cool
   * @return cool
   */
  
  @Schema(name = "cool", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cool")
  public @Nullable Integer getCool() {
    return cool;
  }

  public void setCool(@Nullable Integer cool) {
    this.cool = cool;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CalculateDnDModifiersRequest calculateDnDModifiersRequest = (CalculateDnDModifiersRequest) o;
    return Objects.equals(this.body, calculateDnDModifiersRequest.body) &&
        Objects.equals(this.reflex, calculateDnDModifiersRequest.reflex) &&
        Objects.equals(this.tech, calculateDnDModifiersRequest.tech) &&
        Objects.equals(this.intelligence, calculateDnDModifiersRequest.intelligence) &&
        Objects.equals(this.cool, calculateDnDModifiersRequest.cool);
  }

  @Override
  public int hashCode() {
    return Objects.hash(body, reflex, tech, intelligence, cool);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CalculateDnDModifiersRequest {\n");
    sb.append("    body: ").append(toIndentedString(body)).append("\n");
    sb.append("    reflex: ").append(toIndentedString(reflex)).append("\n");
    sb.append("    tech: ").append(toIndentedString(tech)).append("\n");
    sb.append("    intelligence: ").append(toIndentedString(intelligence)).append("\n");
    sb.append("    cool: ").append(toIndentedString(cool)).append("\n");
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

