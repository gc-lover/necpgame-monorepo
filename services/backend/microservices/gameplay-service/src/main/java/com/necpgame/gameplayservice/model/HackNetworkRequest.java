package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * HackNetworkRequest
 */

@JsonTypeName("hackNetwork_request")

public class HackNetworkRequest {

  private String characterId;

  private @Nullable String entryPoint;

  /**
   * Gets or Sets method
   */
  public enum MethodEnum {
    BRUTE_FORCE("brute_force"),
    
    STEALTH("stealth"),
    
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

  public HackNetworkRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public HackNetworkRequest(String characterId) {
    this.characterId = characterId;
  }

  public HackNetworkRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public HackNetworkRequest entryPoint(@Nullable String entryPoint) {
    this.entryPoint = entryPoint;
    return this;
  }

  /**
   * ID узла для входа в сеть
   * @return entryPoint
   */
  
  @Schema(name = "entry_point", description = "ID узла для входа в сеть", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("entry_point")
  public @Nullable String getEntryPoint() {
    return entryPoint;
  }

  public void setEntryPoint(@Nullable String entryPoint) {
    this.entryPoint = entryPoint;
  }

  public HackNetworkRequest method(@Nullable MethodEnum method) {
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
    HackNetworkRequest hackNetworkRequest = (HackNetworkRequest) o;
    return Objects.equals(this.characterId, hackNetworkRequest.characterId) &&
        Objects.equals(this.entryPoint, hackNetworkRequest.entryPoint) &&
        Objects.equals(this.method, hackNetworkRequest.method);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, entryPoint, method);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HackNetworkRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    entryPoint: ").append(toIndentedString(entryPoint)).append("\n");
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

