package com.necpgame.socialservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.socialservice.model.PlayerOrderGuaranteeSelection;
import com.necpgame.socialservice.model.PlayerOrderInvitee;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * PlayerOrderPublicationPayload
 */


public class PlayerOrderPublicationPayload {

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

  private VisibilityEnum visibility;

  @Valid
  private List<@Valid PlayerOrderInvitee> invited = new ArrayList<>();

  private PlayerOrderGuaranteeSelection guarantees;

  private Boolean releaseEscrowImmediately = true;

  /**
   * Gets or Sets notificationChannels
   */
  public enum NotificationChannelsEnum {
    IN_GAME("in_game"),
    
    MAIL("mail"),
    
    SMS("sms"),
    
    HOLO("holo");

    private final String value;

    NotificationChannelsEnum(String value) {
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
    public static NotificationChannelsEnum fromValue(String value) {
      for (NotificationChannelsEnum b : NotificationChannelsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @Valid
  private List<NotificationChannelsEnum> notificationChannels = new ArrayList<>();

  private @Nullable String publishNotes;

  public PlayerOrderPublicationPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlayerOrderPublicationPayload(VisibilityEnum visibility, PlayerOrderGuaranteeSelection guarantees) {
    this.visibility = visibility;
    this.guarantees = guarantees;
  }

  public PlayerOrderPublicationPayload visibility(VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  @NotNull 
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("visibility")
  public VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(VisibilityEnum visibility) {
    this.visibility = visibility;
  }

  public PlayerOrderPublicationPayload invited(List<@Valid PlayerOrderInvitee> invited) {
    this.invited = invited;
    return this;
  }

  public PlayerOrderPublicationPayload addInvitedItem(PlayerOrderInvitee invitedItem) {
    if (this.invited == null) {
      this.invited = new ArrayList<>();
    }
    this.invited.add(invitedItem);
    return this;
  }

  /**
   * Список приглашённых игроков или NPC.
   * @return invited
   */
  @Valid 
  @Schema(name = "invited", description = "Список приглашённых игроков или NPC.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("invited")
  public List<@Valid PlayerOrderInvitee> getInvited() {
    return invited;
  }

  public void setInvited(List<@Valid PlayerOrderInvitee> invited) {
    this.invited = invited;
  }

  public PlayerOrderPublicationPayload guarantees(PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
    return this;
  }

  /**
   * Get guarantees
   * @return guarantees
   */
  @NotNull @Valid 
  @Schema(name = "guarantees", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("guarantees")
  public PlayerOrderGuaranteeSelection getGuarantees() {
    return guarantees;
  }

  public void setGuarantees(PlayerOrderGuaranteeSelection guarantees) {
    this.guarantees = guarantees;
  }

  public PlayerOrderPublicationPayload releaseEscrowImmediately(Boolean releaseEscrowImmediately) {
    this.releaseEscrowImmediately = releaseEscrowImmediately;
    return this;
  }

  /**
   * Get releaseEscrowImmediately
   * @return releaseEscrowImmediately
   */
  
  @Schema(name = "releaseEscrowImmediately", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("releaseEscrowImmediately")
  public Boolean getReleaseEscrowImmediately() {
    return releaseEscrowImmediately;
  }

  public void setReleaseEscrowImmediately(Boolean releaseEscrowImmediately) {
    this.releaseEscrowImmediately = releaseEscrowImmediately;
  }

  public PlayerOrderPublicationPayload notificationChannels(List<NotificationChannelsEnum> notificationChannels) {
    this.notificationChannels = notificationChannels;
    return this;
  }

  public PlayerOrderPublicationPayload addNotificationChannelsItem(NotificationChannelsEnum notificationChannelsItem) {
    if (this.notificationChannels == null) {
      this.notificationChannels = new ArrayList<>();
    }
    this.notificationChannels.add(notificationChannelsItem);
    return this;
  }

  /**
   * Get notificationChannels
   * @return notificationChannels
   */
  
  @Schema(name = "notificationChannels", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notificationChannels")
  public List<NotificationChannelsEnum> getNotificationChannels() {
    return notificationChannels;
  }

  public void setNotificationChannels(List<NotificationChannelsEnum> notificationChannels) {
    this.notificationChannels = notificationChannels;
  }

  public PlayerOrderPublicationPayload publishNotes(@Nullable String publishNotes) {
    this.publishNotes = publishNotes;
    return this;
  }

  /**
   * Get publishNotes
   * @return publishNotes
   */
  
  @Schema(name = "publishNotes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("publishNotes")
  public @Nullable String getPublishNotes() {
    return publishNotes;
  }

  public void setPublishNotes(@Nullable String publishNotes) {
    this.publishNotes = publishNotes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerOrderPublicationPayload playerOrderPublicationPayload = (PlayerOrderPublicationPayload) o;
    return Objects.equals(this.visibility, playerOrderPublicationPayload.visibility) &&
        Objects.equals(this.invited, playerOrderPublicationPayload.invited) &&
        Objects.equals(this.guarantees, playerOrderPublicationPayload.guarantees) &&
        Objects.equals(this.releaseEscrowImmediately, playerOrderPublicationPayload.releaseEscrowImmediately) &&
        Objects.equals(this.notificationChannels, playerOrderPublicationPayload.notificationChannels) &&
        Objects.equals(this.publishNotes, playerOrderPublicationPayload.publishNotes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(visibility, invited, guarantees, releaseEscrowImmediately, notificationChannels, publishNotes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerOrderPublicationPayload {\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
    sb.append("    invited: ").append(toIndentedString(invited)).append("\n");
    sb.append("    guarantees: ").append(toIndentedString(guarantees)).append("\n");
    sb.append("    releaseEscrowImmediately: ").append(toIndentedString(releaseEscrowImmediately)).append("\n");
    sb.append("    notificationChannels: ").append(toIndentedString(notificationChannels)).append("\n");
    sb.append("    publishNotes: ").append(toIndentedString(publishNotes)).append("\n");
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

