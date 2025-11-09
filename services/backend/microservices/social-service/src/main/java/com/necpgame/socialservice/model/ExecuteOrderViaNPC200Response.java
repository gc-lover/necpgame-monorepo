package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ExecuteOrderViaNPC200Response
 */

@JsonTypeName("executeOrderViaNPC_200_response")

public class ExecuteOrderViaNPC200Response {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime estimatedCompletion;

  private @Nullable Float npcEfficiency;

  public ExecuteOrderViaNPC200Response estimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
    return this;
  }

  /**
   * Get estimatedCompletion
   * @return estimatedCompletion
   */
  @Valid 
  @Schema(name = "estimated_completion", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimated_completion")
  public @Nullable OffsetDateTime getEstimatedCompletion() {
    return estimatedCompletion;
  }

  public void setEstimatedCompletion(@Nullable OffsetDateTime estimatedCompletion) {
    this.estimatedCompletion = estimatedCompletion;
  }

  public ExecuteOrderViaNPC200Response npcEfficiency(@Nullable Float npcEfficiency) {
    this.npcEfficiency = npcEfficiency;
    return this;
  }

  /**
   * Get npcEfficiency
   * @return npcEfficiency
   */
  
  @Schema(name = "npc_efficiency", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("npc_efficiency")
  public @Nullable Float getNpcEfficiency() {
    return npcEfficiency;
  }

  public void setNpcEfficiency(@Nullable Float npcEfficiency) {
    this.npcEfficiency = npcEfficiency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExecuteOrderViaNPC200Response executeOrderViaNPC200Response = (ExecuteOrderViaNPC200Response) o;
    return Objects.equals(this.estimatedCompletion, executeOrderViaNPC200Response.estimatedCompletion) &&
        Objects.equals(this.npcEfficiency, executeOrderViaNPC200Response.npcEfficiency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(estimatedCompletion, npcEfficiency);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExecuteOrderViaNPC200Response {\n");
    sb.append("    estimatedCompletion: ").append(toIndentedString(estimatedCompletion)).append("\n");
    sb.append("    npcEfficiency: ").append(toIndentedString(npcEfficiency)).append("\n");
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

