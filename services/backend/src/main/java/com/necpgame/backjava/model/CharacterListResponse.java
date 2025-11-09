package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.backjava.model.CharacterSlotState;
import com.necpgame.backjava.model.CharacterSummary;
import com.necpgame.backjava.model.PlayerProfile;
import com.necpgame.backjava.model.RestoreQueueEntry;
import com.necpgame.backjava.model.StateSnapshotRef;
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
 * CharacterListResponse
 */


public class CharacterListResponse {

  @Valid
  private List<@Valid CharacterSummary> data = new ArrayList<>();

  private CharacterSlotState slots;

  private PlayerProfile player;

  @Valid
  private List<@Valid RestoreQueueEntry> restoreQueue = new ArrayList<>();

  @Valid
  private List<@Valid StateSnapshotRef> snapshots = new ArrayList<>();

  public CharacterListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CharacterListResponse(List<@Valid CharacterSummary> data, CharacterSlotState slots, PlayerProfile player, List<@Valid RestoreQueueEntry> restoreQueue) {
    this.data = data;
    this.slots = slots;
    this.player = player;
    this.restoreQueue = restoreQueue;
  }

  public CharacterListResponse data(List<@Valid CharacterSummary> data) {
    this.data = data;
    return this;
  }

  public CharacterListResponse addDataItem(CharacterSummary dataItem) {
    if (this.data == null) {
      this.data = new ArrayList<>();
    }
    this.data.add(dataItem);
    return this;
  }

  /**
   * Get data
   * @return data
   */
  @NotNull @Valid 
  @Schema(name = "data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("data")
  public List<@Valid CharacterSummary> getData() {
    return data;
  }

  public void setData(List<@Valid CharacterSummary> data) {
    this.data = data;
  }

  public CharacterListResponse slots(CharacterSlotState slots) {
    this.slots = slots;
    return this;
  }

  /**
   * Get slots
   * @return slots
   */
  @NotNull @Valid 
  @Schema(name = "slots", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("slots")
  public CharacterSlotState getSlots() {
    return slots;
  }

  public void setSlots(CharacterSlotState slots) {
    this.slots = slots;
  }

  public CharacterListResponse player(PlayerProfile player) {
    this.player = player;
    return this;
  }

  /**
   * Get player
   * @return player
   */
  @NotNull @Valid 
  @Schema(name = "player", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("player")
  public PlayerProfile getPlayer() {
    return player;
  }

  public void setPlayer(PlayerProfile player) {
    this.player = player;
  }

  public CharacterListResponse restoreQueue(List<@Valid RestoreQueueEntry> restoreQueue) {
    this.restoreQueue = restoreQueue;
    return this;
  }

  public CharacterListResponse addRestoreQueueItem(RestoreQueueEntry restoreQueueItem) {
    if (this.restoreQueue == null) {
      this.restoreQueue = new ArrayList<>();
    }
    this.restoreQueue.add(restoreQueueItem);
    return this;
  }

  /**
   * Get restoreQueue
   * @return restoreQueue
   */
  @NotNull @Valid 
  @Schema(name = "restoreQueue", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("restoreQueue")
  public List<@Valid RestoreQueueEntry> getRestoreQueue() {
    return restoreQueue;
  }

  public void setRestoreQueue(List<@Valid RestoreQueueEntry> restoreQueue) {
    this.restoreQueue = restoreQueue;
  }

  public CharacterListResponse snapshots(List<@Valid StateSnapshotRef> snapshots) {
    this.snapshots = snapshots;
    return this;
  }

  public CharacterListResponse addSnapshotsItem(StateSnapshotRef snapshotsItem) {
    if (this.snapshots == null) {
      this.snapshots = new ArrayList<>();
    }
    this.snapshots.add(snapshotsItem);
    return this;
  }

  /**
   * Get snapshots
   * @return snapshots
   */
  @Valid 
  @Schema(name = "snapshots", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("snapshots")
  public List<@Valid StateSnapshotRef> getSnapshots() {
    return snapshots;
  }

  public void setSnapshots(List<@Valid StateSnapshotRef> snapshots) {
    this.snapshots = snapshots;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CharacterListResponse characterListResponse = (CharacterListResponse) o;
    return Objects.equals(this.data, characterListResponse.data) &&
        Objects.equals(this.slots, characterListResponse.slots) &&
        Objects.equals(this.player, characterListResponse.player) &&
        Objects.equals(this.restoreQueue, characterListResponse.restoreQueue) &&
        Objects.equals(this.snapshots, characterListResponse.snapshots);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, slots, player, restoreQueue, snapshots);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CharacterListResponse {\n");
    sb.append("    data: ").append(toIndentedString(data)).append("\n");
    sb.append("    slots: ").append(toIndentedString(slots)).append("\n");
    sb.append("    player: ").append(toIndentedString(player)).append("\n");
    sb.append("    restoreQueue: ").append(toIndentedString(restoreQueue)).append("\n");
    sb.append("    snapshots: ").append(toIndentedString(snapshots)).append("\n");
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

