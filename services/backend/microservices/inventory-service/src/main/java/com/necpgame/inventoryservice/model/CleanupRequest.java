package com.necpgame.inventoryservice.model;

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
 * CleanupRequest
 */


public class CleanupRequest {

  private @Nullable String rarityThreshold;

  private @Nullable Boolean dryRun;

  public CleanupRequest rarityThreshold(@Nullable String rarityThreshold) {
    this.rarityThreshold = rarityThreshold;
    return this;
  }

  /**
   * Get rarityThreshold
   * @return rarityThreshold
   */
  
  @Schema(name = "rarityThreshold", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rarityThreshold")
  public @Nullable String getRarityThreshold() {
    return rarityThreshold;
  }

  public void setRarityThreshold(@Nullable String rarityThreshold) {
    this.rarityThreshold = rarityThreshold;
  }

  public CleanupRequest dryRun(@Nullable Boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  /**
   * Get dryRun
   * @return dryRun
   */
  
  @Schema(name = "dryRun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("dryRun")
  public @Nullable Boolean getDryRun() {
    return dryRun;
  }

  public void setDryRun(@Nullable Boolean dryRun) {
    this.dryRun = dryRun;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CleanupRequest cleanupRequest = (CleanupRequest) o;
    return Objects.equals(this.rarityThreshold, cleanupRequest.rarityThreshold) &&
        Objects.equals(this.dryRun, cleanupRequest.dryRun);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rarityThreshold, dryRun);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CleanupRequest {\n");
    sb.append("    rarityThreshold: ").append(toIndentedString(rarityThreshold)).append("\n");
    sb.append("    dryRun: ").append(toIndentedString(dryRun)).append("\n");
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

