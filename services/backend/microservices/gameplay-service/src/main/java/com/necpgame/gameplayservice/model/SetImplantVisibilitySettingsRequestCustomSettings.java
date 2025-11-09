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
 * Кастомные настройки для каждого импланта
 */

@Schema(name = "setImplantVisibilitySettings_request_custom_settings", description = "Кастомные настройки для каждого импланта")
@JsonTypeName("setImplantVisibilitySettings_request_custom_settings")

public class SetImplantVisibilitySettingsRequestCustomSettings {

  private @Nullable String implantId;

  private @Nullable String visibility;

  public SetImplantVisibilitySettingsRequestCustomSettings implantId(@Nullable String implantId) {
    this.implantId = implantId;
    return this;
  }

  /**
   * Get implantId
   * @return implantId
   */
  
  @Schema(name = "implant_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("implant_id")
  public @Nullable String getImplantId() {
    return implantId;
  }

  public void setImplantId(@Nullable String implantId) {
    this.implantId = implantId;
  }

  public SetImplantVisibilitySettingsRequestCustomSettings visibility(@Nullable String visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Get visibility
   * @return visibility
   */
  
  @Schema(name = "visibility", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable String getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable String visibility) {
    this.visibility = visibility;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetImplantVisibilitySettingsRequestCustomSettings setImplantVisibilitySettingsRequestCustomSettings = (SetImplantVisibilitySettingsRequestCustomSettings) o;
    return Objects.equals(this.implantId, setImplantVisibilitySettingsRequestCustomSettings.implantId) &&
        Objects.equals(this.visibility, setImplantVisibilitySettingsRequestCustomSettings.visibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(implantId, visibility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetImplantVisibilitySettingsRequestCustomSettings {\n");
    sb.append("    implantId: ").append(toIndentedString(implantId)).append("\n");
    sb.append("    visibility: ").append(toIndentedString(visibility)).append("\n");
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

