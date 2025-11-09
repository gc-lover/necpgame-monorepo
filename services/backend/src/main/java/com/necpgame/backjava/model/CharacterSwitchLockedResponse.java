package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
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
 * CharacterSwitchLockedResponse
 */


public class CharacterSwitchLockedResponse {

  /**
   * Gets or Sets lockReason
   */
  public enum LockReasonEnum {
    COMBAT("combat"),
    
    CINEMATIC("cinematic"),
    
    MISSION_CRITICAL("mission_critical"),
    
    MAINTENANCE("maintenance");

    private final String value;

    LockReasonEnum(String value) {
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
    public static LockReasonEnum fromValue(String value) {
      for (LockReasonEnum b : LockReasonEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private LockReasonEnum lockReason;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime expiresAt;

  public CharacterSwitchLockedResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterSwitchLockedResponse(LockReasonEnum lockReason, OffsetDateTime expiresAt) {
    this.lockReason = lockReason;
    this.expiresAt = expiresAt;
  }

  public CharacterSwitchLockedResponse lockReason(LockReasonEnum lockReason) {
    this.lockReason = lockReason;
    return this;
  }

  /**
   * Get lockReason
   * @return lockReason
   */
  @NotNull 
  @Schema(name = "lockReason", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("lockReason")
  public LockReasonEnum getLockReason() {
    return lockReason;
  }

  public void setLockReason(LockReasonEnum lockReason) {
    this.lockReason = lockReason;
  }

  public CharacterSwitchLockedResponse expiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @NotNull @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("expiresAt")
  public OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterSwitchLockedResponse characterSwitchLockedResponse = (CharacterSwitchLockedResponse) o;
    return Objects.equals(this.lockReason, characterSwitchLockedResponse.lockReason) &&
        Objects.equals(this.expiresAt, characterSwitchLockedResponse.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lockReason, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterSwitchLockedResponse {\n");
    sb.append("    lockReason: ").append(toIndentedString(lockReason)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
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

