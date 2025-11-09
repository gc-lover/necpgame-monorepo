package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.ChainStage;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * UpdateFactionChainProgress200Response
 */

@JsonTypeName("updateFactionChainProgress_200_response")

public class UpdateFactionChainProgress200Response {

  private @Nullable ChainStage stage;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    
    COMPLETED("COMPLETED"),
    
    FAILED("FAILED");

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

  public UpdateFactionChainProgress200Response stage(@Nullable ChainStage stage) {
    this.stage = stage;
    return this;
  }

  /**
   * Get stage
   * @return stage
   */
  @Valid 
  @Schema(name = "stage", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("stage")
  public @Nullable ChainStage getStage() {
    return stage;
  }

  public void setStage(@Nullable ChainStage stage) {
    this.stage = stage;
  }

  public UpdateFactionChainProgress200Response status(@Nullable StatusEnum status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateFactionChainProgress200Response updateFactionChainProgress200Response = (UpdateFactionChainProgress200Response) o;
    return Objects.equals(this.stage, updateFactionChainProgress200Response.stage) &&
        Objects.equals(this.status, updateFactionChainProgress200Response.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stage, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateFactionChainProgress200Response {\n");
    sb.append("    stage: ").append(toIndentedString(stage)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

