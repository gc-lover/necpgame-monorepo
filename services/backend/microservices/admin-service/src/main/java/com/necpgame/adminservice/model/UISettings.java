package com.necpgame.adminservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
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
 * UISettings
 */


public class UISettings {

  private @Nullable UUID characterId;

  private @Nullable Float hudScale;

  private @Nullable Boolean showMinimap;

  private @Nullable Boolean showDamageNumbers;

  private @Nullable Boolean showQuestTracker;

  private @Nullable Integer chatFontSize;

  /**
   * Gets or Sets uiTheme
   */
  public enum UiThemeEnum {
    DARK("DARK"),
    
    LIGHT("LIGHT"),
    
    CYBERPUNK("CYBERPUNK");

    private final String value;

    UiThemeEnum(String value) {
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
    public static UiThemeEnum fromValue(String value) {
      for (UiThemeEnum b : UiThemeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private @Nullable UiThemeEnum uiTheme;

  @Valid
  private Map<String, Object> customSettings = new HashMap<>();

  public UISettings characterId(@Nullable UUID characterId) {
    this.characterId = characterId;
    return this;
  }

  /**
   * Get characterId
   * @return characterId
   */
  @Valid 
  @Schema(name = "character_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("character_id")
  public @Nullable UUID getCharacterId() {
    return characterId;
  }

  public void setCharacterId(@Nullable UUID characterId) {
    this.characterId = characterId;
  }

  public UISettings hudScale(@Nullable Float hudScale) {
    this.hudScale = hudScale;
    return this;
  }

  /**
   * Get hudScale
   * @return hudScale
   */
  
  @Schema(name = "hud_scale", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("hud_scale")
  public @Nullable Float getHudScale() {
    return hudScale;
  }

  public void setHudScale(@Nullable Float hudScale) {
    this.hudScale = hudScale;
  }

  public UISettings showMinimap(@Nullable Boolean showMinimap) {
    this.showMinimap = showMinimap;
    return this;
  }

  /**
   * Get showMinimap
   * @return showMinimap
   */
  
  @Schema(name = "show_minimap", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("show_minimap")
  public @Nullable Boolean getShowMinimap() {
    return showMinimap;
  }

  public void setShowMinimap(@Nullable Boolean showMinimap) {
    this.showMinimap = showMinimap;
  }

  public UISettings showDamageNumbers(@Nullable Boolean showDamageNumbers) {
    this.showDamageNumbers = showDamageNumbers;
    return this;
  }

  /**
   * Get showDamageNumbers
   * @return showDamageNumbers
   */
  
  @Schema(name = "show_damage_numbers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("show_damage_numbers")
  public @Nullable Boolean getShowDamageNumbers() {
    return showDamageNumbers;
  }

  public void setShowDamageNumbers(@Nullable Boolean showDamageNumbers) {
    this.showDamageNumbers = showDamageNumbers;
  }

  public UISettings showQuestTracker(@Nullable Boolean showQuestTracker) {
    this.showQuestTracker = showQuestTracker;
    return this;
  }

  /**
   * Get showQuestTracker
   * @return showQuestTracker
   */
  
  @Schema(name = "show_quest_tracker", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("show_quest_tracker")
  public @Nullable Boolean getShowQuestTracker() {
    return showQuestTracker;
  }

  public void setShowQuestTracker(@Nullable Boolean showQuestTracker) {
    this.showQuestTracker = showQuestTracker;
  }

  public UISettings chatFontSize(@Nullable Integer chatFontSize) {
    this.chatFontSize = chatFontSize;
    return this;
  }

  /**
   * Get chatFontSize
   * @return chatFontSize
   */
  
  @Schema(name = "chat_font_size", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("chat_font_size")
  public @Nullable Integer getChatFontSize() {
    return chatFontSize;
  }

  public void setChatFontSize(@Nullable Integer chatFontSize) {
    this.chatFontSize = chatFontSize;
  }

  public UISettings uiTheme(@Nullable UiThemeEnum uiTheme) {
    this.uiTheme = uiTheme;
    return this;
  }

  /**
   * Get uiTheme
   * @return uiTheme
   */
  
  @Schema(name = "ui_theme", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ui_theme")
  public @Nullable UiThemeEnum getUiTheme() {
    return uiTheme;
  }

  public void setUiTheme(@Nullable UiThemeEnum uiTheme) {
    this.uiTheme = uiTheme;
  }

  public UISettings customSettings(Map<String, Object> customSettings) {
    this.customSettings = customSettings;
    return this;
  }

  public UISettings putCustomSettingsItem(String key, Object customSettingsItem) {
    if (this.customSettings == null) {
      this.customSettings = new HashMap<>();
    }
    this.customSettings.put(key, customSettingsItem);
    return this;
  }

  /**
   * Get customSettings
   * @return customSettings
   */
  
  @Schema(name = "custom_settings", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("custom_settings")
  public Map<String, Object> getCustomSettings() {
    return customSettings;
  }

  public void setCustomSettings(Map<String, Object> customSettings) {
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
    UISettings uiSettings = (UISettings) o;
    return Objects.equals(this.characterId, uiSettings.characterId) &&
        Objects.equals(this.hudScale, uiSettings.hudScale) &&
        Objects.equals(this.showMinimap, uiSettings.showMinimap) &&
        Objects.equals(this.showDamageNumbers, uiSettings.showDamageNumbers) &&
        Objects.equals(this.showQuestTracker, uiSettings.showQuestTracker) &&
        Objects.equals(this.chatFontSize, uiSettings.chatFontSize) &&
        Objects.equals(this.uiTheme, uiSettings.uiTheme) &&
        Objects.equals(this.customSettings, uiSettings.customSettings);
  }

  @Override
  public int hashCode() {
    return Objects.hash(characterId, hudScale, showMinimap, showDamageNumbers, showQuestTracker, chatFontSize, uiTheme, customSettings);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UISettings {\n");
    sb.append("    characterId: ").append(toIndentedString(characterId)).append("\n");
    sb.append("    hudScale: ").append(toIndentedString(hudScale)).append("\n");
    sb.append("    showMinimap: ").append(toIndentedString(showMinimap)).append("\n");
    sb.append("    showDamageNumbers: ").append(toIndentedString(showDamageNumbers)).append("\n");
    sb.append("    showQuestTracker: ").append(toIndentedString(showQuestTracker)).append("\n");
    sb.append("    chatFontSize: ").append(toIndentedString(chatFontSize)).append("\n");
    sb.append("    uiTheme: ").append(toIndentedString(uiTheme)).append("\n");
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

