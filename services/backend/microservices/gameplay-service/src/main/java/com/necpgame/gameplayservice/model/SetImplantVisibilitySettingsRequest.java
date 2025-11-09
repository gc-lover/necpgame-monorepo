package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.SetImplantVisibilitySettingsRequestCustomSettings;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SetImplantVisibilitySettingsRequest
 */

@JsonTypeName("setImplantVisibilitySettings_request")

public class SetImplantVisibilitySettingsRequest {

  private String characterId;

  /**
   * Режим видимости
   */
  public enum VisibilityModeEnum {
    SHOW_ALL("show_all"),
    
    HIDE_INTERNAL("hide_internal"),
    
    HIDE_ALL("hide_all"),
    
    CUSTOM("custom");

    private final String value;

    VisibilityModeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static VisibilityModeEnum fromValue(String value) {
      for (VisibilityModeEnum b : VisibilityModeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private VisibilityModeEnum visibilityMode;

  private @Nullable SetImplantVisibilitySettingsRequestCustomSettings customSettings;

  public SetImplantVisibilitySettingsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SetImplantVisibilitySettingsRequest(String characterId, VisibilityModeEnum visibilityMode) {
    this.characterId = characterId;
    this.visibilityMode = visibilityMode;
  }

  public SetImplantVisibilitySettingsRequest characterId(String characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @NotNull 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character_id")
  public String getCharacterId() {
    return characterId;
  }

  public void setCharacterId(String characterId) {
    this.characterId = characterId;
  }

  public SetImplantVisibilitySettingsRequest visibilityMode(VisibilityModeEnum visibilityMode) {
    this.visibilityMode = visibilityMode;
    return this;
  }

  /**
   * Режим видимости
   * @return visibilityMode
   */
  @NotNull 
  @Schema(name = "visibility_mode", description = "Режим видимости", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("visibility_mode")
  public VisibilityModeEnum getVisibilityMode() {
    return visibilityMode;
  }

  public void setVisibilityMode(VisibilityModeEnum visibilityMode) {
    this.visibilityMode = visibilityMode;
  }

  public SetImplantVisibilitySettingsRequest customSettings(@Nullable SetImplantVisibilitySettingsRequestCustomSettings customSettings) {
    this.customSettings = customSettings;
    return this;
  }

  /**
   * Get customSettings
   * @return customSettings
   */
  @Valid 
  @Schema(name = "custom_settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("custom_settings")
  public @Nullable SetImplantVisibilitySettingsRequestCustomSettings getCustomSettings() {
    return customSettings;
  }

  public void setCustomSettings(@Nullable SetImplantVisibilitySettingsRequestCustomSettings customSettings) {
    this.customSettings = customSettings;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetImplantVisibilitySettingsRequest setImplantVisibilitySettingsRequest = (SetImplantVisibilitySettingsRequest) o;
    return Objects.equals(this.characterId, setImplantVisibilitySettingsRequest.characterId) &&
        Objects.equals(this.visibilityMode, setImplantVisibilitySettingsRequest.visibilityMode) &&
        Objects.equals(this.customSettings, setImplantVisibilitySettingsRequest.customSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, visibilityMode, customSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetImplantVisibilitySettingsRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    visibilityMode: ").append(toIndentedString(visibilityMode)).append("\n");
    sb.append("    customSettings: ").append(toIndentedString(customSettings)).append("\n");
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

