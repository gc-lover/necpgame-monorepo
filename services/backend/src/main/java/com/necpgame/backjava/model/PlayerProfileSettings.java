package com.necpgame.backjava.model;

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
 * PlayerProfileSettings
 */

@JsonTypeName("PlayerProfile_settings")

public class PlayerProfileSettings {

  private @Nullable Object ui;

  private @Nullable Object audio;

  private @Nullable Object graphics;

  public PlayerProfileSettings ui(@Nullable Object ui) {
    this.ui = ui;
    return this;
  }

  /**
   * Get ui
   * @return ui
   */
  
  @Schema(name = "ui", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ui")
  public @Nullable Object getUi() {
    return ui;
  }

  public void setUi(@Nullable Object ui) {
    this.ui = ui;
  }

  public PlayerProfileSettings audio(@Nullable Object audio) {
    this.audio = audio;
    return this;
  }

  /**
   * Get audio
   * @return audio
   */
  
  @Schema(name = "audio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("audio")
  public @Nullable Object getAudio() {
    return audio;
  }

  public void setAudio(@Nullable Object audio) {
    this.audio = audio;
  }

  public PlayerProfileSettings graphics(@Nullable Object graphics) {
    this.graphics = graphics;
    return this;
  }

  /**
   * Get graphics
   * @return graphics
   */
  
  @Schema(name = "graphics", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("graphics")
  public @Nullable Object getGraphics() {
    return graphics;
  }

  public void setGraphics(@Nullable Object graphics) {
    this.graphics = graphics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlayerProfileSettings playerProfileSettings = (PlayerProfileSettings) o;
    return Objects.equals(this.ui, playerProfileSettings.ui) &&
        Objects.equals(this.audio, playerProfileSettings.audio) &&
        Objects.equals(this.graphics, playerProfileSettings.graphics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ui, audio, graphics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlayerProfileSettings {\n");
    sb.append("    ui: ").append(toIndentedString(ui)).append("\n");
    sb.append("    audio: ").append(toIndentedString(audio)).append("\n");
    sb.append("    graphics: ").append(toIndentedString(graphics)).append("\n");
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

