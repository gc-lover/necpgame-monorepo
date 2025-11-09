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
 * GuestInvite
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuestInvite {

  private @Nullable String guestPlayerId;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACCEPTED("ACCEPTED"),
    
    DECLINED("DECLINED"),
    
    BANNED("BANNED");

    private final String value;

    StatusEnum(String value) {
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
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StatusEnum status;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime invitedAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  private @Nullable String accessLevel;

  public GuestInvite guestPlayerId(@Nullable String guestPlayerId) {
    this.guestPlayerId = guestPlayerId;
    return this;
  }

  /**
   * Get guestPlayerId
   * @return guestPlayerId
   */
  
  @Schema(name = "guestPlayerId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guestPlayerId")
  public @Nullable String getGuestPlayerId() {
    return guestPlayerId;
  }

  public void setGuestPlayerId(@Nullable String guestPlayerId) {
    this.guestPlayerId = guestPlayerId;
  }

  public GuestInvite status(@Nullable StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
   */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public @Nullable StatusEnum getStatus() {
    return status;
  }

  public void setStatus(@Nullable StatusEnum status) {
    this.status = status;
  }

  public GuestInvite invitedAt(@Nullable OffsetDateTime invitedAt) {
    this.invitedAt = invitedAt;
    return this;
  }

  /**
   * Get invitedAt
   * @return invitedAt
   */
  @Valid 
  @Schema(name = "invitedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invitedAt")
  public @Nullable OffsetDateTime getInvitedAt() {
    return invitedAt;
  }

  public void setInvitedAt(@Nullable OffsetDateTime invitedAt) {
    this.invitedAt = invitedAt;
  }

  public GuestInvite expiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
    return this;
  }

  /**
   * Get expiresAt
   * @return expiresAt
   */
  @Valid 
  @Schema(name = "expiresAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("expiresAt")
  public @Nullable OffsetDateTime getExpiresAt() {
    return expiresAt;
  }

  public void setExpiresAt(@Nullable OffsetDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  public GuestInvite accessLevel(@Nullable String accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "accessLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accessLevel")
  public @Nullable String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(@Nullable String accessLevel) {
    this.accessLevel = accessLevel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestInvite guestInvite = (GuestInvite) o;
    return Objects.equals(this.guestPlayerId, guestInvite.guestPlayerId) &&
        Objects.equals(this.status, guestInvite.status) &&
        Objects.equals(this.invitedAt, guestInvite.invitedAt) &&
        Objects.equals(this.expiresAt, guestInvite.expiresAt) &&
        Objects.equals(this.accessLevel, guestInvite.accessLevel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guestPlayerId, status, invitedAt, expiresAt, accessLevel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestInvite {\n");
    sb.append("    guestPlayerId: ").append(toIndentedString(guestPlayerId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    invitedAt: ").append(toIndentedString(invitedAt)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
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

