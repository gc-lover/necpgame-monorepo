package com.necpgame.worldservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.worldservice.model.TriggerPayloadOverrides;
import com.necpgame.worldservice.model.TriggerSource;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TriggerPayload
 */


public class TriggerPayload {

  private TriggerSource source;

  private @Nullable UUID actorId;

  private @Nullable TriggerPayloadOverrides overrides;

  private @Nullable String notes;

  public TriggerPayload() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TriggerPayload(TriggerSource source) {
    this.source = source;
  }

  public TriggerPayload source(TriggerSource source) {
    this.source = source;
    return this;
  }

  /**
   * Get source
   * @return source
   */
  @NotNull @Valid 
  @Schema(name = "source", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("source")
  public TriggerSource getSource() {
    return source;
  }

  public void setSource(TriggerSource source) {
    this.source = source;
  }

  public TriggerPayload actorId(@Nullable UUID actorId) {
    this.actorId = actorId;
    return this;
  }

  /**
   * Get actorId
   * @return actorId
   */
  @Valid 
  @Schema(name = "actorId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("actorId")
  public @Nullable UUID getActorId() {
    return actorId;
  }

  public void setActorId(@Nullable UUID actorId) {
    this.actorId = actorId;
  }

  public TriggerPayload overrides(@Nullable TriggerPayloadOverrides overrides) {
    this.overrides = overrides;
    return this;
  }

  /**
   * Get overrides
   * @return overrides
   */
  @Valid 
  @Schema(name = "overrides", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("overrides")
  public @Nullable TriggerPayloadOverrides getOverrides() {
    return overrides;
  }

  public void setOverrides(@Nullable TriggerPayloadOverrides overrides) {
    this.overrides = overrides;
  }

  public TriggerPayload notes(@Nullable String notes) {
    this.notes = notes;
    return this;
  }

  /**
   * Get notes
   * @return notes
   */
  @Size(max = 500) 
  @Schema(name = "notes", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("notes")
  public @Nullable String getNotes() {
    return notes;
  }

  public void setNotes(@Nullable String notes) {
    this.notes = notes;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriggerPayload triggerPayload = (TriggerPayload) o;
    return Objects.equals(this.source, triggerPayload.source) &&
        Objects.equals(this.actorId, triggerPayload.actorId) &&
        Objects.equals(this.overrides, triggerPayload.overrides) &&
        Objects.equals(this.notes, triggerPayload.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, actorId, overrides, notes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriggerPayload {\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    actorId: ").append(toIndentedString(actorId)).append("\n");
    sb.append("    overrides: ").append(toIndentedString(overrides)).append("\n");
    sb.append("    notes: ").append(toIndentedString(notes)).append("\n");
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

