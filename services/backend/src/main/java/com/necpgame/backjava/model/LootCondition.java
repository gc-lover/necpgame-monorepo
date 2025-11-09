package com.necpgame.backjava.model;

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
 * LootCondition
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class LootCondition {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FACTION("FACTION"),
    
    QUEST_STATE("QUEST_STATE"),
    
    EVENT_FLAG("EVENT_FLAG"),
    
    PARTY_SIZE("PARTY_SIZE"),
    
    ROLE("ROLE"),
    
    DIFFICULTY("DIFFICULTY");

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

  private @Nullable String value;

  /**
   * Gets or Sets operator
   */
  public enum OperatorEnum {
    EQUALS("EQUALS"),
    
    NOT_EQUALS("NOT_EQUALS"),
    
    GREATER("GREATER"),
    
    LESS("LESS"),
    
    BETWEEN("BETWEEN"),
    
    IN("IN");

    private final String value;

    OperatorEnum(String value) {
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
    public static OperatorEnum fromValue(String value) {
      for (OperatorEnum b : OperatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable OperatorEnum operator;

  public LootCondition() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LootCondition(TypeEnum type) {
    this.type = type;
  }

  public LootCondition type(TypeEnum type) {
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

  public LootCondition value(@Nullable String value) {
    this.value = value;
    return this;
  }

  /**
   * Get value
   * @return value
   */
  
  @Schema(name = "value", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("value")
  public @Nullable String getValue() {
    return value;
  }

  public void setValue(@Nullable String value) {
    this.value = value;
  }

  public LootCondition operator(@Nullable OperatorEnum operator) {
    this.operator = operator;
    return this;
  }

  /**
   * Get operator
   * @return operator
   */
  
  @Schema(name = "operator", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operator")
  public @Nullable OperatorEnum getOperator() {
    return operator;
  }

  public void setOperator(@Nullable OperatorEnum operator) {
    this.operator = operator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LootCondition lootCondition = (LootCondition) o;
    return Objects.equals(this.type, lootCondition.type) &&
        Objects.equals(this.value, lootCondition.value) &&
        Objects.equals(this.operator, lootCondition.operator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, value, operator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LootCondition {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
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

