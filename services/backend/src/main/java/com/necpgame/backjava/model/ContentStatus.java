package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.ContentStatusSystemsReady;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ContentStatus
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ContentStatus {

  private @Nullable Boolean mvpContentReady;

  private @Nullable Integer totalQuestsAvailable;

  private @Nullable Integer totalLocationsAvailable;

  private @Nullable Integer totalNpcsAvailable;

  private @Nullable ContentStatusSystemsReady systemsReady;

  public ContentStatus mvpContentReady(@Nullable Boolean mvpContentReady) {
    this.mvpContentReady = mvpContentReady;
    return this;
  }

  /**
   * Get mvpContentReady
   * @return mvpContentReady
   */
  
  @Schema(name = "mvp_content_ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mvp_content_ready")
  public @Nullable Boolean getMvpContentReady() {
    return mvpContentReady;
  }

  public void setMvpContentReady(@Nullable Boolean mvpContentReady) {
    this.mvpContentReady = mvpContentReady;
  }

  public ContentStatus totalQuestsAvailable(@Nullable Integer totalQuestsAvailable) {
    this.totalQuestsAvailable = totalQuestsAvailable;
    return this;
  }

  /**
   * Get totalQuestsAvailable
   * @return totalQuestsAvailable
   */
  
  @Schema(name = "total_quests_available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_quests_available")
  public @Nullable Integer getTotalQuestsAvailable() {
    return totalQuestsAvailable;
  }

  public void setTotalQuestsAvailable(@Nullable Integer totalQuestsAvailable) {
    this.totalQuestsAvailable = totalQuestsAvailable;
  }

  public ContentStatus totalLocationsAvailable(@Nullable Integer totalLocationsAvailable) {
    this.totalLocationsAvailable = totalLocationsAvailable;
    return this;
  }

  /**
   * Get totalLocationsAvailable
   * @return totalLocationsAvailable
   */
  
  @Schema(name = "total_locations_available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_locations_available")
  public @Nullable Integer getTotalLocationsAvailable() {
    return totalLocationsAvailable;
  }

  public void setTotalLocationsAvailable(@Nullable Integer totalLocationsAvailable) {
    this.totalLocationsAvailable = totalLocationsAvailable;
  }

  public ContentStatus totalNpcsAvailable(@Nullable Integer totalNpcsAvailable) {
    this.totalNpcsAvailable = totalNpcsAvailable;
    return this;
  }

  /**
   * Get totalNpcsAvailable
   * @return totalNpcsAvailable
   */
  
  @Schema(name = "total_npcs_available", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("total_npcs_available")
  public @Nullable Integer getTotalNpcsAvailable() {
    return totalNpcsAvailable;
  }

  public void setTotalNpcsAvailable(@Nullable Integer totalNpcsAvailable) {
    this.totalNpcsAvailable = totalNpcsAvailable;
  }

  public ContentStatus systemsReady(@Nullable ContentStatusSystemsReady systemsReady) {
    this.systemsReady = systemsReady;
    return this;
  }

  /**
   * Get systemsReady
   * @return systemsReady
   */
  @Valid 
  @Schema(name = "systems_ready", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("systems_ready")
  public @Nullable ContentStatusSystemsReady getSystemsReady() {
    return systemsReady;
  }

  public void setSystemsReady(@Nullable ContentStatusSystemsReady systemsReady) {
    this.systemsReady = systemsReady;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentStatus contentStatus = (ContentStatus) o;
    return Objects.equals(this.mvpContentReady, contentStatus.mvpContentReady) &&
        Objects.equals(this.totalQuestsAvailable, contentStatus.totalQuestsAvailable) &&
        Objects.equals(this.totalLocationsAvailable, contentStatus.totalLocationsAvailable) &&
        Objects.equals(this.totalNpcsAvailable, contentStatus.totalNpcsAvailable) &&
        Objects.equals(this.systemsReady, contentStatus.systemsReady);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mvpContentReady, totalQuestsAvailable, totalLocationsAvailable, totalNpcsAvailable, systemsReady);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentStatus {\n");
    sb.append("    mvpContentReady: ").append(toIndentedString(mvpContentReady)).append("\n");
    sb.append("    totalQuestsAvailable: ").append(toIndentedString(totalQuestsAvailable)).append("\n");
    sb.append("    totalLocationsAvailable: ").append(toIndentedString(totalLocationsAvailable)).append("\n");
    sb.append("    totalNpcsAvailable: ").append(toIndentedString(totalNpcsAvailable)).append("\n");
    sb.append("    systemsReady: ").append(toIndentedString(systemsReady)).append("\n");
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

