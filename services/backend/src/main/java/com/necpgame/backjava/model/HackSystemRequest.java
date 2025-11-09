package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * HackSystemRequest
 */

@JsonTypeName("hackSystem_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-06T20:50:35.859669800+03:00[Europe/Moscow]", comments = "Generator version: 7.17.0")
public class HackSystemRequest {

  private UUID characterId;

  private String targetId;

  /**
   * Gets or Sets method
   */
  public enum MethodEnum {
    BREACH("breach"),
    
    QUICKHACK("quickhack"),
    
    DAEMON("daemon");

    private final String value;

    MethodEnum(String value) {
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
    public static MethodEnum fromValue(String value) {
      for (MethodEnum b : MethodEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable MethodEnum method;

  public HackSystemRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HackSystemRequest(UUID characterId, String targetId) {
    this.characterId = characterId;
    this.targetId = targetId;
  }

  public HackSystemRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "characterId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("characterId")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public HackSystemRequest targetId(String targetId) {
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

  public HackSystemRequest method(@Nullable MethodEnum method) {
    this.method = method;
    return this;
  }

  /**
   * Get method
   * @return method
   */
  
  @Schema(name = "method", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("method")
  public @Nullable MethodEnum getMethod() {
    return method;
  }

  public void setMethod(@Nullable MethodEnum method) {
    this.method = method;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HackSystemRequest hackSystemRequest = (HackSystemRequest) o;
    return Objects.equals(this.characterId, hackSystemRequest.characterId) &&
        Objects.equals(this.targetId, hackSystemRequest.targetId) &&
        Objects.equals(this.method, hackSystemRequest.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, targetId, method);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackSystemRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    method: ").append(toIndentedString(method)).append("\n");
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


