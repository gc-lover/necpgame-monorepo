package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.gameplayservice.model.CustomizeImplantVisualsRequestLightEffects;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CustomizeImplantVisualsRequest
 */

@JsonTypeName("customizeImplantVisuals_request")

public class CustomizeImplantVisualsRequest {

  private String characterId;

  /**
   * Цвет кибервара
   */
  public enum ColorEnum {
    CHROME("chrome"),
    
    GOLD("gold"),
    
    BLACK("black"),
    
    RED("red"),
    
    BLUE("blue"),
    
    GREEN("green"),
    
    CUSTOM("custom");

    private final String value;

    ColorEnum(String value) {
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
    public static ColorEnum fromValue(String value) {
      for (ColorEnum b : ColorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable ColorEnum color;

  private @Nullable String customColorHex;

  /**
   * Стиль импланта
   */
  public enum StyleEnum {
    MINIMALISM("minimalism"),
    
    ROUGHNESS("roughness"),
    
    AESTHETIC("aesthetic"),
    
    TECHNOLOGICAL("technological"),
    
    ORGANIC("organic");

    private final String value;

    StyleEnum(String value) {
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
    public static StyleEnum fromValue(String value) {
      for (StyleEnum b : StyleEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable StyleEnum style;

  private @Nullable CustomizeImplantVisualsRequestLightEffects lightEffects;

  /**
   * Видимость импланта
   */
  public enum VisibilityEnum {
    FULL("full"),
    
    PARTIAL("partial"),
    
    HIDDEN("hidden");

    private final String value;

    VisibilityEnum(String value) {
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
    public static VisibilityEnum fromValue(String value) {
      for (VisibilityEnum b : VisibilityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable VisibilityEnum visibility;

  public CustomizeImplantVisualsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CustomizeImplantVisualsRequest(String characterId) {
    this.characterId = characterId;
  }

  public CustomizeImplantVisualsRequest characterId(String characterId) {
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

  public CustomizeImplantVisualsRequest color(@Nullable ColorEnum color) {
    this.color = color;
    return this;
  }

  /**
   * Цвет кибервара
   * @return color
   */
  
  @Schema(name = "color", description = "Цвет кибервара", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("color")
  public @Nullable ColorEnum getColor() {
    return color;
  }

  public void setColor(@Nullable ColorEnum color) {
    this.color = color;
  }

  public CustomizeImplantVisualsRequest customColorHex(@Nullable String customColorHex) {
    this.customColorHex = customColorHex;
    return this;
  }

  /**
   * Кастомный цвет (hex) если color=custom
   * @return customColorHex
   */
  
  @Schema(name = "custom_color_hex", description = "Кастомный цвет (hex) если color=custom", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("custom_color_hex")
  public @Nullable String getCustomColorHex() {
    return customColorHex;
  }

  public void setCustomColorHex(@Nullable String customColorHex) {
    this.customColorHex = customColorHex;
  }

  public CustomizeImplantVisualsRequest style(@Nullable StyleEnum style) {
    this.style = style;
    return this;
  }

  /**
   * Стиль импланта
   * @return style
   */
  
  @Schema(name = "style", description = "Стиль импланта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("style")
  public @Nullable StyleEnum getStyle() {
    return style;
  }

  public void setStyle(@Nullable StyleEnum style) {
    this.style = style;
  }

  public CustomizeImplantVisualsRequest lightEffects(@Nullable CustomizeImplantVisualsRequestLightEffects lightEffects) {
    this.lightEffects = lightEffects;
    return this;
  }

  /**
   * Get lightEffects
   * @return lightEffects
   */
  @Valid 
  @Schema(name = "light_effects", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("light_effects")
  public @Nullable CustomizeImplantVisualsRequestLightEffects getLightEffects() {
    return lightEffects;
  }

  public void setLightEffects(@Nullable CustomizeImplantVisualsRequestLightEffects lightEffects) {
    this.lightEffects = lightEffects;
  }

  public CustomizeImplantVisualsRequest visibility(@Nullable VisibilityEnum visibility) {
    this.visibility = visibility;
    return this;
  }

  /**
   * Видимость импланта
   * @return visibility
   */
  
  @Schema(name = "visibility", description = "Видимость импланта", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("visibility")
  public @Nullable VisibilityEnum getVisibility() {
    return visibility;
  }

  public void setVisibility(@Nullable VisibilityEnum visibility) {
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
    CustomizeImplantVisualsRequest customizeImplantVisualsRequest = (CustomizeImplantVisualsRequest) o;
    return Objects.equals(this.characterId, customizeImplantVisualsRequest.characterId) &&
        Objects.equals(this.color, customizeImplantVisualsRequest.color) &&
        Objects.equals(this.customColorHex, customizeImplantVisualsRequest.customColorHex) &&
        Objects.equals(this.style, customizeImplantVisualsRequest.style) &&
        Objects.equals(this.lightEffects, customizeImplantVisualsRequest.lightEffects) &&
        Objects.equals(this.visibility, customizeImplantVisualsRequest.visibility);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, color, customColorHex, style, lightEffects, visibility);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CustomizeImplantVisualsRequest {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    customColorHex: ").append(toIndentedString(customColorHex)).append("\n");
    sb.append("    style: ").append(toIndentedString(style)).append("\n");
    sb.append("    lightEffects: ").append(toIndentedString(lightEffects)).append("\n");
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

