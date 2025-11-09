package com.necpgame.gameplayservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * TutorialStep
 */


public class TutorialStep {

  private String id;

  private String title;

  private String description;

  private String hint;

  public TutorialStep() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TutorialStep(String id, String title, String description, String hint) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.hint = hint;
  }

  public TutorialStep id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Уникальный идентификатор шага
   * @return id
   */
  @NotNull 
  @Schema(name = "id", example = "step-1", description = "Уникальный идентификатор шага", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TutorialStep title(String title) {
    this.title = title;
    return this;
  }

  /**
   * Заголовок шага
   * @return title
   */
  @NotNull @Size(min = 1, max = 100) 
  @Schema(name = "title", example = "Добро пожаловать", description = "Заголовок шага", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public TutorialStep description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Описание шага
   * @return description
   */
  @NotNull @Size(min = 10, max = 500) 
  @Schema(name = "description", example = "Это ваш первый день в Night City. Вы находитесь в корпоративном районе Downtown.", description = "Описание шага", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public TutorialStep hint(String hint) {
    this.hint = hint;
    return this;
  }

  /**
   * Подсказка для игрока
   * @return hint
   */
  @NotNull @Size(min = 1, max = 200) 
  @Schema(name = "hint", example = "Изучите интерфейс и выберите действие", description = "Подсказка для игрока", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("hint")
  public String getHint() {
    return hint;
  }

  public void setHint(String hint) {
    this.hint = hint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TutorialStep tutorialStep = (TutorialStep) o;
    return Objects.equals(this.id, tutorialStep.id) &&
        Objects.equals(this.title, tutorialStep.title) &&
        Objects.equals(this.description, tutorialStep.description) &&
        Objects.equals(this.hint, tutorialStep.hint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, title, description, hint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TutorialStep {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    hint: ").append(toIndentedString(hint)).append("\n");
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

