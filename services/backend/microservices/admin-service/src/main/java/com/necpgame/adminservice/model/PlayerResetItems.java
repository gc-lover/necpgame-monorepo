package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.adminservice.model.PlayerResetItemsBonuses;
import com.necpgame.adminservice.model.PlayerResetItemsInstancesInner;
import com.necpgame.adminservice.model.PlayerResetItemsLimits;
import com.necpgame.adminservice.model.PlayerResetItemsQuests;
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
 * PlayerResetItems
 */


public class PlayerResetItems {

  private @Nullable PlayerResetItemsQuests quests;

  private @Nullable PlayerResetItemsLimits limits;

  private @Nullable PlayerResetItemsBonuses bonuses;

  @Valid
  private List<@Valid PlayerResetItemsInstancesInner> instances = new ArrayList<>();

  public PlayerResetItems quests(@Nullable PlayerResetItemsQuests quests) {
    this.quests = quests;
    return this;
  }

  /**
   * Get quests
   * @return quests
   */
  @Valid 
  @Schema(name = "quests", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("quests")
  public @Nullable PlayerResetItemsQuests getQuests() {
    return quests;
  }

  public void setQuests(@Nullable PlayerResetItemsQuests quests) {
    this.quests = quests;
  }

  public PlayerResetItems limits(@Nullable PlayerResetItemsLimits limits) {
    this.limits = limits;
    return this;
  }

  /**
   * Get limits
   * @return limits
   */
  @Valid 
  @Schema(name = "limits", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("limits")
  public @Nullable PlayerResetItemsLimits getLimits() {
    return limits;
  }

  public void setLimits(@Nullable PlayerResetItemsLimits limits) {
    this.limits = limits;
  }

  public PlayerResetItems bonuses(@Nullable PlayerResetItemsBonuses bonuses) {
    this.bonuses = bonuses;
    return this;
  }

  /**
   * Get bonuses
   * @return bonuses
   */
  @Valid 
  @Schema(name = "bonuses", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bonuses")
  public @Nullable PlayerResetItemsBonuses getBonuses() {
    return bonuses;
  }

  public void setBonuses(@Nullable PlayerResetItemsBonuses bonuses) {
    this.bonuses = bonuses;
  }

  public PlayerResetItems instances(List<@Valid PlayerResetItemsInstancesInner> instances) {
    this.instances = instances;
    return this;
  }

  public PlayerResetItems addInstancesItem(PlayerResetItemsInstancesInner instancesItem) {
    if (this.instances == null) {
      this.instances = new ArrayList<>();
    }
    this.instances.add(instancesItem);
    return this;
  }

  /**
   * Get instances
   * @return instances
   */
  @Valid 
  @Schema(name = "instances", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("instances")
  public List<@Valid PlayerResetItemsInstancesInner> getInstances() {
    return instances;
  }

  public void setInstances(List<@Valid PlayerResetItemsInstancesInner> instances) {
    this.instances = instances;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerResetItems playerResetItems = (PlayerResetItems) o;
    return Objects.equals(this.quests, playerResetItems.quests) &&
        Objects.equals(this.limits, playerResetItems.limits) &&
        Objects.equals(this.bonuses, playerResetItems.bonuses) &&
        Objects.equals(this.instances, playerResetItems.instances);
  }

  @Override
  public int hashCode() {
    return Objects.hash(quests, limits, bonuses, instances);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerResetItems {\n");
    sb.append("    quests: ").append(toIndentedString(quests)).append("\n");
    sb.append("    limits: ").append(toIndentedString(limits)).append("\n");
    sb.append("    bonuses: ").append(toIndentedString(bonuses)).append("\n");
    sb.append("    instances: ").append(toIndentedString(instances)).append("\n");
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

