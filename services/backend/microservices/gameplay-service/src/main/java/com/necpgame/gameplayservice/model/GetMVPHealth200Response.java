package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.GetMVPHealth200ResponseSystems;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetMVPHealth200Response
 */

@JsonTypeName("getMVPHealth_200_response")

public class GetMVPHealth200Response {

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    HEALTHY("HEALTHY"),
    
    DEGRADED("DEGRADED"),
    
    DOWN("DOWN");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  private @Nullable GetMVPHealth200ResponseSystems systems;

  public GetMVPHealth200Response status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public GetMVPHealth200Response systems(@Nullable GetMVPHealth200ResponseSystems systems) {
    this.systems = systems;
    return this;
  }

  /**
   * Get systems
   * @return systems
   */
  @Valid 
  @Schema(name = "systems", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("systems")
  public @Nullable GetMVPHealth200ResponseSystems getSystems() {
    return systems;
  }

  public void setSystems(@Nullable GetMVPHealth200ResponseSystems systems) {
    this.systems = systems;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetMVPHealth200Response getMVPHealth200Response = (GetMVPHealth200Response) o;
    return Objects.equals(this.status, getMVPHealth200Response.status) &&
        Objects.equals(this.systems, getMVPHealth200Response.systems);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, systems);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetMVPHealth200Response {\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    systems: ").append(toIndentedString(systems)).append("\n");
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

