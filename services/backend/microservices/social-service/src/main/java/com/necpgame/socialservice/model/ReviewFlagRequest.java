package com.necpgame.socialservice.model;

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
 * ReviewFlagRequest
 */


public class ReviewFlagRequest {

  /**
   * Gets or Sets flag
   */
  public enum FlagEnum {
    POSITIVE("positive"),
    
    NEUTRAL("neutral"),
    
    NEGATIVE("negative"),
    
    WARNING("warning"),
    
    DISPUTE("dispute");

    private final String value;

    FlagEnum(String value) {
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
    public static FlagEnum fromValue(String value) {
      for (FlagEnum b : FlagEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private FlagEnum flag;

  private @Nullable String reason;

  private @Nullable UUID setBy;

  public ReviewFlagRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewFlagRequest(FlagEnum flag) {
    this.flag = flag;
  }

  public ReviewFlagRequest flag(FlagEnum flag) {
    this.flag = flag;
    return this;
  }

  /**
   * Get flag
   * @return flag
   */
  @NotNull 
  @Schema(name = "flag", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("flag")
  public FlagEnum getFlag() {
    return flag;
  }

  public void setFlag(FlagEnum flag) {
    this.flag = flag;
  }

  public ReviewFlagRequest reason(@Nullable String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Get reason
   * @return reason
   */
  @Size(max = 512) 
  @Schema(name = "reason", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reason")
  public @Nullable String getReason() {
    return reason;
  }

  public void setReason(@Nullable String reason) {
    this.reason = reason;
  }

  public ReviewFlagRequest setBy(@Nullable UUID setBy) {
    this.setBy = setBy;
    return this;
  }

  /**
   * Get setBy
   * @return setBy
   */
  @Valid 
  @Schema(name = "setBy", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("setBy")
  public @Nullable UUID getSetBy() {
    return setBy;
  }

  public void setSetBy(@Nullable UUID setBy) {
    this.setBy = setBy;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewFlagRequest reviewFlagRequest = (ReviewFlagRequest) o;
    return Objects.equals(this.flag, reviewFlagRequest.flag) &&
        Objects.equals(this.reason, reviewFlagRequest.reason) &&
        Objects.equals(this.setBy, reviewFlagRequest.setBy);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flag, reason, setBy);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewFlagRequest {\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    setBy: ").append(toIndentedString(setBy)).append("\n");
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

