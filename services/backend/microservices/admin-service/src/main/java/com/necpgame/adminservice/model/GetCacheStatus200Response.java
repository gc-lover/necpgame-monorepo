package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.necpgame.adminservice.model.CacheLayerStatus;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetCacheStatus200Response
 */

@JsonTypeName("getCacheStatus_200_response")

public class GetCacheStatus200Response {

  private @Nullable CacheLayerStatus l1Cdn;

  private @Nullable CacheLayerStatus l2Redis;

  private @Nullable CacheLayerStatus l3Application;

  public GetCacheStatus200Response l1Cdn(@Nullable CacheLayerStatus l1Cdn) {
    this.l1Cdn = l1Cdn;
    return this;
  }

  /**
   * Get l1Cdn
   * @return l1Cdn
   */
  @Valid 
  @Schema(name = "l1_cdn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("l1_cdn")
  public @Nullable CacheLayerStatus getL1Cdn() {
    return l1Cdn;
  }

  public void setL1Cdn(@Nullable CacheLayerStatus l1Cdn) {
    this.l1Cdn = l1Cdn;
  }

  public GetCacheStatus200Response l2Redis(@Nullable CacheLayerStatus l2Redis) {
    this.l2Redis = l2Redis;
    return this;
  }

  /**
   * Get l2Redis
   * @return l2Redis
   */
  @Valid 
  @Schema(name = "l2_redis", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("l2_redis")
  public @Nullable CacheLayerStatus getL2Redis() {
    return l2Redis;
  }

  public void setL2Redis(@Nullable CacheLayerStatus l2Redis) {
    this.l2Redis = l2Redis;
  }

  public GetCacheStatus200Response l3Application(@Nullable CacheLayerStatus l3Application) {
    this.l3Application = l3Application;
    return this;
  }

  /**
   * Get l3Application
   * @return l3Application
   */
  @Valid 
  @Schema(name = "l3_application", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("l3_application")
  public @Nullable CacheLayerStatus getL3Application() {
    return l3Application;
  }

  public void setL3Application(@Nullable CacheLayerStatus l3Application) {
    this.l3Application = l3Application;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetCacheStatus200Response getCacheStatus200Response = (GetCacheStatus200Response) o;
    return Objects.equals(this.l1Cdn, getCacheStatus200Response.l1Cdn) &&
        Objects.equals(this.l2Redis, getCacheStatus200Response.l2Redis) &&
        Objects.equals(this.l3Application, getCacheStatus200Response.l3Application);
  }

  @Override
  public int hashCode() {
    return Objects.hash(l1Cdn, l2Redis, l3Application);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetCacheStatus200Response {\n");
    sb.append("    l1Cdn: ").append(toIndentedString(l1Cdn)).append("\n");
    sb.append("    l2Redis: ").append(toIndentedString(l2Redis)).append("\n");
    sb.append("    l3Application: ").append(toIndentedString(l3Application)).append("\n");
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

