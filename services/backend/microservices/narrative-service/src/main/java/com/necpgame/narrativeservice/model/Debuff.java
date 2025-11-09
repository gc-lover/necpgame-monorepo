package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Debuff
 */


public class Debuff {

  private @Nullable String id;

  private @Nullable Integer durationSeconds;

  private @Nullable String descriptionKey;

  public Debuff id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  public void setId(@Nullable String id) {
    this.id = id;
  }

  public Debuff durationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
    return this;
  }

  /**
   * Get durationSeconds
   * @return durationSeconds
   */
  
  @Schema(name = "durationSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationSeconds")
  public @Nullable Integer getDurationSeconds() {
    return durationSeconds;
  }

  public void setDurationSeconds(@Nullable Integer durationSeconds) {
    this.durationSeconds = durationSeconds;
  }

  public Debuff descriptionKey(@Nullable String descriptionKey) {
    this.descriptionKey = descriptionKey;
    return this;
  }

  /**
   * Get descriptionKey
   * @return descriptionKey
   */
  
  @Schema(name = "descriptionKey", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("descriptionKey")
  public @Nullable String getDescriptionKey() {
    return descriptionKey;
  }

  public void setDescriptionKey(@Nullable String descriptionKey) {
    this.descriptionKey = descriptionKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Debuff debuff = (Debuff) o;
    return Objects.equals(this.id, debuff.id) &&
        Objects.equals(this.durationSeconds, debuff.durationSeconds) &&
        Objects.equals(this.descriptionKey, debuff.descriptionKey);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, durationSeconds, descriptionKey);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Debuff {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    durationSeconds: ").append(toIndentedString(durationSeconds)).append("\n");
    sb.append("    descriptionKey: ").append(toIndentedString(descriptionKey)).append("\n");
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

