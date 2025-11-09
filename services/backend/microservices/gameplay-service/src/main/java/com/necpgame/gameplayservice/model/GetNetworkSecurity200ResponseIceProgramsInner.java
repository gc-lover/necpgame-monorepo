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
 * GetNetworkSecurity200ResponseIceProgramsInner
 */

@JsonTypeName("getNetworkSecurity_200_response_ice_programs_inner")

public class GetNetworkSecurity200ResponseIceProgramsInner {

  private @Nullable String iceId;

  private @Nullable String type;

  private @Nullable Integer threatLevel;

  public GetNetworkSecurity200ResponseIceProgramsInner iceId(@Nullable String iceId) {
    this.iceId = iceId;
    return this;
  }

  /**
   * Get iceId
   * @return iceId
   */
  
  @Schema(name = "ice_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ice_id")
  public @Nullable String getIceId() {
    return iceId;
  }

  public void setIceId(@Nullable String iceId) {
    this.iceId = iceId;
  }

  public GetNetworkSecurity200ResponseIceProgramsInner type(@Nullable String type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public @Nullable String getType() {
    return type;
  }

  public void setType(@Nullable String type) {
    this.type = type;
  }

  public GetNetworkSecurity200ResponseIceProgramsInner threatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
    return this;
  }

  /**
   * Get threatLevel
   * @return threatLevel
   */
  
  @Schema(name = "threat_level", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("threat_level")
  public @Nullable Integer getThreatLevel() {
    return threatLevel;
  }

  public void setThreatLevel(@Nullable Integer threatLevel) {
    this.threatLevel = threatLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetNetworkSecurity200ResponseIceProgramsInner getNetworkSecurity200ResponseIceProgramsInner = (GetNetworkSecurity200ResponseIceProgramsInner) o;
    return Objects.equals(this.iceId, getNetworkSecurity200ResponseIceProgramsInner.iceId) &&
        Objects.equals(this.type, getNetworkSecurity200ResponseIceProgramsInner.type) &&
        Objects.equals(this.threatLevel, getNetworkSecurity200ResponseIceProgramsInner.threatLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iceId, type, threatLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetNetworkSecurity200ResponseIceProgramsInner {\n");
    sb.append("    iceId: ").append(toIndentedString(iceId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    threatLevel: ").append(toIndentedString(threatLevel)).append("\n");
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

