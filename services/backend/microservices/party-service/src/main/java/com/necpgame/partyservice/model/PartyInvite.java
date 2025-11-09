package com.necpgame.partyservice.model;

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
 * PartyInvite
 */


public class PartyInvite {

  private @Nullable String inviteId;

  private @Nullable String partyId;

  private @Nullable String senderId;

  private @Nullable String targetId;

  private @Nullable String code;

  /**
   * Gets or Sets visibility
   */
  public enum VisibilityEnum {
    PRIVATE("PRIVATE"),
    
    FRIENDS("FRIENDS"),
    
    PUBLIC("PUBLIC");

    private final String value;

    VisibilityEnum(String value) {
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
    public static VisibilityEnum fromValue(String value) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VisibilityEnum visibility;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime expiresAt;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    PENDING("PENDING"),
    
    ACCEPTED("ACCEPTED"),
    
    DECLINED("DECLINED"),
    
    EXPIRED("EXPIRED");

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

  public PartyInvite inviteId(@Nullable String inviteId) {
    this.inviteId = inviteId;
    return this;
  }

  /**
   * Get inviteId
   * @return inviteId
   */
  
  @Schema(name = "inviteId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("inviteId")
  public @Nullable String getInviteId() {
    return inviteId;
  }

  public void setInviteId(@Nullable String inviteId) {
    this.inviteId = inviteId;
  }

  public PartyInvite partyId(@Nullable String partyId) {
    this.partyId = partyId;
    return this;
  }

  /**
   * Get partyId
   * @return partyId
   */
  
  @Schema(name = "partyId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("partyId")
  public @Nullable String getPartyId() {
    return partyId;
  }

  public void setPartyId(@Nullable String partyId) {
    this.partyId = partyId;
  }

  public PartyInvite senderId(@Nullable String senderId) {
    this.senderId = senderId;
    return this;
  }

  /**
   * Get senderId
   * @return senderId
   */
  
  @Schema(name = "senderId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("senderId")
  public @Nullable String getSenderId() {
    return senderId;
  }

  public void setSenderId(@Nullable String senderId) {
    this.senderId = senderId;
  }

  public PartyInvite targetId(@Nullable String targetId) {
    this.targetId = targetId;
    return this;
  }

  /**
   * Get targetId
   * @return targetId
   */
  
  @Schema(name = "targetId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("targetId")
  public @Nullable String getTargetId() {
    return targetId;
  }

  public void setTargetId(@Nullable String targetId) {
    this.targetId = targetId;
  }

  public PartyInvite code(@Nullable String code) {
    this.code = code;
    return this;
  }

  /**
   * Get code
   * @return code
   */
  
  @Schema(name = "code", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("code")
  public @Nullable String getCode() {
    return code;
  }

  public void setCode(@Nullable String code) {
    this.code = code;
  }

  public PartyInvite visibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PartyInvite expiresAt(@Nullable OffsetDateTime expiresAt) {
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

  public PartyInvite status(@Nullable StatusEnum status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PartyInvite partyInvite = (PartyInvite) o;
    return Objects.equals(this.inviteId, partyInvite.inviteId) &&
        Objects.equals(this.partyId, partyInvite.partyId) &&
        Objects.equals(this.senderId, partyInvite.senderId) &&
        Objects.equals(this.targetId, partyInvite.targetId) &&
        Objects.equals(this.code, partyInvite.code) &&
        Objects.equals(this.visibility, partyInvite.visibility) &&
        Objects.equals(this.expiresAt, partyInvite.expiresAt) &&
        Objects.equals(this.status, partyInvite.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(inviteId, partyId, senderId, targetId, code, visibility, expiresAt, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PartyInvite {\n");
    sb.append("    inviteId: ").append(toIndentedString(inviteId)).append("\n");
    sb.append("    partyId: ").append(toIndentedString(partyId)).append("\n");
    sb.append("    senderId: ").append(toIndentedString(senderId)).append("\n");
    sb.append("    targetId: ").append(toIndentedString(targetId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    expiresAt: ").append(toIndentedString(expiresAt)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

