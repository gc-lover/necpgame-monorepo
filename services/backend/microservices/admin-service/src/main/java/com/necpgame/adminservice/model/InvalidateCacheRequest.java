package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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
 * InvalidateCacheRequest
 */

@JsonTypeName("invalidateCache_request")

public class InvalidateCacheRequest {

  /**
   * Gets or Sets layer
   */
  public enum LayerEnum {
    L1_CDN("l1_cdn"),
    
    L2_REDIS("l2_redis"),
    
    L3_APP("l3_app"),
    
    ALL("all");

    private final String value;

    LayerEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static LayerEnum fromValue(String value) {
      for (LayerEnum b : LayerEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable LayerEnum layer;

  @Valid
  private List<String> keys = new ArrayList<>();

  private @Nullable String pattern;

  public InvalidateCacheRequest layer(@Nullable LayerEnum layer) {
    this.layer = layer;
    return this;
  }

  /**
   * Get layer
   * @return layer
   */
  
  @Schema(name = "layer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("layer")
  public @Nullable LayerEnum getLayer() {
    return layer;
  }

  public void setLayer(@Nullable LayerEnum layer) {
    this.layer = layer;
  }

  public InvalidateCacheRequest keys(List<String> keys) {
    this.keys = keys;
    return this;
  }

  public InvalidateCacheRequest addKeysItem(String keysItem) {
    if (this.keys == null) {
      this.keys = new ArrayList<>();
    }
    this.keys.add(keysItem);
    return this;
  }

  /**
   * Get keys
   * @return keys
   */
  
  @Schema(name = "keys", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keys")
  public List<String> getKeys() {
    return keys;
  }

  public void setKeys(List<String> keys) {
    this.keys = keys;
  }

  public InvalidateCacheRequest pattern(@Nullable String pattern) {
    this.pattern = pattern;
    return this;
  }

  /**
   * Pattern для инвалидации (regex)
   * @return pattern
   */
  
  @Schema(name = "pattern", description = "Pattern для инвалидации (regex)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("pattern")
  public @Nullable String getPattern() {
    return pattern;
  }

  public void setPattern(@Nullable String pattern) {
    this.pattern = pattern;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvalidateCacheRequest invalidateCacheRequest = (InvalidateCacheRequest) o;
    return Objects.equals(this.layer, invalidateCacheRequest.layer) &&
        Objects.equals(this.keys, invalidateCacheRequest.keys) &&
        Objects.equals(this.pattern, invalidateCacheRequest.pattern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(layer, keys, pattern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InvalidateCacheRequest {\n");
    sb.append("    layer: ").append(toIndentedString(layer)).append("\n");
    sb.append("    keys: ").append(toIndentedString(keys)).append("\n");
    sb.append("    pattern: ").append(toIndentedString(pattern)).append("\n");
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

