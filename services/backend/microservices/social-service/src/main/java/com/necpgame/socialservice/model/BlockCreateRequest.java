package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * BlockCreateRequest
 */


public class BlockCreateRequest {

  private String targetId;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    BLOCK("BLOCK"),
    
    MUTE("MUTE");

    private final String value;

    TypeEnum(String value) {
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
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private @Nullable String reason;

  private @Nullable Integer durationMinutes;

  public BlockCreateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public BlockCreateRequest(String targetId, TypeEnum type) {
    this.targetId = targetId;
    this.type = type;
  }

  public BlockCreateRequest targetId(String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  @NotNull 
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("targetId")
  public String getTargetId() {
    return targetId;
  }

  public void setTargetId(String targetId) {
    this.targetId = targetId;
  }

  public BlockCreateRequest type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  @NotNull 
  @Schema(name = "type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public BlockCreateRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public BlockCreateRequest durationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
    return this;
  }

  /**
   * Get durationMinutes
   * @return durationMinutes
   */
  
  @Schema(name = "durationMinutes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("durationMinutes")
  public @Nullable Integer getDurationMinutes() {
    return durationMinutes;
  }

  public void setDurationMinutes(@Nullable Integer durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BlockCreateRequest blockCreateRequest = (BlockCreateRequest) o;
    return Objects.equals(this.targetId, blockCreateRequest.targetId) &&
        Objects.equals(this.type, blockCreateRequest.type) &&
        Objects.equals(this.reason, blockCreateRequest.reason) &&
        Objects.equals(this.durationMinutes, blockCreateRequest.durationMinutes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetId, type, reason, durationMinutes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BlockCreateRequest {\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    durationMinutes: ").append(toIndentedString(durationMinutes)).append("\n");
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

