package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.Vector3;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ParticipantSetup
 */


public class ParticipantSetup {

  private @Nullable String participantId;

  private @Nullable String kind;

  private @Nullable String referenceId;

  @Valid
  private Map<String, Object> loadout = new HashMap<>();

  private @Nullable Vector3 spawnPoint;

  public ParticipantSetup participantId(@Nullable String participantId) {
    this.participantId = participantId;
    return this;
  }

  /**
   * Get participantId
   * @return participantId
   */
  
  @Schema(name = "participantId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("participantId")
  public @Nullable String getParticipantId() {
    return participantId;
  }

  public void setParticipantId(@Nullable String participantId) {
    this.participantId = participantId;
  }

  public ParticipantSetup kind(@Nullable String kind) {
    this.kind = kind;
    return this;
  }

  /**
   * Get kind
   * @return kind
   */
  
  @Schema(name = "kind", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("kind")
  public @Nullable String getKind() {
    return kind;
  }

  public void setKind(@Nullable String kind) {
    this.kind = kind;
  }

  public ParticipantSetup referenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
    return this;
  }

  /**
   * Get referenceId
   * @return referenceId
   */
  
  @Schema(name = "referenceId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("referenceId")
  public @Nullable String getReferenceId() {
    return referenceId;
  }

  public void setReferenceId(@Nullable String referenceId) {
    this.referenceId = referenceId;
  }

  public ParticipantSetup loadout(Map<String, Object> loadout) {
    this.loadout = loadout;
    return this;
  }

  public ParticipantSetup putLoadoutItem(String key, Object loadoutItem) {
    if (this.loadout == null) {
      this.loadout = new HashMap<>();
    }
    this.loadout.put(key, loadoutItem);
    return this;
  }

  /**
   * Get loadout
   * @return loadout
   */
  
  @Schema(name = "loadout", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("loadout")
  public Map<String, Object> getLoadout() {
    return loadout;
  }

  public void setLoadout(Map<String, Object> loadout) {
    this.loadout = loadout;
  }

  public ParticipantSetup spawnPoint(@Nullable Vector3 spawnPoint) {
    this.spawnPoint = spawnPoint;
    return this;
  }

  /**
   * Get spawnPoint
   * @return spawnPoint
   */
  @Valid 
  @Schema(name = "spawnPoint", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spawnPoint")
  public @Nullable Vector3 getSpawnPoint() {
    return spawnPoint;
  }

  public void setSpawnPoint(@Nullable Vector3 spawnPoint) {
    this.spawnPoint = spawnPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParticipantSetup participantSetup = (ParticipantSetup) o;
    return Objects.equals(this.participantId, participantSetup.participantId) &&
        Objects.equals(this.kind, participantSetup.kind) &&
        Objects.equals(this.referenceId, participantSetup.referenceId) &&
        Objects.equals(this.loadout, participantSetup.loadout) &&
        Objects.equals(this.spawnPoint, participantSetup.spawnPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(participantId, kind, referenceId, loadout, spawnPoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParticipantSetup {\n");
    sb.append("    participantId: ").append(toIndentedString(participantId)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
    sb.append("    referenceId: ").append(toIndentedString(referenceId)).append("\n");
    sb.append("    loadout: ").append(toIndentedString(loadout)).append("\n");
    sb.append("    spawnPoint: ").append(toIndentedString(spawnPoint)).append("\n");
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

