package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DialogueNodeCinematic
 */

@JsonTypeName("DialogueNode_cinematic")

public class DialogueNodeCinematic {

  private @Nullable String cameraPreset;

  private @Nullable String audioCue;

  private @Nullable String vfxPreset;

  public DialogueNodeCinematic cameraPreset(@Nullable String cameraPreset) {
    this.cameraPreset = cameraPreset;
    return this;
  }

  /**
   * Get cameraPreset
   * @return cameraPreset
   */
  
  @Schema(name = "cameraPreset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cameraPreset")
  public @Nullable String getCameraPreset() {
    return cameraPreset;
  }

  public void setCameraPreset(@Nullable String cameraPreset) {
    this.cameraPreset = cameraPreset;
  }

  public DialogueNodeCinematic audioCue(@Nullable String audioCue) {
    this.audioCue = audioCue;
    return this;
  }

  /**
   * Get audioCue
   * @return audioCue
   */
  
  @Schema(name = "audioCue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audioCue")
  public @Nullable String getAudioCue() {
    return audioCue;
  }

  public void setAudioCue(@Nullable String audioCue) {
    this.audioCue = audioCue;
  }

  public DialogueNodeCinematic vfxPreset(@Nullable String vfxPreset) {
    this.vfxPreset = vfxPreset;
    return this;
  }

  /**
   * Get vfxPreset
   * @return vfxPreset
   */
  
  @Schema(name = "vfxPreset", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("vfxPreset")
  public @Nullable String getVfxPreset() {
    return vfxPreset;
  }

  public void setVfxPreset(@Nullable String vfxPreset) {
    this.vfxPreset = vfxPreset;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DialogueNodeCinematic dialogueNodeCinematic = (DialogueNodeCinematic) o;
    return Objects.equals(this.cameraPreset, dialogueNodeCinematic.cameraPreset) &&
        Objects.equals(this.audioCue, dialogueNodeCinematic.audioCue) &&
        Objects.equals(this.vfxPreset, dialogueNodeCinematic.vfxPreset);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cameraPreset, audioCue, vfxPreset);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogueNodeCinematic {\n");
    sb.append("    cameraPreset: ").append(toIndentedString(cameraPreset)).append("\n");
    sb.append("    audioCue: ").append(toIndentedString(audioCue)).append("\n");
    sb.append("    vfxPreset: ").append(toIndentedString(vfxPreset)).append("\n");
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

