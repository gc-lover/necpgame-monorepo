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
 * GuestInviteRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class GuestInviteRequest {

  private String guestPlayerId;

  /**
   * Gets or Sets action
   */
  public enum ActionEnum {
    INVITE("invite"),
    
    REVOKE("revoke"),
    
    BAN("ban");

    private final String value;

    ActionEnum(String value) {
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
    public static ActionEnum fromValue(String value) {
      for (ActionEnum b : ActionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private ActionEnum action;

  /**
   * Gets or Sets accessLevel
   */
  public enum AccessLevelEnum {
    VISITOR("visitor"),
    
    DECORATOR("decorator"),
    
    COHOST("cohost");

    private final String value;

    AccessLevelEnum(String value) {
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
    public static AccessLevelEnum fromValue(String value) {
      for (AccessLevelEnum b : AccessLevelEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private AccessLevelEnum accessLevel = AccessLevelEnum.VISITOR;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  public GuestInviteRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GuestInviteRequest(String guestPlayerId, ActionEnum action) {
    this.guestPlayerId = guestPlayerId;
    this.action = action;
  }

  public GuestInviteRequest guestPlayerId(String guestPlayerId) {
    this.guestPlayerId = guestPlayerId;
    return this;
  }

  /**
   * Get guestPlayerId
   * @return guestPlayerId
   */
  @NotNull 
  @Schema(name = "guestPlayerId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("guestPlayerId")
  public String getGuestPlayerId() {
    return guestPlayerId;
  }

  public void setGuestPlayerId(String guestPlayerId) {
    this.guestPlayerId = guestPlayerId;
  }

  public GuestInviteRequest action(ActionEnum action) {
    this.action = action;
    return this;
  }

  /**
   * Get action
   * @return action
   */
  @NotNull 
  @Schema(name = "action", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("action")
  public ActionEnum getAction() {
    return action;
  }

  public void setAction(ActionEnum action) {
    this.action = action;
  }

  public GuestInviteRequest accessLevel(AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
    return this;
  }

  /**
   * Get accessLevel
   * @return accessLevel
   */
  
  @Schema(name = "accessLevel", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("accessLevel")
  public AccessLevelEnum getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(AccessLevelEnum accessLevel) {
    this.accessLevel = accessLevel;
  }

  public GuestInviteRequest expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GuestInviteRequest guestInviteRequest = (GuestInviteRequest) o;
    return Objects.equals(this.guestPlayerId, guestInviteRequest.guestPlayerId) &&
        Objects.equals(this.action, guestInviteRequest.action) &&
        Objects.equals(this.accessLevel, guestInviteRequest.accessLevel) &&
        Objects.equals(this.expiresAt, guestInviteRequest.expiresAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guestPlayerId, action, accessLevel, expiresAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GuestInviteRequest {\n");
    sb.append("    guestPlayerId: ").append(toIndentedString(guestPlayerId)).append("\n");
    sb.append("    action: ").append(toIndentedString(action)).append("\n");
    sb.append("    accessLevel: ").append(toIndentedString(accessLevel)).append("\n");
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

