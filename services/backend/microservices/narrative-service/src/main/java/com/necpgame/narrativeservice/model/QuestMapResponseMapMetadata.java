package com.necpgame.narrativeservice.model;

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
 * QuestMapResponseMapMetadata
 */

@JsonTypeName("QuestMapResponse_map_metadata")

public class QuestMapResponseMapMetadata {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastUpdated;

  private @Nullable String version;

  public QuestMapResponseMapMetadata lastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    return this;
  }

  /**
   * Get lastUpdated
   * @return lastUpdated
   */
  @Valid 
  @Schema(name = "last_updated", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("last_updated")
  public @Nullable OffsetDateTime getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(@Nullable OffsetDateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public QuestMapResponseMapMetadata version(@Nullable String version) {
    this.version = version;
    return this;
  }

  /**
   * Get version
   * @return version
   */
  
  @Schema(name = "version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("version")
  public @Nullable String getVersion() {
    return version;
  }

  public void setVersion(@Nullable String version) {
    this.version = version;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QuestMapResponseMapMetadata questMapResponseMapMetadata = (QuestMapResponseMapMetadata) o;
    return Objects.equals(this.lastUpdated, questMapResponseMapMetadata.lastUpdated) &&
        Objects.equals(this.version, questMapResponseMapMetadata.version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lastUpdated, version);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class QuestMapResponseMapMetadata {\n");
    sb.append("    lastUpdated: ").append(toIndentedString(lastUpdated)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
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

