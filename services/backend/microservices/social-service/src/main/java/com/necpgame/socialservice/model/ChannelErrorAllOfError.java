package com.necpgame.socialservice.model;

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
 * ChannelErrorAllOfError
 */

@JsonTypeName("ChannelError_allOf_error")

public class ChannelErrorAllOfError {

  /**
   * Gets or Sets code
   */
  public enum CodeEnum {
    BIZ_CHAT_CHANNEL_NOT_FOUND("BIZ_CHAT_CHANNEL_NOT_FOUND"),
    
    BIZ_CHAT_CHANNEL_ACCESS_DENIED("BIZ_CHAT_CHANNEL_ACCESS_DENIED"),
    
    BIZ_CHAT_CHANNEL_LIMIT_REACHED("BIZ_CHAT_CHANNEL_LIMIT_REACHED"),
    
    BIZ_CHAT_CHANNEL_COOLDOWN_ACTIVE("BIZ_CHAT_CHANNEL_COOLDOWN_ACTIVE"),
    
    VAL_CHAT_CHANNEL_INVALID_SCOPE("VAL_CHAT_CHANNEL_INVALID_SCOPE"),
    
    VAL_CHAT_CHANNEL_INVALID_INVITE("VAL_CHAT_CHANNEL_INVALID_INVITE"),
    
    VAL_CHAT_CHANNEL_DUPLICATE_NAME("VAL_CHAT_CHANNEL_DUPLICATE_NAME"),
    
    INT_CHAT_CHANNEL_STORAGE_FAILURE("INT_CHAT_CHANNEL_STORAGE_FAILURE"),
    
    INT_CHAT_CHANNEL_EVENT_FAILURE("INT_CHAT_CHANNEL_EVENT_FAILURE");

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

  public ChannelErrorAllOfError() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ChannelErrorAllOfError(CodeEnum code) {
    this.code = code;
  }

  public ChannelErrorAllOfError code(CodeEnum code) {
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
    ChannelErrorAllOfError channelErrorAllOfError = (ChannelErrorAllOfError) o;
    return Objects.equals(this.code, channelErrorAllOfError.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChannelErrorAllOfError {\n");
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

