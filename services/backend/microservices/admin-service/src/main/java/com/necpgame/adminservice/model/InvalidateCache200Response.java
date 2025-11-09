package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * InvalidateCache200Response
 */

@JsonTypeName("invalidateCache_200_response")

public class InvalidateCache200Response {

  private @Nullable Integer invalidatedKeys;

  @Valid
  private List<String> layersAffected = new ArrayList<>();

  public InvalidateCache200Response invalidatedKeys(@Nullable Integer invalidatedKeys) {
    this.invalidatedKeys = invalidatedKeys;
    return this;
  }

  /**
   * Get invalidatedKeys
   * @return invalidatedKeys
   */
  
  @Schema(name = "invalidated_keys", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invalidated_keys")
  public @Nullable Integer getInvalidatedKeys() {
    return invalidatedKeys;
  }

  public void setInvalidatedKeys(@Nullable Integer invalidatedKeys) {
    this.invalidatedKeys = invalidatedKeys;
  }

  public InvalidateCache200Response layersAffected(List<String> layersAffected) {
    this.layersAffected = layersAffected;
    return this;
  }

  public InvalidateCache200Response addLayersAffectedItem(String layersAffectedItem) {
    if (this.layersAffected == null) {
      this.layersAffected = new ArrayList<>();
    }
    this.layersAffected.add(layersAffectedItem);
    return this;
  }

  /**
   * Get layersAffected
   * @return layersAffected
   */
  
  @Schema(name = "layers_affected", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("layers_affected")
  public List<String> getLayersAffected() {
    return layersAffected;
  }

  public void setLayersAffected(List<String> layersAffected) {
    this.layersAffected = layersAffected;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvalidateCache200Response invalidateCache200Response = (InvalidateCache200Response) o;
    return Objects.equals(this.invalidatedKeys, invalidateCache200Response.invalidatedKeys) &&
        Objects.equals(this.layersAffected, invalidateCache200Response.layersAffected);
  }

  @Override
  public int hashCode() {
    return Objects.hash(invalidatedKeys, layersAffected);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvalidateCache200Response {\n");
    sb.append("    invalidatedKeys: ").append(toIndentedString(invalidatedKeys)).append("\n");
    sb.append("    layersAffected: ").append(toIndentedString(layersAffected)).append("\n");
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

