package com.necpgame.gameplayservice.model;

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
 * NavigateToCyberspaceZone200Response
 */

@JsonTypeName("navigateToCyberspaceZone_200_response")

public class NavigateToCyberspaceZone200Response {

  private @Nullable Boolean success;

  private @Nullable String zoneId;

  private @Nullable Object zoneInfo;

  public NavigateToCyberspaceZone200Response success(@Nullable Boolean success) {
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

  public NavigateToCyberspaceZone200Response zoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
    return this;
  }

  /**
   * Get zoneId
   * @return zoneId
   */
  
  @Schema(name = "zone_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_id")
  public @Nullable String getZoneId() {
    return zoneId;
  }

  public void setZoneId(@Nullable String zoneId) {
    this.zoneId = zoneId;
  }

  public NavigateToCyberspaceZone200Response zoneInfo(@Nullable Object zoneInfo) {
    this.zoneInfo = zoneInfo;
    return this;
  }

  /**
   * Get zoneInfo
   * @return zoneInfo
   */
  
  @Schema(name = "zone_info", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("zone_info")
  public @Nullable Object getZoneInfo() {
    return zoneInfo;
  }

  public void setZoneInfo(@Nullable Object zoneInfo) {
    this.zoneInfo = zoneInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NavigateToCyberspaceZone200Response navigateToCyberspaceZone200Response = (NavigateToCyberspaceZone200Response) o;
    return Objects.equals(this.success, navigateToCyberspaceZone200Response.success) &&
        Objects.equals(this.zoneId, navigateToCyberspaceZone200Response.zoneId) &&
        Objects.equals(this.zoneInfo, navigateToCyberspaceZone200Response.zoneInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, zoneId, zoneInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NavigateToCyberspaceZone200Response {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneId)).append("\n");
    sb.append("    zoneInfo: ").append(toIndentedString(zoneInfo)).append("\n");
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

