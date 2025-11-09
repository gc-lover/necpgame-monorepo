package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ReviewFlag
 */


public class ReviewFlag {

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

  private UUID setBy;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime setAt;

  public ReviewFlag() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ReviewFlag(FlagEnum flag, UUID setBy, OffsetDateTime setAt) {
    this.flag = flag;
    this.setBy = setBy;
    this.setAt = setAt;
  }

  public ReviewFlag flag(FlagEnum flag) {
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

  public ReviewFlag reason(@Nullable String reason) {
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

  public ReviewFlag setBy(UUID setBy) {
    this.setBy = setBy;
    return this;
  }

  /**
   * Get setBy
   * @return setBy
   */
  @NotNull @Valid 
  @Schema(name = "setBy", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("setBy")
  public UUID getSetBy() {
    return setBy;
  }

  public void setSetBy(UUID setBy) {
    this.setBy = setBy;
  }

  public ReviewFlag setAt(OffsetDateTime setAt) {
    this.setAt = setAt;
    return this;
  }

  /**
   * Get setAt
   * @return setAt
   */
  @NotNull @Valid 
  @Schema(name = "setAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("setAt")
  public OffsetDateTime getSetAt() {
    return setAt;
  }

  public void setSetAt(OffsetDateTime setAt) {
    this.setAt = setAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReviewFlag reviewFlag = (ReviewFlag) o;
    return Objects.equals(this.flag, reviewFlag.flag) &&
        Objects.equals(this.reason, reviewFlag.reason) &&
        Objects.equals(this.setBy, reviewFlag.setBy) &&
        Objects.equals(this.setAt, reviewFlag.setAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flag, reason, setBy, setAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ReviewFlag {\n");
    sb.append("    flag: ").append(toIndentedString(flag)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    setBy: ").append(toIndentedString(setBy)).append("\n");
    sb.append("    setAt: ").append(toIndentedString(setAt)).append("\n");
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

