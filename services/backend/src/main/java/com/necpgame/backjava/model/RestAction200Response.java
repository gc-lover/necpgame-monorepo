package com.necpgame.backjava.model;

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
 * RestAction200Response
 */

@JsonTypeName("restAction_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:35.859669800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class RestAction200Response {

  private @Nullable Integer healthRestored;

  private @Nullable Integer energyRestored;

  private @Nullable Integer timePassed;

  public RestAction200Response healthRestored(@Nullable Integer healthRestored) {
    this.healthRestored = healthRestored;
    return this;
  }

  /**
   * Get healthRestored
   * @return healthRestored
   */
  
  @Schema(name = "healthRestored", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("healthRestored")
  public @Nullable Integer getHealthRestored() {
    return healthRestored;
  }

  public void setHealthRestored(@Nullable Integer healthRestored) {
    this.healthRestored = healthRestored;
  }

  public RestAction200Response energyRestored(@Nullable Integer energyRestored) {
    this.energyRestored = energyRestored;
    return this;
  }

  /**
   * Get energyRestored
   * @return energyRestored
   */
  
  @Schema(name = "energyRestored", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("energyRestored")
  public @Nullable Integer getEnergyRestored() {
    return energyRestored;
  }

  public void setEnergyRestored(@Nullable Integer energyRestored) {
    this.energyRestored = energyRestored;
  }

  public RestAction200Response timePassed(@Nullable Integer timePassed) {
    this.timePassed = timePassed;
    return this;
  }

  /**
   * Get timePassed
   * @return timePassed
   */
  
  @Schema(name = "timePassed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timePassed")
  public @Nullable Integer getTimePassed() {
    return timePassed;
  }

  public void setTimePassed(@Nullable Integer timePassed) {
    this.timePassed = timePassed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RestAction200Response restAction200Response = (RestAction200Response) o;
    return Objects.equals(this.healthRestored, restAction200Response.healthRestored) &&
        Objects.equals(this.energyRestored, restAction200Response.energyRestored) &&
        Objects.equals(this.timePassed, restAction200Response.timePassed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(healthRestored, energyRestored, timePassed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RestAction200Response {\n");
    sb.append("    healthRestored: ").append(toIndentedString(healthRestored)).append("\n");
    sb.append("    energyRestored: ").append(toIndentedString(energyRestored)).append("\n");
    sb.append("    timePassed: ").append(toIndentedString(timePassed)).append("\n");
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


