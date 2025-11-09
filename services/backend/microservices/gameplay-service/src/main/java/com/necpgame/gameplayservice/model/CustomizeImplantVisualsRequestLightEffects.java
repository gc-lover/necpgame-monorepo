package com.necpgame.gameplayservice.model;

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
 * CustomizeImplantVisualsRequestLightEffects
 */

@JsonTypeName("customizeImplantVisuals_request_light_effects")

public class CustomizeImplantVisualsRequestLightEffects {

  private @Nullable Boolean glow;

  private @Nullable Boolean flicker;

  private @Nullable Boolean highlight;

  public CustomizeImplantVisualsRequestLightEffects glow(@Nullable Boolean glow) {
    this.glow = glow;
    return this;
  }

  /**
   * Свечение
   * @return glow
   */
  
  @Schema(name = "glow", description = "Свечение", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("glow")
  public @Nullable Boolean getGlow() {
    return glow;
  }

  public void setGlow(@Nullable Boolean glow) {
    this.glow = glow;
  }

  public CustomizeImplantVisualsRequestLightEffects flicker(@Nullable Boolean flicker) {
    this.flicker = flicker;
    return this;
  }

  /**
   * Мерцание
   * @return flicker
   */
  
  @Schema(name = "flicker", description = "Мерцание", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flicker")
  public @Nullable Boolean getFlicker() {
    return flicker;
  }

  public void setFlicker(@Nullable Boolean flicker) {
    this.flicker = flicker;
  }

  public CustomizeImplantVisualsRequestLightEffects highlight(@Nullable Boolean highlight) {
    this.highlight = highlight;
    return this;
  }

  /**
   * Подсветка
   * @return highlight
   */
  
  @Schema(name = "highlight", description = "Подсветка", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("highlight")
  public @Nullable Boolean getHighlight() {
    return highlight;
  }

  public void setHighlight(@Nullable Boolean highlight) {
    this.highlight = highlight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CustomizeImplantVisualsRequestLightEffects customizeImplantVisualsRequestLightEffects = (CustomizeImplantVisualsRequestLightEffects) o;
    return Objects.equals(this.glow, customizeImplantVisualsRequestLightEffects.glow) &&
        Objects.equals(this.flicker, customizeImplantVisualsRequestLightEffects.flicker) &&
        Objects.equals(this.highlight, customizeImplantVisualsRequestLightEffects.highlight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(glow, flicker, highlight);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomizeImplantVisualsRequestLightEffects {\n");
    sb.append("    glow: ").append(toIndentedString(glow)).append("\n");
    sb.append("    flicker: ").append(toIndentedString(flicker)).append("\n");
    sb.append("    highlight: ").append(toIndentedString(highlight)).append("\n");
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

