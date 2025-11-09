package com.necpgame.narrativeservice.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.necpgame.narrativeservice.model.TutorialHintUiBinding;
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
 * TutorialHint
 */


public class TutorialHint {

  private String id;

  /**
   * Gets or Sets category
   */
  public enum CategoryEnum {
    MOVEMENT("movement"),
    
    STEALTH("stealth"),
    
    TECH("tech"),
    
    SOCIAL("social"),
    
    NARRATIVE("narrative");

    private final String value;

    CategoryEnum(String value) {
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
    public static CategoryEnum fromValue(String value) {
      for (CategoryEnum b : CategoryEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private CategoryEnum category;

  private Integer priority;

  private String textKey;

  @Valid
  private List<String> conditions = new ArrayList<>();

  private @Nullable TutorialHintUiBinding uiBinding;

  private @Nullable Integer cooldownSeconds;

  private @Nullable String videoClipId;

  public TutorialHint() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public TutorialHint(String id, CategoryEnum category, Integer priority, String textKey) {
    this.id = id;
    this.category = category;
    this.priority = priority;
    this.textKey = textKey;
  }

  public TutorialHint id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @NotNull 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public TutorialHint category(CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * Get category
   * @return category
   */
  @NotNull 
  @Schema(name = "category", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("category")
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(CategoryEnum category) {
    this.category = category;
  }

  public TutorialHint priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Get priority
   * @return priority
   */
  @NotNull 
  @Schema(name = "priority", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("priority")
  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public TutorialHint textKey(String textKey) {
    this.textKey = textKey;
    return this;
  }

  /**
   * Get textKey
   * @return textKey
   */
  @NotNull 
  @Schema(name = "textKey", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("textKey")
  public String getTextKey() {
    return textKey;
  }

  public void setTextKey(String textKey) {
    this.textKey = textKey;
  }

  public TutorialHint conditions(List<String> conditions) {
    this.conditions = conditions;
    return this;
  }

  public TutorialHint addConditionsItem(String conditionsItem) {
    if (this.conditions == null) {
      this.conditions = new ArrayList<>();
    }
    this.conditions.add(conditionsItem);
    return this;
  }

  /**
   * Get conditions
   * @return conditions
   */
  
  @Schema(name = "conditions", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("conditions")
  public List<String> getConditions() {
    return conditions;
  }

  public void setConditions(List<String> conditions) {
    this.conditions = conditions;
  }

  public TutorialHint uiBinding(@Nullable TutorialHintUiBinding uiBinding) {
    this.uiBinding = uiBinding;
    return this;
  }

  /**
   * Get uiBinding
   * @return uiBinding
   */
  @Valid 
  @Schema(name = "uiBinding", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uiBinding")
  public @Nullable TutorialHintUiBinding getUiBinding() {
    return uiBinding;
  }

  public void setUiBinding(@Nullable TutorialHintUiBinding uiBinding) {
    this.uiBinding = uiBinding;
  }

  public TutorialHint cooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
    return this;
  }

  /**
   * Get cooldownSeconds
   * @return cooldownSeconds
   */
  
  @Schema(name = "cooldownSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cooldownSeconds")
  public @Nullable Integer getCooldownSeconds() {
    return cooldownSeconds;
  }

  public void setCooldownSeconds(@Nullable Integer cooldownSeconds) {
    this.cooldownSeconds = cooldownSeconds;
  }

  public TutorialHint videoClipId(@Nullable String videoClipId) {
    this.videoClipId = videoClipId;
    return this;
  }

  /**
   * Get videoClipId
   * @return videoClipId
   */
  
  @Schema(name = "videoClipId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("videoClipId")
  public @Nullable String getVideoClipId() {
    return videoClipId;
  }

  public void setVideoClipId(@Nullable String videoClipId) {
    this.videoClipId = videoClipId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TutorialHint tutorialHint = (TutorialHint) o;
    return Objects.equals(this.id, tutorialHint.id) &&
        Objects.equals(this.category, tutorialHint.category) &&
        Objects.equals(this.priority, tutorialHint.priority) &&
        Objects.equals(this.textKey, tutorialHint.textKey) &&
        Objects.equals(this.conditions, tutorialHint.conditions) &&
        Objects.equals(this.uiBinding, tutorialHint.uiBinding) &&
        Objects.equals(this.cooldownSeconds, tutorialHint.cooldownSeconds) &&
        Objects.equals(this.videoClipId, tutorialHint.videoClipId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, category, priority, textKey, conditions, uiBinding, cooldownSeconds, videoClipId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TutorialHint {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    textKey: ").append(toIndentedString(textKey)).append("\n");
    sb.append("    conditions: ").append(toIndentedString(conditions)).append("\n");
    sb.append("    uiBinding: ").append(toIndentedString(uiBinding)).append("\n");
    sb.append("    cooldownSeconds: ").append(toIndentedString(cooldownSeconds)).append("\n");
    sb.append("    videoClipId: ").append(toIndentedString(videoClipId)).append("\n");
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

