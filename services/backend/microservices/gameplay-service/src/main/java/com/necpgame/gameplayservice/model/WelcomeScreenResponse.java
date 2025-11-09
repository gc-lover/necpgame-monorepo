package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.necpgame.gameplayservice.model.WelcomeScreenResponseButtonsInner;
import com.necpgame.gameplayservice.model.WelcomeScreenResponseCharacter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * WelcomeScreenResponse
 */


public class WelcomeScreenResponse {

  private String message;

  private String subtitle;

  private WelcomeScreenResponseCharacter character;

  private String startingLocation;

  @Valid
  private List<@Valid WelcomeScreenResponseButtonsInner> buttons = new ArrayList<>();

  public WelcomeScreenResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public WelcomeScreenResponse(String message, String subtitle, WelcomeScreenResponseCharacter character, String startingLocation, List<@Valid WelcomeScreenResponseButtonsInner> buttons) {
    this.message = message;
    this.subtitle = subtitle;
    this.character = character;
    this.startingLocation = startingLocation;
    this.buttons = buttons;
  }

  public WelcomeScreenResponse message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Приветственное сообщение
   * @return message
   */
  @NotNull 
  @Schema(name = "message", example = "Добро пожаловать в NECPGAME", description = "Приветственное сообщение", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public WelcomeScreenResponse subtitle(String subtitle) {
    this.subtitle = subtitle;
    return this;
  }

  /**
   * Подзаголовок
   * @return subtitle
   */
  @NotNull 
  @Schema(name = "subtitle", example = "Ночь в Night City только начинается...", description = "Подзаголовок", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("subtitle")
  public String getSubtitle() {
    return subtitle;
  }

  public void setSubtitle(String subtitle) {
    this.subtitle = subtitle;
  }

  public WelcomeScreenResponse character(WelcomeScreenResponseCharacter character) {
    this.character = character;
    return this;
  }

  /**
   * Get character
   * @return character
   */
  @NotNull @Valid 
  @Schema(name = "character", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("character")
  public WelcomeScreenResponseCharacter getCharacter() {
    return character;
  }

  public void setCharacter(WelcomeScreenResponseCharacter character) {
    this.character = character;
  }

  public WelcomeScreenResponse startingLocation(String startingLocation) {
    this.startingLocation = startingLocation;
    return this;
  }

  /**
   * Стартовая локация
   * @return startingLocation
   */
  @NotNull 
  @Schema(name = "startingLocation", example = "Night City - Downtown", description = "Стартовая локация", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startingLocation")
  public String getStartingLocation() {
    return startingLocation;
  }

  public void setStartingLocation(String startingLocation) {
    this.startingLocation = startingLocation;
  }

  public WelcomeScreenResponse buttons(List<@Valid WelcomeScreenResponseButtonsInner> buttons) {
    this.buttons = buttons;
    return this;
  }

  public WelcomeScreenResponse addButtonsItem(WelcomeScreenResponseButtonsInner buttonsItem) {
    if (this.buttons == null) {
      this.buttons = new ArrayList<>();
    }
    this.buttons.add(buttonsItem);
    return this;
  }

  /**
   * Get buttons
   * @return buttons
   */
  @NotNull @Valid 
  @Schema(name = "buttons", example = "[{\"id\":\"start-game\",\"label\":\"Начать игру\"},{\"id\":\"skip-tutorial\",\"label\":\"Пропустить туториал\"}]", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("buttons")
  public List<@Valid WelcomeScreenResponseButtonsInner> getButtons() {
    return buttons;
  }

  public void setButtons(List<@Valid WelcomeScreenResponseButtonsInner> buttons) {
    this.buttons = buttons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeScreenResponse welcomeScreenResponse = (WelcomeScreenResponse) o;
    return Objects.equals(this.message, welcomeScreenResponse.message) &&
        Objects.equals(this.subtitle, welcomeScreenResponse.subtitle) &&
        Objects.equals(this.character, welcomeScreenResponse.character) &&
        Objects.equals(this.startingLocation, welcomeScreenResponse.startingLocation) &&
        Objects.equals(this.buttons, welcomeScreenResponse.buttons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, subtitle, character, startingLocation, buttons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WelcomeScreenResponse {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    subtitle: ").append(toIndentedString(subtitle)).append("\n");
    sb.append("    character: ").append(toIndentedString(character)).append("\n");
    sb.append("    startingLocation: ").append(toIndentedString(startingLocation)).append("\n");
    sb.append("    buttons: ").append(toIndentedString(buttons)).append("\n");
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

