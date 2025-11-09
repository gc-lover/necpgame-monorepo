package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.DamagePacket;
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
 * DamagePreviewResponse
 */


public class DamagePreviewResponse {

  @Valid
  private List<@Valid DamagePacket> previewPackets = new ArrayList<>();

  private @Nullable Integer estimatedTimeMs;

  public DamagePreviewResponse previewPackets(List<@Valid DamagePacket> previewPackets) {
    this.previewPackets = previewPackets;
    return this;
  }

  public DamagePreviewResponse addPreviewPacketsItem(DamagePacket previewPacketsItem) {
    if (this.previewPackets == null) {
      this.previewPackets = new ArrayList<>();
    }
    this.previewPackets.add(previewPacketsItem);
    return this;
  }

  /**
   * Get previewPackets
   * @return previewPackets
   */
  @Valid 
  @Schema(name = "previewPackets", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("previewPackets")
  public List<@Valid DamagePacket> getPreviewPackets() {
    return previewPackets;
  }

  public void setPreviewPackets(List<@Valid DamagePacket> previewPackets) {
    this.previewPackets = previewPackets;
  }

  public DamagePreviewResponse estimatedTimeMs(@Nullable Integer estimatedTimeMs) {
    this.estimatedTimeMs = estimatedTimeMs;
    return this;
  }

  /**
   * Get estimatedTimeMs
   * @return estimatedTimeMs
   */
  
  @Schema(name = "estimatedTimeMs", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("estimatedTimeMs")
  public @Nullable Integer getEstimatedTimeMs() {
    return estimatedTimeMs;
  }

  public void setEstimatedTimeMs(@Nullable Integer estimatedTimeMs) {
    this.estimatedTimeMs = estimatedTimeMs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DamagePreviewResponse damagePreviewResponse = (DamagePreviewResponse) o;
    return Objects.equals(this.previewPackets, damagePreviewResponse.previewPackets) &&
        Objects.equals(this.estimatedTimeMs, damagePreviewResponse.estimatedTimeMs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previewPackets, estimatedTimeMs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DamagePreviewResponse {\n");
    sb.append("    previewPackets: ").append(toIndentedString(previewPackets)).append("\n");
    sb.append("    estimatedTimeMs: ").append(toIndentedString(estimatedTimeMs)).append("\n");
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

