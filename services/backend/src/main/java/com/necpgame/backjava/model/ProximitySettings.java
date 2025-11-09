package com.necpgame.backjava.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ProximitySettings
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", comments = "Generator version: 7.17.0")
public class ProximitySettings {

  private @Nullable Boolean enabled;

  private @Nullable BigDecimal falloffStartMeters;

  private @Nullable BigDecimal falloffEndMeters;

  private @Nullable Boolean spatialAudio;

  public ProximitySettings enabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * Get enabled
   * @return enabled
   */
  
  @Schema(name = "enabled", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("enabled")
  public @Nullable Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(@Nullable Boolean enabled) {
    this.enabled = enabled;
  }

  public ProximitySettings falloffStartMeters(@Nullable BigDecimal falloffStartMeters) {
    this.falloffStartMeters = falloffStartMeters;
    return this;
  }

  /**
   * Get falloffStartMeters
   * minimum: 0
   * @return falloffStartMeters
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "falloffStartMeters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("falloffStartMeters")
  public @Nullable BigDecimal getFalloffStartMeters() {
    return falloffStartMeters;
  }

  public void setFalloffStartMeters(@Nullable BigDecimal falloffStartMeters) {
    this.falloffStartMeters = falloffStartMeters;
  }

  public ProximitySettings falloffEndMeters(@Nullable BigDecimal falloffEndMeters) {
    this.falloffEndMeters = falloffEndMeters;
    return this;
  }

  /**
   * Get falloffEndMeters
   * minimum: 0
   * @return falloffEndMeters
   */
  @Valid @DecimalMin(value = "0") 
  @Schema(name = "falloffEndMeters", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("falloffEndMeters")
  public @Nullable BigDecimal getFalloffEndMeters() {
    return falloffEndMeters;
  }

  public void setFalloffEndMeters(@Nullable BigDecimal falloffEndMeters) {
    this.falloffEndMeters = falloffEndMeters;
  }

  public ProximitySettings spatialAudio(@Nullable Boolean spatialAudio) {
    this.spatialAudio = spatialAudio;
    return this;
  }

  /**
   * Get spatialAudio
   * @return spatialAudio
   */
  
  @Schema(name = "spatialAudio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("spatialAudio")
  public @Nullable Boolean getSpatialAudio() {
    return spatialAudio;
  }

  public void setSpatialAudio(@Nullable Boolean spatialAudio) {
    this.spatialAudio = spatialAudio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProximitySettings proximitySettings = (ProximitySettings) o;
    return Objects.equals(this.enabled, proximitySettings.enabled) &&
        Objects.equals(this.falloffStartMeters, proximitySettings.falloffStartMeters) &&
        Objects.equals(this.falloffEndMeters, proximitySettings.falloffEndMeters) &&
        Objects.equals(this.spatialAudio, proximitySettings.spatialAudio);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, falloffStartMeters, falloffEndMeters, spatialAudio);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProximitySettings {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    falloffStartMeters: ").append(toIndentedString(falloffStartMeters)).append("\n");
    sb.append("    falloffEndMeters: ").append(toIndentedString(falloffEndMeters)).append("\n");
    sb.append("    spatialAudio: ").append(toIndentedString(spatialAudio)).append("\n");
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

