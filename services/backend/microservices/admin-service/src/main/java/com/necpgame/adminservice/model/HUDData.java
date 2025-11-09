package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.HUDDataCharacter;
import com.necpgame.adminservice.model.HUDDataMinimap;
import com.necpgame.adminservice.model.HUDDataNotificationsInner;
import com.necpgame.adminservice.model.HUDDataQuickActionsInner;
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
 * HUDData
 */


public class HUDData {

  private @Nullable HUDDataCharacter character;

  private @Nullable HUDDataMinimap minimap;

  @Valid
  private List<@Valid HUDDataQuickActionsInner> quickActions = new ArrayList<>();

  @Valid
  private List<@Valid HUDDataNotificationsInner> notifications = new ArrayList<>();

  public HUDData character(@Nullable HUDDataCharacter character) {
    this.character = character;
    return this;
  }

  /**
   * Get character
   * @return character
   */
  @Valid 
  @Schema(name = "character", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character")
  public @Nullable HUDDataCharacter getCharacter() {
    return character;
  }

  public void setCharacter(@Nullable HUDDataCharacter character) {
    this.character = character;
  }

  public HUDData minimap(@Nullable HUDDataMinimap minimap) {
    this.minimap = minimap;
    return this;
  }

  /**
   * Get minimap
   * @return minimap
   */
  @Valid 
  @Schema(name = "minimap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("minimap")
  public @Nullable HUDDataMinimap getMinimap() {
    return minimap;
  }

  public void setMinimap(@Nullable HUDDataMinimap minimap) {
    this.minimap = minimap;
  }

  public HUDData quickActions(List<@Valid HUDDataQuickActionsInner> quickActions) {
    this.quickActions = quickActions;
    return this;
  }

  public HUDData addQuickActionsItem(HUDDataQuickActionsInner quickActionsItem) {
    if (this.quickActions == null) {
      this.quickActions = new ArrayList<>();
    }
    this.quickActions.add(quickActionsItem);
    return this;
  }

  /**
   * Get quickActions
   * @return quickActions
   */
  @Valid 
  @Schema(name = "quick_actions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quick_actions")
  public List<@Valid HUDDataQuickActionsInner> getQuickActions() {
    return quickActions;
  }

  public void setQuickActions(List<@Valid HUDDataQuickActionsInner> quickActions) {
    this.quickActions = quickActions;
  }

  public HUDData notifications(List<@Valid HUDDataNotificationsInner> notifications) {
    this.notifications = notifications;
    return this;
  }

  public HUDData addNotificationsItem(HUDDataNotificationsInner notificationsItem) {
    if (this.notifications == null) {
      this.notifications = new ArrayList<>();
    }
    this.notifications.add(notificationsItem);
    return this;
  }

  /**
   * Get notifications
   * @return notifications
   */
  @Valid 
  @Schema(name = "notifications", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notifications")
  public List<@Valid HUDDataNotificationsInner> getNotifications() {
    return notifications;
  }

  public void setNotifications(List<@Valid HUDDataNotificationsInner> notifications) {
    this.notifications = notifications;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HUDData huDData = (HUDData) o;
    return Objects.equals(this.character, huDData.character) &&
        Objects.equals(this.minimap, huDData.minimap) &&
        Objects.equals(this.quickActions, huDData.quickActions) &&
        Objects.equals(this.notifications, huDData.notifications);
  }

  @Override
  public int hashCode() {
    return Objects.hash(character, minimap, quickActions, notifications);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class HUDData {\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
    sb.append("    minimap: ").append(toIndentedString(minimap)).append("\n");
    sb.append("    quickActions: ").append(toIndentedString(quickActions)).append("\n");
    sb.append("    notifications: ").append(toIndentedString(notifications)).append("\n");
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

