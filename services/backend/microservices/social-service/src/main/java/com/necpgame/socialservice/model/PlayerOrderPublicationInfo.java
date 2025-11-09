package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderGuaranteeSelection;
import com.necpgame.socialservice.model.PlayerOrderInvitee;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * PlayerOrderPublicationInfo
 */


public class PlayerOrderPublicationInfo {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime publishedAt;

  private @Nullable String publishToken;

  /**
   * Gets or Sets visibility
   */
  public enum VisibilityEnum {
    PUBLIC("public"),
    
    INVITE_ONLY("invite_only"),
    
    PRIVATE("private");

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

  @Valid
  private List<@Valid PlayerOrderInvitee> invited = new ArrayList<>();

  private @Nullable PlayerOrderGuaranteeSelection guarantees;

  /**
   * Gets or Sets escrowState
   */
  public enum EscrowStateEnum {
    PENDING_LOCK("pending_lock"),
    
    LOCKED("locked"),
    
    RELEASED("released"),
    
    REFUNDED("refunded");

    private final String value;

    EscrowStateEnum(String value) {
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
    public static EscrowStateEnum fromValue(String value) {
      for (EscrowStateEnum b : EscrowStateEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable EscrowStateEnum escrowState;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime lastNotificationAt;

  public PlayerOrderPublicationInfo publishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
    return this;
  }

  /**
   * Get publishedAt
   * @return publishedAt
   */
  @Valid 
  @Schema(name = "publishedAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishedAt")
  public @Nullable OffsetDateTime getPublishedAt() {
    return publishedAt;
  }

  public void setPublishedAt(@Nullable OffsetDateTime publishedAt) {
    this.publishedAt = publishedAt;
  }

  public PlayerOrderPublicationInfo publishToken(@Nullable String publishToken) {
    this.publishToken = publishToken;
    return this;
  }

  /**
   * Get publishToken
   * @return publishToken
   */
  
  @Schema(name = "publishToken", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishToken")
  public @Nullable String getPublishToken() {
    return publishToken;
  }

  public void setPublishToken(@Nullable String publishToken) {
    this.publishToken = publishToken;
  }

  public PlayerOrderPublicationInfo visibility(@Nullable VisibilityEnum visibility) {
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

  public PlayerOrderPublicationInfo invited(List<@Valid PlayerOrderInvitee> invited) {
    this.invited = invited;
    return this;
  }

  public PlayerOrderPublicationInfo addInvitedItem(PlayerOrderInvitee invitedItem) {
    if (this.invited == null) {
      this.invited = new ArrayList<>();
    }
    this.invited.add(invitedItem);
    return this;
  }

  /**
   * Get invited
   * @return invited
   */
  @Valid 
  @Schema(name = "invited", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invited")
  public List<@Valid PlayerOrderInvitee> getInvited() {
    return invited;
  }

  public void setInvited(List<@Valid PlayerOrderInvitee> invited) {
    this.invited = invited;
  }

  public PlayerOrderPublicationInfo guarantees(@Nullable PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
    return this;
  }

  /**
   * Get guarantees
   * @return guarantees
   */
  @Valid 
  @Schema(name = "guarantees", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("guarantees")
  public @Nullable PlayerOrderGuaranteeSelection getGuarantees() {
    return guarantees;
  }

  public void setGuarantees(@Nullable PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
  }

  public PlayerOrderPublicationInfo escrowState(@Nullable EscrowStateEnum escrowState) {
    this.escrowState = escrowState;
    return this;
  }

  /**
   * Get escrowState
   * @return escrowState
   */
  
  @Schema(name = "escrowState", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("escrowState")
  public @Nullable EscrowStateEnum getEscrowState() {
    return escrowState;
  }

  public void setEscrowState(@Nullable EscrowStateEnum escrowState) {
    this.escrowState = escrowState;
  }

  public PlayerOrderPublicationInfo lastNotificationAt(@Nullable OffsetDateTime lastNotificationAt) {
    this.lastNotificationAt = lastNotificationAt;
    return this;
  }

  /**
   * Get lastNotificationAt
   * @return lastNotificationAt
   */
  @Valid 
  @Schema(name = "lastNotificationAt", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastNotificationAt")
  public @Nullable OffsetDateTime getLastNotificationAt() {
    return lastNotificationAt;
  }

  public void setLastNotificationAt(@Nullable OffsetDateTime lastNotificationAt) {
    this.lastNotificationAt = lastNotificationAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPublicationInfo playerOrderPublicationInfo = (PlayerOrderPublicationInfo) o;
    return Objects.equals(this.publishedAt, playerOrderPublicationInfo.publishedAt) &&
        Objects.equals(this.publishToken, playerOrderPublicationInfo.publishToken) &&
        Objects.equals(this.visibility, playerOrderPublicationInfo.visibility) &&
        Objects.equals(this.invited, playerOrderPublicationInfo.invited) &&
        Objects.equals(this.guarantees, playerOrderPublicationInfo.guarantees) &&
        Objects.equals(this.escrowState, playerOrderPublicationInfo.escrowState) &&
        Objects.equals(this.lastNotificationAt, playerOrderPublicationInfo.lastNotificationAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publishedAt, publishToken, visibility, invited, guarantees, escrowState, lastNotificationAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPublicationInfo {\n");
    sb.append("    publishedAt: ").append(toIndentedString(publishedAt)).append("\n");
    sb.append("    publishToken: ").append(toIndentedString(publishToken)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    invited: ").append(toIndentedString(invited)).append("\n");
    sb.append("    guarantees: ").append(toIndentedString(guarantees)).append("\n");
    sb.append("    escrowState: ").append(toIndentedString(escrowState)).append("\n");
    sb.append("    lastNotificationAt: ").append(toIndentedString(lastNotificationAt)).append("\n");
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

