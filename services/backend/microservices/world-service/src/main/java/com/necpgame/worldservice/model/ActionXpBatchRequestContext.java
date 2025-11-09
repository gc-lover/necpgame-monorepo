package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActionXpBatchRequestContext
 */

@JsonTypeName("ActionXpBatchRequest_context")

public class ActionXpBatchRequestContext {

  private @Nullable String source;

  private @Nullable String location;

  private @Nullable BigDecimal fatigueSoftCap;

  public ActionXpBatchRequestContext source(@Nullable String source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  
  @Schema(name = "source", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("source")
  public @Nullable String getSource() {
    return source;
  }

  public void setSource(@Nullable String source) {
    this.source = source;
  }

  public ActionXpBatchRequestContext location(@Nullable String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   * @return location
   */
  
  @Schema(name = "location", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("location")
  public @Nullable String getLocation() {
    return location;
  }

  public void setLocation(@Nullable String location) {
    this.location = location;
  }

  public ActionXpBatchRequestContext fatigueSoftCap(@Nullable BigDecimal fatigueSoftCap) {
    this.fatigueSoftCap = fatigueSoftCap;
    return this;
  }

  /**
   * Get fatigueSoftCap
   * @return fatigueSoftCap
   */
  @Valid 
  @Schema(name = "fatigueSoftCap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fatigueSoftCap")
  public @Nullable BigDecimal getFatigueSoftCap() {
    return fatigueSoftCap;
  }

  public void setFatigueSoftCap(@Nullable BigDecimal fatigueSoftCap) {
    this.fatigueSoftCap = fatigueSoftCap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActionXpBatchRequestContext actionXpBatchRequestContext = (ActionXpBatchRequestContext) o;
    return Objects.equals(this.source, actionXpBatchRequestContext.source) &&
        Objects.equals(this.location, actionXpBatchRequestContext.location) &&
        Objects.equals(this.fatigueSoftCap, actionXpBatchRequestContext.fatigueSoftCap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, location, fatigueSoftCap);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActionXpBatchRequestContext {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    fatigueSoftCap: ").append(toIndentedString(fatigueSoftCap)).append("\n");
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

