package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
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
 * StateOperationRequest
 */


public class StateOperationRequest {

  private UUID characterId;

  /**
   * Gets or Sets operationType
   */
  public enum OperationTypeEnum {
    INCREMENT("INCREMENT"),
    
    DECREMENT("DECREMENT"),
    
    SET("SET"),
    
    APPEND("APPEND"),
    
    REMOVE("REMOVE");

    private final String value;

    OperationTypeEnum(String value) {
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
    public static OperationTypeEnum fromValue(String value) {
      for (OperationTypeEnum b : OperationTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private OperationTypeEnum operationType;

  private @Nullable String field;

  private @Nullable Object value;

  private @Nullable Integer expectedVersion;

  public StateOperationRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public StateOperationRequest(UUID characterId, OperationTypeEnum operationType) {
    this.characterId = characterId;
    this.operationType = operationType;
  }

  public StateOperationRequest characterId(UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(UUID characterId) {
    this.characterId = characterId;
  }

  public StateOperationRequest operationType(OperationTypeEnum operationType) {
    this.operationType = operationType;
    return this;
  }

  /**
   * Get operationType
   * @return operationType
   */
  @NotNull 
  @Schema(name = "operation_type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operation_type")
  public OperationTypeEnum getOperationType() {
    return operationType;
  }

  public void setOperationType(OperationTypeEnum operationType) {
    this.operationType = operationType;
  }

  public StateOperationRequest field(@Nullable String field) {
    this.field = field;
    return this;
  }

  /**
   * Get field
   * @return field
   */
  
  @Schema(name = "field", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("field")
  public @Nullable String getField() {
    return field;
  }

  public void setField(@Nullable String field) {
    this.field = field;
  }

  public StateOperationRequest value(@Nullable Object value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable Object getValue() {
    return value;
  }

  public void setValue(@Nullable Object value) {
    this.value = value;
  }

  public StateOperationRequest expectedVersion(@Nullable Integer expectedVersion) {
    this.expectedVersion = expectedVersion;
    return this;
  }

  /**
   * Get expectedVersion
   * @return expectedVersion
   */
  
  @Schema(name = "expected_version", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expected_version")
  public @Nullable Integer getExpectedVersion() {
    return expectedVersion;
  }

  public void setExpectedVersion(@Nullable Integer expectedVersion) {
    this.expectedVersion = expectedVersion;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StateOperationRequest stateOperationRequest = (StateOperationRequest) o;
    return Objects.equals(this.characterId, stateOperationRequest.characterId) &&
        Objects.equals(this.operationType, stateOperationRequest.operationType) &&
        Objects.equals(this.field, stateOperationRequest.field) &&
        Objects.equals(this.value, stateOperationRequest.value) &&
        Objects.equals(this.expectedVersion, stateOperationRequest.expectedVersion);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, operationType, field, value, expectedVersion);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StateOperationRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    operationType: ").append(toIndentedString(operationType)).append("\n");
    sb.append("    field: ").append(toIndentedString(field)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    expectedVersion: ").append(toIndentedString(expectedVersion)).append("\n");
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

