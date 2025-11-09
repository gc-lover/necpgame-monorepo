package com.necpgame.worldservice.model;

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
 * ContractErrorAllOfError
 */

@JsonTypeName("ContractError_allOf_error")

public class ContractErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_FACTION_CONTRACT_LOCKED("BIZ_FACTION_CONTRACT_LOCKED"),
    
    BIZ_FACTION_CONTRACT_COOLDOWN("BIZ_FACTION_CONTRACT_COOLDOWN"),
    
    BIZ_FACTION_CONTRACT_NOT_FOUND("BIZ_FACTION_CONTRACT_NOT_FOUND"),
    
    BIZ_FACTION_BRANCH_NOT_AVAILABLE("BIZ_FACTION_BRANCH_NOT_AVAILABLE"),
    
    BIZ_FACTION_STAGE_INVALID("BIZ_FACTION_STAGE_INVALID"),
    
    BIZ_FACTION_OUTCOME_ALREADY_APPLIED("BIZ_FACTION_OUTCOME_ALREADY_APPLIED"),
    
    VAL_FACTION_REQUIREMENTS_FAILED("VAL_FACTION_REQUIREMENTS_FAILED"),
    
    VAL_FACTION_BRANCH_INVALID("VAL_FACTION_BRANCH_INVALID"),
    
    VAL_FACTION_PROGRESS_INVALID("VAL_FACTION_PROGRESS_INVALID"),
    
    INT_FACTION_SOCIAL_SERVICE_FAILURE("INT_FACTION_SOCIAL_SERVICE_FAILURE"),
    
    INT_FACTION_ECONOMY_SERVICE_FAILURE("INT_FACTION_ECONOMY_SERVICE_FAILURE"),
    
    INT_FACTION_ANALYTICS_FAILURE("INT_FACTION_ANALYTICS_FAILURE");

    private final String value;

    CodeEnum(String value) {
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
    public static CodeEnum fromValue(String value) {
      for (CodeEnum b : CodeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CodeEnum code;

  public ContractErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ContractErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public ContractErrorAllOfError code(CodeEnum code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  @NotNull 
  @Schema(name = "code", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("code")
  public CodeEnum getCode() {
    return code;
  }

  public void setCode(CodeEnum code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContractErrorAllOfError contractErrorAllOfError = (ContractErrorAllOfError) o;
    return Objects.equals(this.code, contractErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContractErrorAllOfError {\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

