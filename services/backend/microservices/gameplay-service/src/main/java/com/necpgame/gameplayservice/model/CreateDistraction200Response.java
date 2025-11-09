package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
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
 * CreateDistraction200Response
 */

@JsonTypeName("createDistraction_200_response")

public class CreateDistraction200Response {

  private @Nullable Boolean success;

  private @Nullable String distractionId;

  private @Nullable BigDecimal duration;

  @Valid
  private List<String> affectedEnemies = new ArrayList<>();

  public CreateDistraction200Response success(@Nullable Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
   */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public @Nullable Boolean getSuccess() {
    return success;
  }

  public void setSuccess(@Nullable Boolean success) {
    this.success = success;
  }

  public CreateDistraction200Response distractionId(@Nullable String distractionId) {
    this.distractionId = distractionId;
    return this;
  }

  /**
   * Get distractionId
   * @return distractionId
   */
  
  @Schema(name = "distraction_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("distraction_id")
  public @Nullable String getDistractionId() {
    return distractionId;
  }

  public void setDistractionId(@Nullable String distractionId) {
    this.distractionId = distractionId;
  }

  public CreateDistraction200Response duration(@Nullable BigDecimal duration) {
    this.duration = duration;
    return this;
  }

  /**
   * Длительность отвлечения (секунды)
   * @return duration
   */
  @Valid 
  @Schema(name = "duration", description = "Длительность отвлечения (секунды)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("duration")
  public @Nullable BigDecimal getDuration() {
    return duration;
  }

  public void setDuration(@Nullable BigDecimal duration) {
    this.duration = duration;
  }

  public CreateDistraction200Response affectedEnemies(List<String> affectedEnemies) {
    this.affectedEnemies = affectedEnemies;
    return this;
  }

  public CreateDistraction200Response addAffectedEnemiesItem(String affectedEnemiesItem) {
    if (this.affectedEnemies == null) {
      this.affectedEnemies = new ArrayList<>();
    }
    this.affectedEnemies.add(affectedEnemiesItem);
    return this;
  }

  /**
   * Get affectedEnemies
   * @return affectedEnemies
   */
  
  @Schema(name = "affected_enemies", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("affected_enemies")
  public List<String> getAffectedEnemies() {
    return affectedEnemies;
  }

  public void setAffectedEnemies(List<String> affectedEnemies) {
    this.affectedEnemies = affectedEnemies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateDistraction200Response createDistraction200Response = (CreateDistraction200Response) o;
    return Objects.equals(this.success, createDistraction200Response.success) &&
        Objects.equals(this.distractionId, createDistraction200Response.distractionId) &&
        Objects.equals(this.duration, createDistraction200Response.duration) &&
        Objects.equals(this.affectedEnemies, createDistraction200Response.affectedEnemies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, distractionId, duration, affectedEnemies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateDistraction200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    distractionId: ").append(toIndentedString(distractionId)).append("\n");
    sb.append("    duration: ").append(toIndentedString(duration)).append("\n");
    sb.append("    affectedEnemies: ").append(toIndentedString(affectedEnemies)).append("\n");
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

